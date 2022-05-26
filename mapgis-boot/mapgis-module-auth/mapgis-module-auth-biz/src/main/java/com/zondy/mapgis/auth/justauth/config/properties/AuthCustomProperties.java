package com.zondy.mapgis.auth.justauth.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongbo
 * @since 2022/5/25 20:14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "justauth.extend")
public class AuthCustomProperties {
    /**
     * 授权的api
     */
    private String authorize;

    /**
     * 获取accessToken的api
     */
    private String accessToken;

    /**
     * 获取用户信息的api
     */
    private String userInfo;
}