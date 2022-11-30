package com.zondy.mapgis.common.accesslog.service;

import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import com.zondy.mapgis.system.api.service.ISysHttpAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 记录Http访问日志方法
 *
 * @author xiongbo
 * @since 2022/11/29 15:17
 */
@Service
public class SysHttpAccessLogService implements ISysHttpAccessLogService {
    @Autowired
    private ISysHttpAccessService sysHttpAccessService;

    @Override
    public void recordHttpAccess(SysHttpAccess httpAccess) {
        sysHttpAccessService.insertSysHttpAccess(httpAccess);
    }
}
