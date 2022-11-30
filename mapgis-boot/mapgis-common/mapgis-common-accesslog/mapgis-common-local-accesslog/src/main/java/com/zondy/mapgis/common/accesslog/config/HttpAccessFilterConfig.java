package com.zondy.mapgis.common.accesslog.config;

import com.zondy.mapgis.common.accesslog.filter.HttpAccessFilter;
import com.zondy.mapgis.common.accesslog.recorder.AsyncHttpAccessRecorder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Filter配置
 *
 * @author xiongbo
 * @since 2022/11/29 15:20
 */
@Configuration
public class HttpAccessFilterConfig {
    @Bean
    public FilterRegistrationBean httpAccessFilterRegistration(HttpAccessConfig httpAccessConfig, AsyncHttpAccessRecorder asyncHttpAccessRecorder) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpAccessFilter(httpAccessConfig, asyncHttpAccessRecorder));
        registration.addUrlPatterns("/*");
        registration.setName("httpAccessFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
