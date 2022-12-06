package com.zondy.mapgis.common.meter.constants;

/**
 * metric相关常量定义
 *
 * @author mapgis
 * @since 2022/12/2 16:57
 */
public class MetricConstants {
    /**
     * 服务器的请求数
     */
    public final static String METRIC_SERVER_HTTP_COUNT = "server.http.count";
    /**
     * 服务器的请求失败次数
     */
    public final static String METRIC_SERVER_HTTP_FAIL_COUNT = "server.http.failCount";
    /**
     * 监控范围
     */
    public final static String METRIC_STATISTIC_TYPE = "type";
    /**
     * 默认监控所有
     */
    public final static String METRIC_STATISTIC_ALL = "all";
    /**
     * 统计每天的请求总数
     */
    public final static String METRIC_SERVER_STATISTIC_DAY = "day";
    /**
     * 服务器的实时并发数Metric名称
     */
    public final static String METRIC_SERVER_CONCURRENCY = "server.http.concurrency";
    /**
     * 服务器的响应时间Metric名称
     */
    public final static String METRIC_SERVER_RESPONSE_TIME = "server.http.responseTime";
    /**
     * 远程用户的性能Metric名称
     */
    public final static String METRIC_REMOTE_ADDR_TAG = "remoteAddr";
    /**
     * 指定服务的性能Metric名称
     */
    public final static String METRIC_SERVICE_TAG = "serviceName";
}
