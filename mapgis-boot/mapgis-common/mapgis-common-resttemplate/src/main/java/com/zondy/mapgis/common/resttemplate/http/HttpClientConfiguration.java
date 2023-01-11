package com.zondy.mapgis.common.resttemplate.http;

import com.zondy.mapgis.common.resttemplate.properties.HttpClientProperties;
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
public class HttpClientConfiguration {
}
