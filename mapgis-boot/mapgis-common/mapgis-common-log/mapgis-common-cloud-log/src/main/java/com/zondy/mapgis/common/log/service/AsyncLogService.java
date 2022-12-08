package com.zondy.mapgis.common.log.service;

import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class AsyncLogService {
    @Lazy
    @Autowired
    private ISysServiceApi sysServiceApi;

    /**
     * 保存系统日志记录
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) {
        sysServiceApi.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
