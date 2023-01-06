package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysDisk;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * 磁盘信息Mapper接口
 *
 * @author mapgis
 * @date 2023-01-03
 */
public interface SysDiskMapper {
    /**
     * 查询到当前时间周期内最近的磁盘信息（>time)
     *
     * @param time 最近时间
     * @return 磁盘信息
     */
    public SysDisk selectRecentSysDiskWithinTime(@Param("readwtire") Integer readwrite, @Param("time") Date time);

    /**
     * 查询指定时间周期内最近的磁盘信息（>beginTime,<=endTime)
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 磁盘信息
     */
    public SysDisk selectRecentSysDiskBetweenTime(@Param("readwtire") Integer readwrite, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 查询磁盘信息列表
     *
     * @param sysDisk 磁盘信息
     * @return 磁盘信息集合
     */
    public List<SysDisk> selectSysDiskList(SysDisk sysDisk);

    /**
     * 新增磁盘信息
     *
     * @param sysDisk 磁盘信息
     * @return 结果
     */
    public int insertSysDisk(SysDisk sysDisk);

    /**
     * 清理过期的数据（90天之前）
     */
    public void clearExpired();
}
