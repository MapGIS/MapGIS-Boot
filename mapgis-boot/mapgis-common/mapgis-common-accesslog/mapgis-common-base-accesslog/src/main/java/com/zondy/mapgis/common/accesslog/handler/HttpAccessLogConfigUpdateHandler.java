package com.zondy.mapgis.common.accesslog.handler;

import com.zondy.mapgis.common.accesslog.config.HttpAccessConfig;
import com.zondy.mapgis.common.accesslog.recorder.AsyncHttpAccessRecorder;
import com.zondy.mapgis.system.api.domain.SysLogConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HTTP访问日志配置更新处理
 *
 * @author xiongbo
 * @since 2022/11/30 11:24
 */
@Component
public class HttpAccessLogConfigUpdateHandler {
    @Autowired
    private AsyncHttpAccessRecorder asyncHttpAccessRecorder;

    @Autowired
    private HttpAccessConfig httpAccessConfig;

    public void updateLogConfig(SysLogConfig postLogConfig) {
        SysLogConfig preLogConfig = httpAccessConfig.getLogConfig();

        // 更新配置
        boolean preEnabled = preLogConfig.getHttpAccessEnabled();
        boolean postEnabled = postLogConfig.getHttpAccessEnabled();

        preLogConfig.setHttpAccessEnabled(postLogConfig.getHttpAccessEnabled());
        preLogConfig.setHttpAccessMonitorUrls(postLogConfig.getHttpAccessMonitorUrls());

        if (preEnabled != postEnabled) {
            if (postEnabled) {
                asyncHttpAccessRecorder.stop();
            } else {
                asyncHttpAccessRecorder.restart();
            }
        }
    }
}
