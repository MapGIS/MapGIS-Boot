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
}
