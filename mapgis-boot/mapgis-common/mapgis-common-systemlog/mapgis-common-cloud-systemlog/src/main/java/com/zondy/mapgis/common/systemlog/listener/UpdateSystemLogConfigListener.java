package com.zondy.mapgis.common.systemlog.listener;

import com.zondy.mapgis.common.cache.listener.IRedisListener;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.utils.LogUtils;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 更新HTTP访问日志配置监听器
 *
 * @author xiongbo
 * @since 2022/12/8 16:40
 */
@Component(Constants.UPDATE_SYSTEM_LOG_CONFIG_LISTENER)
public class UpdateSystemLogConfigListener implements IRedisListener {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Override
    public void onMessage(HashMap<String, Object> message) {
        LogUtils.setLogLevel("com.zondy.mapgis", sysServiceProxy.getLogConfig().getSystemLoglevel());
    }
}
