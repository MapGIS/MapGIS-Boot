package com.zondy.mapgis.common.ratelimiter.enums;

/**
 * 限流类型
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP,
}
