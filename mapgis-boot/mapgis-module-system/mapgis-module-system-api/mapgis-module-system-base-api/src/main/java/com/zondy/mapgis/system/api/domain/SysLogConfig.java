package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 日志配置对象
 *
 * @author xiongbo
 * @since 2022/11/24 14:39
 */
@Data
public class SysLogConfig {
    /**
     * 系统日志级别
     */
    String systemLoglevel;

    /**
     * 是否开启服务访问日志
     */
    Boolean httpAccessEnabled;

    /**
     * 监控的服务访问URL列表，支持ant匹配表达式（**，*），多个用‘,’分隔
     */
    String[] httpAccessMonitorUrls;

    public SysLogConfig() {
        systemLoglevel = "INFO";
        httpAccessEnabled = false;
        httpAccessMonitorUrls = new String[0];
    }
}
