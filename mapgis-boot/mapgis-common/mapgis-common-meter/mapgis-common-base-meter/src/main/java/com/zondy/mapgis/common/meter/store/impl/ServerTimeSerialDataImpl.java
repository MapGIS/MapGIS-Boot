package com.zondy.mapgis.common.meter.store.impl;

import com.zondy.mapgis.common.meter.store.ITimeSerialData;
import lombok.Data;

/**
 * 服务器时间序列数据
 *
 * @author mapgis
 * @since 2022/12/5 11:09
 */
@Data
public class ServerTimeSerialDataImpl implements ITimeSerialData {
    /**
     * 当前时刻
     */
    private long time;

    /**
     * 实时并发数,通过Gause
     */
    private int concurrency;

    /**
     * 实时响应时间，通过Timer ？？？好像无法统计实时的，只能统计平均的
     */
    private int responseTime;
}
