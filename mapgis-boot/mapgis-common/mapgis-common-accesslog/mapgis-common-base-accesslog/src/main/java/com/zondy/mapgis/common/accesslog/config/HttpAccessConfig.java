package com.zondy.mapgis.common.accesslog.config;

import com.zondy.mapgis.system.api.domain.SysLogConfig;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * Http访问配置
 *
 * @author xiongbo
 * @since 2022/11/29 13:58
 */
@Data
@Configuration
public class HttpAccessConfig {
    /**
     * 日志配置
     */
    private SysLogConfig logConfig = new SysLogConfig();
}
