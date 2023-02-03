package com.zondy.mapgis.common.json;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jackson可选模块的注册，注意ConditionalOnClass注解的使用
 *
 * @author Chelsea
 * @since 2023/1/11 10:20
 */
@Configuration
public class JacksonModuleConfiguration {
    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule")
    public Module jsonOrgModule() {
        return new JsonOrgModuleCondition().getModule();
    }

    @Bean
    @ConditionalOnClass(name = {"org.locationtech.jts.geom.Geometry", "com.bedatadriven.jackson.datatype.jts.JtsModule"})
    public Module jtsGeometryModule() {
        return new GeoJsonModuleCondition().getModule();
    }
}
