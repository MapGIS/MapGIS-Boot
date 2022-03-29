package com.zondy.mapgis.file.config;

import com.zondy.mapgis.file.api.config.properties.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 通用映射配置
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Autowired
    private FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(fileProperties.getPrefix() + "/**")
                .addResourceLocations("file:" + fileProperties.getFullPath() + File.separator);
    }

    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping(fileProperties.getPrefix() + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("GET");
    }
}