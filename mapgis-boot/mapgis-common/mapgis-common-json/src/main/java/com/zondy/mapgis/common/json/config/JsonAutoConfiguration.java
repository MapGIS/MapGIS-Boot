package com.zondy.mapgis.common.json.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created on 2023/2/3.
 *
 * @author Chelsea
 */
@Configuration
@Import({JacksonModuleConfiguration.class})
public class JsonAutoConfiguration {
}
