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

    public SysLogConfig() {
        systemLoglevel = "INFO";
    }
}
