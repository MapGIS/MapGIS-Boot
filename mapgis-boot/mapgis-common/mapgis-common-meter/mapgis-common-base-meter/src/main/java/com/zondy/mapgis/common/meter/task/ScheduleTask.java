package com.zondy.mapgis.common.meter.task;

import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.meter.constants.MetricConstants;
import com.zondy.mapgis.common.meter.context.MetricContext;
import com.zondy.mapgis.common.meter.store.ITimeSerialStore;
import com.zondy.mapgis.common.meter.store.impl.ServerTimeSerialDataImpl;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 *
 * @author xiongbo
 * @since 2022/12/5 10:59
 */
@Component
@Slf4j
public class ScheduleTask {
    @Autowired
    @Qualifier("serverTimeSerialDataStore")
    private ITimeSerialStore<ServerTimeSerialDataImpl> serverTimeSerialDataStore;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private MetricContext metricContext;

    /**
     * 记录时序监控数据，每3s记录一次
     */
    @Scheduled(initialDelay = 5 * DateUtils.MILLIS_PER_SECOND, fixedDelay = 3 * DateUtils.MILLIS_PER_SECOND)
    public void dealServerPerformanceMonitor() {
        ServerTimeSerialDataImpl data = new ServerTimeSerialDataImpl();
        data.setTime(System.currentTimeMillis());
        // 为了统计并发数
        metricContext.takeSnapShootForConcurrency();
        Gauge gauge = meterRegistry.find(MetricConstants.METRIC_SERVER_CONCURRENCY).tags(MetricContext.DEFAULT_TAG).gauge();
        if (gauge != null) {
            data.setConcurrency((int) gauge.value());
        }
        Timer timer = meterRegistry.find(MetricConstants.METRIC_SERVER_RESPONSE_TIME).tags(MetricContext.DEFAULT_TAG).timer();
        if (timer != null) {
            data.setResponseTime((int) timer.mean(TimeUnit.MILLISECONDS));
        }
        serverTimeSerialDataStore.put(data);
    }

    @Scheduled(initialDelay = 5 * DateUtils.MILLIS_PER_SECOND, fixedDelay = DateUtils.MILLIS_PER_DAY)
    public void clearGarbage() {
        // 清理10天以前的每天请求计数器counter
        long day = LocalDate.now().toEpochDay();
        meterRegistry.find(MetricConstants.METRIC_SERVER_HTTP_COUNT).tagKeys(MetricConstants.METRIC_SERVER_STATISTIC_DAY).counters().forEach(t -> {
            String tag = t.getId().getTag(MetricConstants.METRIC_SERVER_STATISTIC_DAY);
            if (StringUtils.hasText(tag)) {
                long d = Long.parseLong(tag);
                if (d < day - 10) {
                    meterRegistry.remove(t);
                }
            }
        });
    }
}
