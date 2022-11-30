package com.zondy.mapgis.common.accesslog.listener;

import com.zondy.mapgis.common.accesslog.handler.HttpAccessLogConfigUpdateHandler;
import com.zondy.mapgis.system.api.domain.SysLogConfig;
import com.zondy.mapgis.system.api.event.SysConfigEvent;
import com.zondy.mapgis.system.api.event.SysEventConstants;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * HTTP访问日志配置更新监听器（用于单体版）
 *
 * @author xiongbo
 * @since 2022/11/30 11:11
 */
@Component
public class HttpAccessLogConfigUpdateListener implements ApplicationListener<SysConfigEvent> {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Autowired
    private HttpAccessLogConfigUpdateHandler httpAccessLogConfigUpdateHandler;

    @Override
    public void onApplicationEvent(SysConfigEvent event) {
        if (event.getName() == SysEventConstants.LOG_HTTP_ACCESS_LOG_CONFIG_UPDATE) {
            SysLogConfig postLogConfig = sysServiceProxy.getLogConfig();

            httpAccessLogConfigUpdateHandler.updateLogConfig(postLogConfig);
        }
    }
}
