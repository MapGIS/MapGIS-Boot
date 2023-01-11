package com.zondy.mapgis.common.resttemplate.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HttpClient属性配置
 *
 * @author Chelsea
 * @since 2023/1/11 12:00
 */
@ConfigurationProperties("httpclient")
@Data
public class HttpClientProperties {
    /**
     * 数据传输过程中数据包之间间隔的最大时间
     */
    private int socketTimeout = -1;

    /**
     * 与服务器建立连接超时
     */
    private int connectTimeout = 5000;

    /**
     * 从池中获取连接的超时
     */
    private int requestTimeout = 3000;

    /**
     * 连接池最大连接数
     */
    private int maxConnectCount = 10000;

    /**
     * 每个主机的并发最多只有1000
     */
    private int maxPerRoutCount = 1000;

    /**
     * 连接空闲的时长(分钟),超时则清理
     */
    private int idleTimeDuration = 3;
}
