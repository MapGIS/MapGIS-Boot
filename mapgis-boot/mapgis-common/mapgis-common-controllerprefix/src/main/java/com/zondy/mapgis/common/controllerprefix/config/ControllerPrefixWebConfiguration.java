package com.zondy.mapgis.common.controllerprefix.config;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.controllerprefix.annotation.ServicesRestController;
import com.zondy.mapgis.common.controllerprefix.config.properties.ApiPathProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author powanjuanshu
 * @since 2022/4/21 9:14
 */
@Configuration
public class ControllerPrefixWebConfiguration implements WebMvcConfigurer {
    @Resource
    private ApiPathProperties apiPathProperties;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix(apiPathProperties.getServicesPrefix(), c -> c.isAnnotationPresent(ServicesRestController.class))
                .addPathPrefix(apiPathProperties.getManagerPrefix(), c -> c.isAnnotationPresent(ManagerRestController.class));
    }
}
