package com.zondy.mapgis.gateway.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import com.zondy.mapgis.common.cache.service.CacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.gateway.domain.SysGatewayRoute;
import com.zondy.mapgis.gateway.repository.InMemoryRouteDefinitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 网关路由服务
 *
 * @author powanjuanshu
 * @since 2022/9/21 9:06
 */
@Slf4j
@Component
public class SysGatewayRouteService implements ApplicationEventPublisherAware {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private Environment env;
    @Autowired
    private InMemoryRouteDefinitionRepository repository;

    private final static String[] KEY_ROUTERS = new String[]{"Path", "Host", "Method", "After", "Before", "Between", "RemoteAddr"};

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 从Redis中读取路由配置
     *
     * @param message 消息对象
     */
    public void refresh(HashMap<String, Object> message) {
        List<SysGatewayRoute> routes = Lists.newArrayList();
        final String strRoutes = cacheService.getCacheObject(CacheConstants.SYS_ROUTES_KEY);

        if (StringUtils.isNotEmpty(strRoutes)) {
            log.info("gateway routes:\r\n{}", strRoutes);
            final List<Dict> dicts = JsonUtils.parseArrayMap(strRoutes);

            try {
                routes = parseRoutes(dicts);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("gateway routes is empty");
        }

        addOpenApiRoute();

        for (SysGatewayRoute route : routes) {
            String status = route.getStatus();

            log.info("gateway route: id={}, status={}, {}", route.getId(), route.getStatus(), route.toString());
            if (status.equals("0")) {
                addRoute(route);
            } else {
                deleteRoute(route.getId());
            }
        }
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    public List<SysGatewayRoute> parseRoutes(List<Dict> dicts) throws URISyntaxException {
        List<SysGatewayRoute> routes = Lists.newArrayList();

        for (Dict dict : dicts) {
            SysGatewayRoute route = new SysGatewayRoute();

            route.setId(env.resolvePlaceholders(dict.getStr("routeId")));
            route.setUri(new URI(env.resolvePlaceholders(dict.getStr("uri"))));
            route.setStatus(dict.getStr("status"));
            route.setOrder(dict.getInt("orderNum"));

            try {
                final List<Dict> predicates = JsonUtils.parseArrayMap(dict.getStr("predicates"));

                if (StringUtils.isNotEmpty(predicates)) {
                    List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();

                    for (Dict predicate : predicates) {
                        PredicateDefinition predicateDefinition = new PredicateDefinition();
                        final String predicateName = predicate.getStr("name");
                        predicateDefinition.setName(predicateName);

                        if (ArrayUtil.contains(KEY_ROUTERS, predicateName)) {
                            final Object args = predicate.getObj("args");
                            if (StringUtils.isNotNull(args) && args instanceof ArrayList) {
                                ArrayList argList = (ArrayList) args;

                                for (int i = 0; i < argList.size(); i++) {
                                    predicateDefinition.addArg("_genkey" + i, env.resolvePlaceholders(argList.get(i).toString()));
                                }
                            }
                        } else {
                            final Object args = predicate.getObj("args");
                            if (StringUtils.isNotNull(args) && args instanceof LinkedHashMap) {
                                for (Map.Entry arg : ((LinkedHashMap<?, ?>) args).entrySet()) {
                                    if (StringUtils.isNotNull(arg.getValue())) {
                                        predicateDefinition.addArg(arg.getKey().toString(), env.resolvePlaceholders(arg.getValue().toString()));
                                    }
                                }
                            }
                        }
                        predicateDefinitionList.add(predicateDefinition);
                    }
                    route.setPredicates(predicateDefinitionList);
                }
            } catch (RuntimeException exception) {
                log.error("gateway route: id={}, parse predicates error: {}", route.getId(), exception.getMessage());
                continue;
            }

            try {
                final List<Dict> filters = JsonUtils.parseArrayMap(dict.getStr("filters"));

                if (StringUtils.isNotEmpty(filters)) {
                    List<FilterDefinition> filterDefinitions = new ArrayList<>();

                    for (Dict filter : filters) {
                        FilterDefinition filterDefinition = new FilterDefinition();
                        final String filterName = filter.getStr("name");
                        filterDefinition.setName(filterName);

                        final Object args = filter.getObj("args");
                        if (StringUtils.isNotNull(args) && args instanceof ArrayList) {
                            ArrayList argList = (ArrayList) args;
                            for (int i = 0; i < argList.size(); i++) {
                                final Object arg = argList.get(i);
                                if (StringUtils.isNotNull(arg) && arg instanceof LinkedHashMap) {
                                    Object key = ((LinkedHashMap<?, ?>) arg).get("key");
                                    Object value = ((LinkedHashMap<?, ?>) arg).get("value");
                                    if (StringUtils.isNotNull(key) && StringUtils.isNotNull(value)) {
                                        filterDefinition.addArg(key.toString(), value.toString());
                                    }
                                }
                            }
                        }
                        filterDefinitions.add(filterDefinition);
                    }
                    route.setFilters(filterDefinitions);
                }
            } catch (RuntimeException exception) {
                log.error("gateway route: id={}, parse filters error: {}", route.getId(), exception.getMessage());
                continue;
            }
            routes.add(route);
        }
        return routes;
    }

    /**
     * 添加openapi路由
     */
    public void addOpenApiRoute() {
        try {
            SysGatewayRoute openapiRoute = new SysGatewayRoute();
            openapiRoute.setOrder(0);
            openapiRoute.setId("openapi");
            openapiRoute.setUri(new URI(env.resolvePlaceholders("lb://${mapgis.product.full-name}-gateway-server")));

            List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            Map<String, String> predicateArgs = new HashMap<>();

            predicateDefinition.setName("Path");
            predicateArgs.put("_genkey0", "/v3/api-docs/**");
            predicateDefinition.setArgs(predicateArgs);

            predicateDefinitionList.add(predicateDefinition);
            openapiRoute.setPredicates(predicateDefinitionList);

            List<FilterDefinition> filterDefinitions = new ArrayList<>();
            FilterDefinition filterDefinition = new FilterDefinition();
            Map<String, String> filterArgs = new HashMap<>();

            filterDefinition.setName("RewritePath");
            filterArgs.put("regexp", "/v3/api-docs/(?<path>.*)");
            filterArgs.put("replacement", "/$\\{path}/v3/api-docs");
            filterDefinition.setArgs(filterArgs);

            filterDefinitions.add(filterDefinition);
            openapiRoute.setFilters(filterDefinitions);

            addRoute(openapiRoute);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加路由
     *
     * @param definition 路由对象
     */
    public synchronized void addRoute(RouteDefinition definition) {
        try {
            repository.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 删除路由
     *
     * @param id 路由id
     * @return
     */
    public synchronized void deleteRoute(String id) {
        try {
            repository.delete(Mono.just(id)).subscribe();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }
}
