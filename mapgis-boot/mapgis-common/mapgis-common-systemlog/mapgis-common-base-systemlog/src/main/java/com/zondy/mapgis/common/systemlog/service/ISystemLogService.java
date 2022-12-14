package com.zondy.mapgis.common.systemlog.service;

import com.zondy.mapgis.common.systemlog.model.SysLoginfoList;

import javax.servlet.http.HttpServletResponse;

/**
 * 系统日志服务
 *
 * @author xiongbo
 * @since 2022/11/22 18:21
 */
public interface ISystemLogService {
    /**
     * 获取日志
     *
     * @param logId     文件标识
     * @param position  文件索引
     * @param count
     * @param level
     * @param beginTime
     * @param endTime
     * @param keyword
     * @return
     */
    SysLoginfoList getLogs(String logId, String position, int count, String level, String beginTime, String endTime, String keyword);

    /**
     * 导出日志
     *
     * @param response 响应对象
     * @param logId    文件标识
     */
    void exportLogFilesToZip(HttpServletResponse response, String logId);
}
