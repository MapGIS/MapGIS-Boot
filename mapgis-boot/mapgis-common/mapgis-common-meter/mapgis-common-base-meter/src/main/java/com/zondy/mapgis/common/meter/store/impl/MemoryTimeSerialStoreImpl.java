package com.zondy.mapgis.common.meter.store.impl;

import com.zondy.mapgis.common.meter.store.ITimeSerialData;
import com.zondy.mapgis.common.meter.store.ITimeSerialStore;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 内存时间序列数据存储
 *
 * @author mapgis
 * @since 2022/12/5 11:11
 */
public class MemoryTimeSerialStoreImpl<T extends ITimeSerialData> implements ITimeSerialStore<T> {
    private final ConcurrentLinkedQueue<ITimeSerialData> data = new ConcurrentLinkedQueue<>();

    /**
     * 统计频率3s，统计最近10分钟，如果太多，前端显示堆在一起看不清，最多200条记录
     */
    private final static int MAX_COUNT = 200;

    @Override
    public void put(@NonNull T t) {
        data.add(t);
    }

    @Override
    public List<T> get(boolean includeAll) {
        while (data.size() > MAX_COUNT) {
            data.poll();
        }
        List<T> res = Arrays.stream(data.toArray()).map(t -> (T) t).collect(Collectors.toList());
        if (includeAll) {
            return res;
        } else {
            if (res.size() > 0) {
                return res.subList(res.size() - 1, res.size() - 1);
            }
            return null;
        }
    }
}
