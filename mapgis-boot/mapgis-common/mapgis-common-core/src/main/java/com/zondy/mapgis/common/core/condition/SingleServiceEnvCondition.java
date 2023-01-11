package com.zondy.mapgis.common.core.condition;

import com.zondy.mapgis.common.core.utils.EnvUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 是否为单体版环境条件
 *
 * @author xiongbo
 * @since 2023/1/11 14:38
 */
public class SingleServiceEnvCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        return EnvUtils.isSingleServiceMode(env);
    }
}