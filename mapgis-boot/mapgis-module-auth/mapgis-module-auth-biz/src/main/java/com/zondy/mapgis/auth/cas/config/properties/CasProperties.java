package com.zondy.mapgis.auth.cas.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * CAS的配置参数
 *
 * @author powanjuanshu
 * @since 2022/6/6 20:37
 */
@Data
@Configuration
public class CasProperties {
    /**
     * cas服务端地址
     */
    @Value("${cas.server.host.url}")
    private String casServerUrl;

    /**
     * cas服务端登录地址
     */
    @Value("${cas.server.host.login_url}")
    private String casServerLoginUrl;

    /**
     * cas服务端登出地址 并回跳到指定页面
     */
    @Value("${cas.server.host.logout_url}")
    private String casServerLogoutUrl;

    /**
     * 是否开启cas
     */
    @Value("${cas.enabled}")
    private boolean casEnabled;

    /**
     * cas客户端地址
     */
    @Value("${cas.service.host.url}")
    private String casServiceUrl;

    /**
     * cas客户端地址登录地址
     */
    @Value("${cas.service.host.login_url}")
    private String casServiceLoginUrl;

    /**
     * cas客户端地址登出地址
     */
    @Value("${cas.service.host.logout_url}")
    private String casServiceLogoutUrl;

    /**
     * cas客户端web地址
     */
    @Value("${cas.service.web.url}")
    private String casServiceWebUrl;
}