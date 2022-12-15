package com.zondy.mapgis.common.accesslog.listener;

import com.zondy.mapgis.common.accesslog.handler.HttpAccessLogConfigUpdateHandler;
import com.zondy.mapgis.common.cache.listener.IRedisListener;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.system.api.domain.SysLogConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 更新HTTP访问日志配置监听器（用于微服务版监听）
 *
 * @author xiongbo
 * @since 2022/12/8 16:40
 */
@Component(Constants.UPDATE_HTTP_ACCESS_LOG_CONFIG_LISTENER)
public class UpdateHttpAccessLogConfigListener implements IRedisListener {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Autowired
    private HttpAccessLogConfigUpdateHandler httpAccessLogConfigUpdateHandler;

    @Override
    public void onMessage(HashMap<String, Object> message) {
        SysLogConfig postLogConfig = sysServiceProxy.getLogConfig();

        httpAccessLogConfigUpdateHandler.updateLogConfig(postLogConfig);
    }
}
