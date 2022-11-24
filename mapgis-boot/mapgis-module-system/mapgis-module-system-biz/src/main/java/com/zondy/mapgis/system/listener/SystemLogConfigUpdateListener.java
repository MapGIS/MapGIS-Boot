package com.zondy.mapgis.system.listener;

import com.zondy.mapgis.common.systemlog.utils.LogUtils;
import com.zondy.mapgis.system.api.event.SysConfigEvent;
import com.zondy.mapgis.system.api.event.SysEventConstants;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 系统日志配置更新监听器（用于单体版）
 *
 * @author xiongbo
 * @since 2022/11/24 14:31
 */
@Component
public class SystemLogConfigUpdateListener implements ApplicationListener<SysConfigEvent> {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Override
    public void onApplicationEvent(SysConfigEvent event) {
        if (event.getName() == SysEventConstants.LOG_SYSTEM_LOG_CONFIG_UPDATE) {
            LogUtils.setLogLevel("com.zondy.mapgis", sysServiceProxy.getLogConfig().getSystemLoglevel());
        }
    }
}
