package com.zondy.mapgis.common.security.config;

import com.zondy.mapgis.common.core.config.ProductConfig;
import com.zondy.mapgis.common.repeatsubmit.interceptor.RepeatSubmitInterceptor;
import com.zondy.mapgis.file.api.config.properties.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.io.File;

/**
 * 通用配置
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private RepeatSubmitInterceptor repeatSubmitInterceptor;

    @Autowired
    private ProductConfig productConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(fileProperties.getPrefix() + "/**")
                .addResourceLocations("file:" + fileProperties.getFullPath() + File.separator);
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
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
