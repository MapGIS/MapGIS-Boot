package com.zondy.mapgis.common.service.constant;

/**
 * 通用常量信息
 *
 * @author xiongbo
 * @since 2023/1/11 15:52
 */
public class Constants {
    /**
     * 只读的用户id
     */
    public static final String READONLY_USER_ID = "__readonly_user__";

    /**
     * 临时资源缓存最大的空闲时间，超过该时间清理缓存；默认4小时
     */
    public static final int TEMP_RESOURCE_MAX_IDLE_SECONDS = 3600 * 4;
}
