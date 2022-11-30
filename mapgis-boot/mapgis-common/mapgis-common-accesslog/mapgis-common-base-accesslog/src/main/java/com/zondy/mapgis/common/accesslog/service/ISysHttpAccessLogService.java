package com.zondy.mapgis.common.accesslog.service;

import com.zondy.mapgis.system.api.domain.SysHttpAccess;

/**
 * 记录Http访问日志方法
 *
 * @author xiongbo
 * @since 2022/11/29 14:31
 */
public interface ISysHttpAccessLogService {

    /**
     * 记录Http访问日志
     *
     * @param httpAccess Http访问对象
     */
    public void recordHttpAccess(SysHttpAccess httpAccess);
}
