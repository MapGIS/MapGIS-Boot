package com.zondy.mapgis.common.systemlog.listener;

import com.zondy.mapgis.common.core.utils.LogUtils;
import com.zondy.mapgis.system.api.event.SysConfigEvent;
import com.zondy.mapgis.system.api.event.SysEventConstants;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 更新系统日志配置监听器（用于单体版监听）
 *
 * @author xiongbo
 * @since 2022/11/24 14:31
 */
@Component
public class UpdateSystemLogConfigListener implements ApplicationListener<SysConfigEvent> {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Override
    public void onApplicationEvent(SysConfigEvent event) {
        if (event.getName() == SysEventConstants.UPDATE_SYSTEM_LOG) {
            LogUtils.setLogLevel("com.zondy.mapgis", sysServiceProxy.getLogConfig().getSystemLoglevel());
        }
    }
}
