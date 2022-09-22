package com.zondy.mapgis.gateway.config;

import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;

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
    private SwaggerUiConfigParameters swaggerUiConfigParameters;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    /**
     * 聚合其他服务接口
     */
    public void refreshApis() {
        List<RouteDefinition> definitions = routeDefinitionLocator.getRouteDefinitions().collectList().block();

        definitions.stream().filter(routeDefinition -> !routeDefinition.getId().equals("openapi"))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            String routeId = routeDefinition.getId().replace("ReactiveCompositeDiscoveryClient_", "");
                            swaggerUiConfigParameters.addGroup(routeId);
                        }));
    }
}
