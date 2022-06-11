package com.zondy.mapgis.common.security.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author powanjuanshu
 * @since 2022/6/9 16:16
 */
public class SecurityCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String enabled = conditionContext.getEnvironment().getProperty("cas.enabled");
        return !"true".equals(enabled);
    }
}