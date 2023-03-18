package com.zondy.mapgis.common.meter.config;

import com.zondy.mapgis.common.meter.context.MetricContext;
import com.zondy.mapgis.common.meter.filter.PerformanceMeterFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.Filter;

/**
 * Filter配置
 *
 * @author xiongbo
 * @since 2022/12/5 9:24
 */
@Configuration
public class MeterFilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> performanceMeterFilterRegistration(MetricContext metricContext) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new PerformanceMeterFilter(metricContext));
        registration.addUrlPatterns("/*");
        registration.setName("performanceMeterFilter");
        // 有一些过滤器需要在它之前，比如ServiceMeterFilter
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
        return registration;
    }
}
