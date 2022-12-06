package com.zondy.mapgis.common.meter.store;

import java.util.List;

/**
 * 时间序列存储
 *
 * @author mapgis
 * @since 2022/12/5 11:10
 */
public interface ITimeSerialStore<T extends ITimeSerialData> {
    void put(T t);

    List<T> get(boolean includeAll);
}
