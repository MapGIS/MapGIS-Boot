package com.zondy.mapgis.common.security.config;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.security.interceptor.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * 拦截器配置
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 不需要拦截地址
     */
    public static String[] excludeUrls;

    @Autowired
    private ApiPathProperties apiPathProperties;

    @PostConstruct
    public void init() {
        String strManagerPrefix = apiPathProperties.getManagerPrefix();

        excludeUrls = new String[]{strManagerPrefix + "/auth/login", strManagerPrefix + "/auth/logout"};
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-10);
    }

    /**
     * 自定义请求头拦截器
     */
    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }
}
