package com.zondy.mapgis.common.security.annotation;

import java.lang.annotation.*;

/**
 * 内部认证注解
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth {
    /**
     * 是否校验用户信息
     */
    boolean isUser() default false;
}