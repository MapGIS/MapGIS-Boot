package com.zondy.mapgis.common.webserver.config;

import com.zondy.mapgis.common.core.config.ProductConfig;
import com.zondy.mapgis.common.webserver.factory.UndertowCustomFactory;
import com.zondy.mapgis.common.webserver.filter.CaseInsensitiveRequestParameterNameFilter;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WebServer自动配置
 *
 * @author Chelsea
 * @since 2023/1/12 10:06
 */
@Configuration
public class WebServerAutoConfiguration {
    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowServletWebServerCustomizer() {
        return new UndertowCustomFactory().createUndertowServletWebServerFactoryCustomizer();
    }

    @Bean
    public FilterRegistrationBean<CaseInsensitiveRequestParameterNameFilter> caseInsensitiveRequestParameterNameFilter(ProductConfig productConfig) {
        FilterRegistrationBean<CaseInsensitiveRequestParameterNameFilter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/" + productConfig.getName() + "/rest/*");
        bean.setFilter(new CaseInsensitiveRequestParameterNameFilter());
        bean.setOrder(Integer.MIN_VALUE + 1);
        return bean;
    }
}
