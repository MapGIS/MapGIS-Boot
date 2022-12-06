package com.zondy.mapgis.common.meter.model;

import lombok.Data;

/**
 * 性能统计信息
 *
 * @author mapgis
 * @since 2022/12/6 9:18
 */
@Data
public class PerformanceStatistic {
    /**
     * 统计名称
     */
    private String name;
    /**
     * 总请求数
     */
    private long totalCount;
    /**
     * 成功请求数
     */
    private long successCount;
    /**
     * 实时并发数
     */
    private int realTimeConcurrency;
    /**
     * 平均响应时间
     */
    private double meanResponseTime;
    /**
     * 直方图百分比数据
     */
    private double[] histogramPercentiles;
}
