package com.zondy.mapgis.common.meter.service;

import com.zondy.mapgis.common.meter.model.PerformanceStatistic;

import java.util.List;
import java.util.Map;

/**
 * 性能指标服务接口
 *
 * @author mapgis
 * @since 2022/12/5 18:43
 */
public interface IPerformanceMeterService {
    /**
     * 获取累计总访问量
     *
     * @return 累计总访问量
     */
    long getTotalRequestCount();

    /**
     * 获取今日总访问量
     *
     * @return 今日总访问量
     */
    long getTodayRequestCount();

    /**
     * 获取平均响应时间
     *
     * @return 平均响应时间 ms
     */
    double getServerMeanResponseTime();

    /**
     * 获取统计结果，Tag为MetricConstants.METRIC_REMOTE_ADDR_TAG
     *
     * @return
     */
    List<PerformanceStatistic> getRemoteAddrRequestStatisticList();

    /**
     * 获取统计结果，Tag为MetricConstants.METRIC_SERVICE_TAG
     *
     * @return
     */
    List<PerformanceStatistic> getServiceNameRequestStatisticList();

    /**
     * 获取性能信息
     *
     * @return 性能信息
     */
    Map<String, Object> getPerformanceInfo();
}
