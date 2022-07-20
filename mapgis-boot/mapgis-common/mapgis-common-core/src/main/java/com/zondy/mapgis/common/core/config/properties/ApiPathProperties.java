package com.zondy.mapgis.common.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongbo
 * @since 2022/4/21 9:16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "api.path")
public class ApiPathProperties {
    String servicesPrefix;

    String managerPrefix;
}
