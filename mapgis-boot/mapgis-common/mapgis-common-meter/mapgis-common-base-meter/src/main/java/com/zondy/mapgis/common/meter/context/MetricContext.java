package com.zondy.mapgis.common.meter.context;

import com.google.common.collect.Lists;
import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.meter.constants.MetricConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 监控上下文
 * <p>
 * 注意所有监控指标最多使用1个tag，由于find接口限制，不能精确查找，如果出现两个同名metric又同时包括公共的tag，会导致查询问题
 * 总响应     timer   MetricConstants.METRIC_SERVER_RESPONSE_TIME[MetricConstants.METRIC_STATISTIC_TYPE=MetricConstants.METRIC_STATISTIC_ALL]
 * 总请求数  counter  MetricConstants.METRIC_SERVER_HTTP_COUNT[MetricConstants.METRIC_STATISTIC_TYPE=MetricConstants.METRIC_STATISTIC_ALL]
 * 某天请求数 counter MetricConstants.METRIC_SERVER_HTTP_COUNT[MetricConstants.METRIC_SERVER_STATISTIC_DAY=day]
 * 总并发数    gauge  MetricConstants.METRIC_SERVER_CONCURRENCY[MetricConstants.METRIC_STATISTIC_TYPE=MetricConstants.METRIC_STATISTIC_ALL]
 * ip并发数   gauge   MetricConstants.METRIC_SERVER_CONCURRENCY[MetricConstants.METRIC_REMOTE_ADDR_TAG=remoteAddr]
 * 服务并发数  gauge   MetricConstants.METRIC_SERVER_CONCURRENCY[MetricConstants.METRIC_SERVICE_TAG=serviceName]
 * ip响应    timer   MetricConstants.METRIC_SERVER_RESPONSE_TIME[MetricConstants.METRIC_REMOTE_ADDR_TAG=remoteAddr]
 * 服务响应   timer   MetricConstants.METRIC_SERVER_RESPONSE_TIME[MetricConstants.METRIC_SERVICE_TAG=serviceName]
 * ip失败请求数 counter MetricConstants.METRIC_SERVER_HTTP_FAIL_COUNT[MetricConstants.METRIC_REMOTE_ADDR_TAG=remoteAddr]
 * 服务失败请求数 counter MetricConstants.METRIC_SERVER_HTTP_FAIL_COUNT[MetricConstants.METRIC_SERVICE_TAG=serviceName]
 *
 * @author mapgis
 * @since 2022/12/2 16:26
 */
@Slf4j
public class MetricContext {
    /**
     * 某个时刻的值
     */
    @ToString
    private static class CountWithTime {
        /**
         * 时间戳，ms
         */
        private long time;
        private long count;
    }

    /**
     * 默认标签
     */
    public final static List<Tag> DEFAULT_TAG = Lists.newArrayList(Tag.of(MetricConstants.METRIC_STATISTIC_TYPE, MetricConstants.METRIC_STATISTIC_ALL));

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    /**
     * 上一次统计总请求数集合（用于计算单位时间内的并发数）
     */
    private final ConcurrentHashMap<String, CountWithTime> requestConcurrencyMap = new ConcurrentHashMap<>();

    private final AtomicInteger allServiceInteger = new AtomicInteger(0);
    private final ConcurrentHashMap<String, AtomicInteger> remoteAddrIntegers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> serviceNameIntegers = new ConcurrentHashMap<>();

    private final Timer allServiceTimer;
    private final Counter allServiceCount;

    private MeterRegistry meterRegistry;
    private List<String> excludeUrls = new ArrayList<>();

    public MetricContext(MeterRegistry meterRegistry, ApiPathProperties apiPathProperties) {
        String strManagerPrefix = apiPathProperties.getManagerPrefix();

        excludeUrls.add(strManagerPrefix + "/system/monitor/**");

        this.meterRegistry = meterRegistry;
        // 所有请求，统计平均响应时间
        allServiceTimer = meterRegistry.timer(MetricConstants.METRIC_SERVER_RESPONSE_TIME, DEFAULT_TAG);
        // 总请求数
        allServiceCount = meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_COUNT, DEFAULT_TAG);
    }

    private CountWithTime getOrCreateRequestConcurrencyGauge(String name) {
        CountWithTime data = requestConcurrencyMap.get(name);
        if (data == null) {
            requestConcurrencyMap.putIfAbsent(name, new CountWithTime());
            return requestConcurrencyMap.get(name);
        } else {
            return data;
        }
    }

    private int getConcurrency(CountWithTime last, double currentTotalCount) {
        int concurrency = 0;
        long now = System.currentTimeMillis();
        if (last.time > 0) {
            // 每秒请求次数
            concurrency = (int) (Math.ceil((currentTotalCount - last.count) * 1000d / (now - last.time)));
            concurrency = Math.max(0, concurrency);
        }
        last.time = now;
        last.count = (long) currentTotalCount;
        return concurrency;
    }

    /**
     * 定时执行快照，为了统计并发数（通过前后两次总请求数除以时间间隔计算）
     */
    public void takeSnapShootForConcurrency() {
        {
            CountWithTime last = getDefaultRequestConcurrencyGauge();
            int concurrency = getConcurrency(last, allServiceCount.count());
            meterRegistry.gauge(MetricConstants.METRIC_SERVER_CONCURRENCY, DEFAULT_TAG, allServiceInteger);
            allServiceInteger.set(concurrency);
        }

        // 遍历所有remoteAddr的总请求数计数器
        meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tagKeys(MetricConstants.METRIC_REMOTE_ADDR_TAG).counters().forEach(c -> {
            if (c.getId().getTags().size() == 1) {
                String remoteAddr = c.getId().getTag(MetricConstants.METRIC_REMOTE_ADDR_TAG);
                if (StringUtils.hasText(remoteAddr)) {
                    CountWithTime last = getRequestConcurrencyGaugeByRemoteAddr(remoteAddr);
                    int concurrency = getConcurrency(last, c.count());
                    AtomicInteger remoteAddrInteger = remoteAddrIntegers.get(remoteAddr);
                    if (remoteAddrInteger == null) {
                        remoteAddrIntegers.putIfAbsent(remoteAddr, new AtomicInteger(0));
                        remoteAddrInteger = remoteAddrIntegers.get(remoteAddr);
                        meterRegistry.gauge(MetricConstants.METRIC_SERVER_CONCURRENCY, Lists.newArrayList(Tag.of(MetricConstants.METRIC_REMOTE_ADDR_TAG, remoteAddr)), remoteAddrInteger);
                    }
                    remoteAddrInteger.set(concurrency);
                }
            }

        });
        // 遍历所有ServiceName的总请求数计数器
        meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tagKeys(MetricConstants.METRIC_SERVICE_TAG).counters().forEach(c -> {
            if (c.getId().getTags().size() == 1) {
                String serviceName = c.getId().getTag(MetricConstants.METRIC_SERVICE_TAG);
                if (StringUtils.hasText(serviceName)) {
                    CountWithTime last = getRequestConcurrencyGaugeByServiceName(serviceName);
                    int concurrency = getConcurrency(last, c.count());
                    AtomicInteger serviceNameInteger = serviceNameIntegers.get(serviceName);
                    if (serviceNameInteger == null) {
                        serviceNameIntegers.putIfAbsent(serviceName, new AtomicInteger(0));
                        serviceNameInteger = serviceNameIntegers.get(serviceName);
                        meterRegistry.gauge(MetricConstants.METRIC_SERVER_CONCURRENCY, Lists.newArrayList(Tag.of(MetricConstants.METRIC_SERVICE_TAG, serviceName)), serviceNameInteger);
                    }
                    serviceNameInteger.set(concurrency);
                }
            }
        });
    }

    /**
     * 记录所有请求的并发数的计数器
     *
     * @return 并发数的计数器
     */
    private CountWithTime getDefaultRequestConcurrencyGauge() {
        return getOrCreateRequestConcurrencyGauge("all");
    }

    /**
     * 记录指定RemoteAddr并发数的计数器
     *
     * @return 并发数的计数器
     */
    private CountWithTime getRequestConcurrencyGaugeByRemoteAddr(String remoteAddr) {
        return getOrCreateRequestConcurrencyGauge("remoteAddr_" + remoteAddr);
    }

    /**
     * 记录指定ServiceName并发数的计数器
     *
     * @return 并发数的计数器
     */
    private CountWithTime getRequestConcurrencyGaugeByServiceName(String serviceName) {
        return getOrCreateRequestConcurrencyGauge("serviceName_" + serviceName);
    }

    public void record(long startTime, String uri, String remoteAddr, String queryString, String method, String ip, int status) {
        if (excludeUrls.stream().anyMatch(t -> antPathMatcher.match(t, uri))) {
            return;
        }

        try {
            long elapseMillis = System.currentTimeMillis() - startTime;
            String serviceName = "";
            // 这里需要处理一下，在测试影像时，getTile，发现uri的情况太多导致内存占满，一直GC，这里最长取5段
            // serviceName = uri;
            if (StringUtils.hasText(uri)) {
                serviceName = Arrays.stream(uri.split("/")).limit(6).collect(Collectors.joining("/"));
            }
            // 慢请求监控，时间超过3s，记录请求信息到log，用于跟踪性能问题
            if (elapseMillis > 3000) {
                String fullUri = uri + (queryString != null ? ("?" + queryString) : "");
                log.warn("慢请求,URL:{},Method:{},Remote:{},耗时:{}ms", fullUri, method, ip, elapseMillis);
            }
            // 所有请求，统计平均响应时间
            allServiceTimer.record(elapseMillis, TimeUnit.MILLISECONDS);
            // 总请求数
            allServiceCount.increment();
            // 统计某日的请求总数
            long day = LocalDate.now().toEpochDay();
            meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_COUNT,
                    Lists.newArrayList(Tag.of(MetricConstants.METRIC_SERVER_STATISTIC_DAY, String.valueOf(day)))).increment();

            // 指定IP的总请求数
            if (StringUtils.hasText(remoteAddr)) {
                meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_COUNT,
                        Lists.newArrayList(Tag.of(MetricConstants.METRIC_REMOTE_ADDR_TAG, remoteAddr))).increment();
            }
            // 指定服务的总请求数
            if (StringUtils.hasText(serviceName)) {
                meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_COUNT,
                        Lists.newArrayList(Tag.of(MetricConstants.METRIC_SERVICE_TAG, serviceName))).increment();
            }

            boolean success = status < 400;
            if (success) {
                // 指定IP的访问
                if (StringUtils.hasText(remoteAddr)) {
                    meterRegistry.timer(MetricConstants.METRIC_SERVER_RESPONSE_TIME,
                            Lists.newArrayList(Tag.of(MetricConstants.METRIC_REMOTE_ADDR_TAG, remoteAddr))).record(elapseMillis, TimeUnit.MILLISECONDS);
                }
                // 指定服务的访问
                if (StringUtils.hasText(serviceName)) {
                    meterRegistry.timer(MetricConstants.METRIC_SERVER_RESPONSE_TIME,
                            Lists.newArrayList(Tag.of(MetricConstants.METRIC_SERVICE_TAG, serviceName))).record(elapseMillis, TimeUnit.MILLISECONDS);
                }
            } else {
                // 指定IP的访问，失败次数
                if (StringUtils.hasText(remoteAddr)) {
                    meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_FAIL_COUNT,
                            Lists.newArrayList(Tag.of(MetricConstants.METRIC_REMOTE_ADDR_TAG, remoteAddr))).increment();
                }
                // 指定服务的访问，失败次数
                if (StringUtils.hasText(serviceName)) {
                    meterRegistry.counter(MetricConstants.METRIC_SERVER_HTTP_FAIL_COUNT,
                            Lists.newArrayList(Tag.of(MetricConstants.METRIC_SERVICE_TAG, serviceName))).increment();
                }
            }
        } catch (Exception e) {
            log.error("监控服务性能结束时出现异常", e);
        }
    }
}
