package com.zondy.mapgis.common.systemlog.model;

import lombok.Data;

/**
 * 系统日志信息
 *
 * @author xiongbo
 * @since 2022/11/22 18:33
 */
@Data
public class SysLoginfo {
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 日志级别
     */
    private String level;

    /**
     * 线程ID
     */
    private String pid;

    /**
     * 线程名称
     */
    private String thread;

    /**
     * 日志记录器
     */
    private String logger;

    /**
     * 日志信息
     */
    private String message;
}
