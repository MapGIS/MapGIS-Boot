package com.zondy.mapgis.common.log.service;

import com.zondy.mapgis.common.security.manager.AsyncManager;
import com.zondy.mapgis.common.security.manager.factory.AsyncFactory;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author xiongbo
 * @since 2022/11/8 9:59
 */
@Service
public class AsyncLogService {
    /**
     * 保存系统日志记录
     */
    public void saveSysLog(SysOperLog sysOperLog) {
        AsyncManager.me().execute(AsyncFactory.recordOper(sysOperLog));
    }
}
