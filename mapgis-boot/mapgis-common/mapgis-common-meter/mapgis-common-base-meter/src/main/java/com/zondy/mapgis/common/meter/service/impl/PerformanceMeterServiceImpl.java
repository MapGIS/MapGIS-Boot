package com.zondy.mapgis.common.meter.service.impl;

import com.zondy.mapgis.common.meter.constants.MetricConstants;
import com.zondy.mapgis.common.meter.model.PerformanceStatistic;
import com.zondy.mapgis.common.meter.service.IPerformanceMeterService;
import com.zondy.mapgis.common.meter.store.ITimeSerialStore;
import com.zondy.mapgis.common.meter.store.impl.ServerTimeSerialDataImpl;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 性能指标服务
 *
 * @author mapgis
 * @since 2022/12/6 9:14
 */
@Service
public class PerformanceMeterServiceImpl implements IPerformanceMeterService {
    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    @Qualifier("serverTimeSerialDataStore")
    private ITimeSerialStore<ServerTimeSerialDataImpl> serverTimeSerialDataStore;

    @Override
    public long getTotalRequestCount() {
        // 必须带标签，否则无法精确匹配
        Counter totalRequestCounter = meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tag(MetricConstants.METRIC_STATISTIC_TYPE, MetricConstants.METRIC_STATISTIC_ALL).counter();
        return totalRequestCounter != null ? (long) totalRequestCounter.count() : 0L;
    }

    @Override
    public long getTodayRequestCount() {
        long day = LocalDate.now().toEpochDay();
        Counter todayRequestCounter = meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tag(MetricConstants.METRIC_SERVER_STATISTIC_DAY, String.valueOf(day)).counter();
        return todayRequestCounter != null ? (long) todayRequestCounter.count() : 0L;
    }

    @Override
    public double getServerMeanResponseTime() {
        Timer timer = meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tag(MetricConstants.METRIC_STATISTIC_TYPE, MetricConstants.METRIC_STATISTIC_ALL).timer();
        return timer != null ? timer.mean(TimeUnit.MILLISECONDS) : 0d;
    }

    private List<PerformanceStatistic> getRequestStatisticList(String tagName) {
        List<PerformanceStatistic> statisticList = new ArrayList<>();
        Collection<Gauge> concurrencyGauges = meterRegistry.find(MetricConstants.METRIC_SERVER_CONCURRENCY).tagKeys(tagName).gauges();
        for (Gauge gauge : concurrencyGauges) {
            String tagValue = gauge.getId().getTag(tagName);
            if (!StringUtils.hasText(tagValue)) {
                continue;
            }
            PerformanceStatistic statistic = new PerformanceStatistic();
            statistic.setName(tagValue);
            statistic.setRealTimeConcurrency((int) gauge.value());
            Counter totalCounter = meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tags(tagName, tagValue).counter();
            statistic.setTotalCount(totalCounter != null ? (long) totalCounter.count() : 0);
            Counter failCounter = meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_FAIL_COUNT).tags(tagName, tagValue).counter();
            statistic.setSuccessCount(statistic.getTotalCount() - (failCounter != null ? ((long) failCounter.count()) : 0));
            Timer timer = meterRegistry.find(MetricConstants.METRIC_SERVER_RESPONSE_TIME).tags(tagName, tagValue).timer();
            if (timer != null) {
                statistic.setMeanResponseTime(timer.mean(TimeUnit.MILLISECONDS));
                double[] percentiles = Arrays.stream(timer.takeSnapshot().percentileValues()).mapToDouble(t -> t.value(TimeUnit.MILLISECONDS)).toArray();
                statistic.setHistogramPercentiles(percentiles);
            } else {
                statistic.setMeanResponseTime(0);
                statistic.setHistogramPercentiles(new double[]{});
            }
            statisticList.add(statistic);
        }
        return statisticList;
    }

    @Override
    public List<PerformanceStatistic> getRemoteAddrRequestStatisticList() {
        return getRequestStatisticList(MetricConstants.METRIC_REMOTE_ADDR_TAG);
    }

    @Override
    public List<PerformanceStatistic> getServiceNameRequestStatisticList() {
        return getRequestStatisticList(MetricConstants.METRIC_SERVICE_TAG);
    }

    @Override
    public Map<String, Object> getPerformanceInfo() {
        Map<String, Object> resultMap = new LinkedHashMap<>(3);
        List<ServerTimeSerialDataImpl> tsData = serverTimeSerialDataStore.get(true);

        resultMap.put("requestSerials", tsData);
        resultMap.put("totalRequestCount", getTotalRequestCount());
        resultMap.put("todayRequestCount", getTodayRequestCount());

        return resultMap;
    }
}
