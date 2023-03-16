package com.zondy.mapgis.common.security.config;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.security.interceptor.HeaderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

    /**
     * 跨域配置，需要控制filter的次序，保证在jwt、token认证之前
     */
    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter(source));
        registration.setOrder(-1000);
        registration.setName("CorsFilter");
        // 返回新的CorsFilter
        return registration;
    }
}
