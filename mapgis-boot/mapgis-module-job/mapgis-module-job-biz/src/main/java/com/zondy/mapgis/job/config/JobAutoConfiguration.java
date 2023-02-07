package com.zondy.mapgis.job.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongbo
 * @since 2023/2/7 13:18
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.job.controller", "com.zondy.mapgis.job.service.impl", "com.zondy.mapgis.job.task"})
public class JobAutoConfiguration {
}
