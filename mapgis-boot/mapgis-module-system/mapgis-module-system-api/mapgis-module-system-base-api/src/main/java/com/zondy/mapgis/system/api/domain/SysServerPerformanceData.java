package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 服务器性能数据
 *
 * @author xiongbo
 * @since 2022/12/8 9:56
 */
@Data
public class SysServerPerformanceData {
    /**
     * 客户端IP
     */
    private String ipaddr;

    /**
     * 远程主机IP
     */
    private String remoteAddr;

    /**
     * 请求URL
     */
    private String url;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 查询字符串
     */
    private String queryString;

    /**
     * 返回状态码
     */
    private Long responseStatus;

    /**
     * 开始时间
     */
    private Long accessTimeMillis;

    /**
     * 耗时
     */
    private Long time;
}
