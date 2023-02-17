package com.zondy.mapgis.config;

import com.zondy.mapgis.common.security.filter.HttpsSecurityHeaderFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置
 *
 * @author xiongbo
 * @since 2023/2/17 10:54
 */
@Configuration
public class MapAppConfiguration {
    /**
     * 只有开启https时才开启安全头
     */
    @ConditionalOnExpression(value = "'${server.ssl.enabled}' == 'true'")
    @Bean
    public FilterRegistrationBean<HttpsSecurityHeaderFilter> httpsSecurityHeaderFilter() {
        FilterRegistrationBean<HttpsSecurityHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.addUrlPatterns("/*");
        bean.setFilter(new HttpsSecurityHeaderFilter());
        bean.setOrder(Integer.MIN_VALUE + 1);
        return bean;
    }
}
