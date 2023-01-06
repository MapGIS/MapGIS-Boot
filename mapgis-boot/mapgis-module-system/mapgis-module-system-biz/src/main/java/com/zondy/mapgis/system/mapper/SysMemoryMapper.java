package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysMemory;

import java.util.Date;
import java.util.List;

/**
 * 内存信息Mapper接口
 *
 * @author mapgis
 * @date 2023-01-03
 */
public interface SysMemoryMapper {
    /**
     * 查询到当前时间周期内最近的内存信息（>time)
     *
     * @param time 最近时间
     * @return 内存信息
     */
    public SysMemory selectRecentSysMemoryWithinTime(Date time);

    /**
     * 查询内存信息列表
     *
     * @param sysMemory 内存信息
     * @return 内存信息集合
     */
    public List<SysMemory> selectSysMemoryList(SysMemory sysMemory);

    /**
     * 新增内存信息
     *
     * @param sysMemory 内存信息
     * @return 结果
     */
    public int insertSysMemory(SysMemory sysMemory);

    /**
     * 清理过期的数据（90天之前）
     */
    public void clearExpired();
}
