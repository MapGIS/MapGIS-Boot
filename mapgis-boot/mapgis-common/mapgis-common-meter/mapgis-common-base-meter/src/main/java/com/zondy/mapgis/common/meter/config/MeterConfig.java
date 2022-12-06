package com.zondy.mapgis.common.meter.config;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.meter.constants.MetricConstants;
import com.zondy.mapgis.common.meter.context.MetricContext;
import com.zondy.mapgis.common.meter.store.ITimeSerialStore;
import com.zondy.mapgis.common.meter.store.impl.MemoryTimeSerialStoreImpl;
import com.zondy.mapgis.common.meter.store.impl.ServerTimeSerialDataImpl;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 指标配置对象
 *
 * @author xiongbo
 * @since 2022/12/5 9:38
 */
@Configuration
public class MeterConfig {
    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public MetricContext metricContext(MeterRegistry meterRegistry, ApiPathProperties apiPathProperties) {
        return new MetricContext(meterRegistry, apiPathProperties);
    }

    @Bean
    public ITimeSerialStore<ServerTimeSerialDataImpl> serverTimeSerialDataStore() {
        return new MemoryTimeSerialStoreImpl<>();
    }

    @Bean
    public MeterFilter meterFilter() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                // 服务性能监控配置
                if (id.getType() == Meter.Type.TIMER && id.getName().equals(MetricConstants.METRIC_SERVER_RESPONSE_TIME) && (id.getTag(MetricConstants.METRIC_REMOTE_ADDR_TAG) != null || id.getTag(MetricConstants.METRIC_SERVICE_TAG) != null)) {
                    // 注意时间单位均为纳秒
                    // percentiles 直方图百分比刻度
                    // 采样最大最小值
                    // expiry 直方图的样本的保留时间
                    // sla 为指定样本取值的个数，这里指响应时间对应的样本个数
                    // percentilePrecision 百分比精度
                    return DistributionStatisticConfig.builder().percentilesHistogram(true).percentiles(0.5, 0.75, 0.90).minimumExpectedValue((double) Duration.ofMillis(1).toNanos()).maximumExpectedValue((double) Duration.ofMinutes(1).toNanos()).expiry(Duration.ofMinutes(5)).serviceLevelObjectives(Duration.ofSeconds(1).toNanos(), Duration.ofSeconds(5).toNanos(), Duration.ofSeconds(15).toNanos()).percentilePrecision(1).build().merge(config);
                }
                return config;
            }
        };
    }
}
