package com.zondy.mapgis.gateway.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合系统接口
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class SwaggerProvider {
    @Autowired
    private GatewayProperties gatewayProperties;

    /**
     * 聚合其他服务接口
     *
     * @return
     */
    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = gatewayProperties.getRoutes();

        definitions.stream().filter(routeDefinition -> !routeDefinition.getId().equals("openapi"))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            swaggerUiConfigParameters.addGroup(routeDefinition.getId());
                            groups.add(
                                    GroupedOpenApi.builder().group(routeDefinition.getId())
                                            .pathsToMatch(predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0"))
                                            .build());
                        }));

        return groups;
    }
}
