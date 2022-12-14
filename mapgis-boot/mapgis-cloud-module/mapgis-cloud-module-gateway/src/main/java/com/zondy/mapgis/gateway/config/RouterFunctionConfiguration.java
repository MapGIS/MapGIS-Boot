package com.zondy.mapgis.gateway.config;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.gateway.handler.ValidateCodeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 路由配置信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class RouterFunctionConfiguration {
    @Autowired
    private ValidateCodeHandler validateCodeHandler;

    @Autowired
    private ApiPathProperties apiPathProperties;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET(apiPathProperties.getServicesPrefix() + "/auth/captchaImage").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                validateCodeHandler);
    }
}
