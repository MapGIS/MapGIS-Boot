package com.zondy.mapgis.system.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 系统模块自动配置
 *
 * @author xiongbo
 * @since 2023/1/11 17:36
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.system.controller", "com.zondy.mapgis.system.service.impl"})
public class SystemAutoConfiguration {
}
