package com.zondy.mapgis.auth.cas.listener;

import com.zondy.mapgis.auth.cas.config.CasSecurityConfig;
import com.zondy.mapgis.common.cache.listener.IRedisListener;
import com.zondy.mapgis.common.core.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 更新CAS配置的监听器（用于微服务版监听）
 *
 * @author powanjuanshu
 * @since 2022/10/29 16:37
 */
@Component(Constants.UPDATE_CAS_CONFIG_LISTENER)
public class UpdateCasConfigListener implements IRedisListener {
    @Autowired
    private CasSecurityConfig casSecurityConfig;

    @Override
    public void onMessage(HashMap<String, Object> message) {
        casSecurityConfig.updateCasSecurityConfig();
    }
}
