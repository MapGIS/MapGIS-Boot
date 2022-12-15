package com.zondy.mapgis.auth.cas.listener;

import com.zondy.mapgis.auth.cas.config.CasSecurityConfig;
import com.zondy.mapgis.system.api.event.SysConfigEvent;
import com.zondy.mapgis.system.api.event.SysEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 更新CAS配置的监听器（用于单体版监听）
 *
 * @author xiongbo
 * @since 2022/10/29 13:55
 */
@Component
public class UpdateCasConfigListener implements ApplicationListener<SysConfigEvent> {
    /**
     * CAS配置
     */
    @Autowired
    private CasSecurityConfig casSecurityConfig;

    @Override
    public void onApplicationEvent(SysConfigEvent event) {
        if (event.getName() == SysEventConstants.UPDATE_CAS_CONFIG) {
            casSecurityConfig.updateCasSecurityConfig();
        }
    }
}
