package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysCpu;

import java.util.Date;
import java.util.List;

/**
 * CPU信息Mapper接口
 *
 * @author mapgis
 * @date 2023-01-03
 */
public interface SysCpuMapper {
    /**
     * 查询到当前时间周期内最近的CPU信息（>time)
     *
     * @param time 最近时间
     * @return CPU信息
     */
    public SysCpu selectRecentSysCpuWithinTime(Date time);

    /**
     * 查询CPU信息列表
     *
     * @param sysCpu CPU信息
     * @return CPU信息集合
     */
    public List<SysCpu> selectSysCpuList(SysCpu sysCpu);

    /**
     * 新增CPU信息
     *
     * @param sysCpu CPU信息
     * @return 结果
     */
    public int insertSysCpu(SysCpu sysCpu);

    /**
     * 清理过期的数据（90天之前）
     */
    public void clearExpired();
}
