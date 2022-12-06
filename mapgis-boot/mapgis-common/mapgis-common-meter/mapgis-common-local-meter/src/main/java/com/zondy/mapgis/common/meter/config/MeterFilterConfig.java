package com.zondy.mapgis.common.meter.config;

import com.zondy.mapgis.common.meter.context.MetricContext;
import com.zondy.mapgis.common.meter.filter.PerformanceMeterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Filter配置
 *
 * @author xiongbo
 * @since 2022/12/5 9:24
 */
@Configuration
public class MeterFilterConfig {
    @Bean
    public FilterRegistrationBean performanceMeterFilterRegistration(MetricContext metricContext) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PerformanceMeterFilter(metricContext));
        registration.addUrlPatterns("/*");
        registration.setName("performanceMeterFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
