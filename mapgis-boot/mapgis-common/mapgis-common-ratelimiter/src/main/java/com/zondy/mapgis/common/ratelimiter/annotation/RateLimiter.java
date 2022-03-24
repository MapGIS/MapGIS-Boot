package com.zondy.mapgis.common.ratelimiter.annotation;

import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.ratelimiter.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    public String key() default Constants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    public int time() default 60;

    /**
     * 限流次数
     */
    public int count() default 100;

    /**
     * 限流类型
     */
    public LimitType limitType() default LimitType.DEFAULT;
}
