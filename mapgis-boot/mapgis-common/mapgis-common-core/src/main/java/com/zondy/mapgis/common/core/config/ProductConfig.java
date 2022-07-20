package com.zondy.mapgis.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取产品相关配置
 *
 * @author powanjuanshu
 * @since 2022/7/17 15:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "mapgis.product")
public class ProductConfig {
    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目完整名称
     */
    private String fullName;
}
