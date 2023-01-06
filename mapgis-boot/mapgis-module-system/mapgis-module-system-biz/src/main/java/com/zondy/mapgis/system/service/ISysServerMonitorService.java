package com.zondy.mapgis.system.service;

import java.util.Map;

/**
 * 硬件监控Service接口
 *
 * @author mapgis
 * @date 2023-01-03
 */
public interface ISysServerMonitorService {

    /**
     * 获取实时的监控信息
     *
     * @return
     */
    Map<String, Object> getMonitorInfo();

    /**
     * 获取指定时间周期内的监控信息
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 监控信息
     */
    Map<String, Object> getMonitorInfoBetweenTime(String beginTime, String endTime);
}
