package com.zondy.mapgis.common.resttemplate.config;

import com.zondy.mapgis.common.resttemplate.config.properties.HttpClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * HttpClient配置类
 *
 * @author Chelsea
 * @since 2023/1/11 12:00
 */
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
public class HttpClientConfig {
}
