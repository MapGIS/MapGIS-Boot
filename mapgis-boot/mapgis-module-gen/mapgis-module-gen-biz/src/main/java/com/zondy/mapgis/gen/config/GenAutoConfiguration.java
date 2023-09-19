package com.zondy.mapgis.gen.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author xiongbo
 * @since 2023/2/7 13:15
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.gen.controller", "com.zondy.mapgis.gen.service.impl"})
public class GenAutoConfiguration {
    @Import({GenConfig.class})
    static class GenConfigAutoConfiguration {
    }
}
