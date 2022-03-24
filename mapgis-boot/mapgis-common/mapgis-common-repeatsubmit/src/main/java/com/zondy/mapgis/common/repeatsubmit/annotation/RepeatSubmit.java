package com.zondy.mapgis.common.repeatsubmit.annotation;

import java.lang.annotation.*;

/**
 * 自定义注册防止表单重复提交
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    /**
     * 间隔时间(ms), 小于此时间视为重复提交
     */
    public long interval() default 5000;

    /**
     * 提示消息
     */
    public String message() default "不允许重复提交，请稍后再试";
}
