package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysNetwork;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 网络信息Mapper接口
 *
 * @author mapgis
 * @date 2023-01-03
 */
public interface SysNetworkMapper {
    /**
     * 查询到当前时间周期内最近的网络信息（>time)
     *
     * @param time 最近时间
     * @return 网络信息
     */
    public SysNetwork selectRecentSysNetworkWithinTime(@Param("updown") Integer updown, @Param("time") Date time);

    /**
     * 查询指定时间周期内最近的网络信息（>beginTime,<=endTime)
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 网络信息
     */
    public SysNetwork selectRecentSysNetworkBetweenTime(@Param("updown") Integer updown, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 查询网络信息列表
     *
     * @param sysNetwork 网络信息
     * @return 网络信息集合
     */
    public List<SysNetwork> selectSysNetworkList(SysNetwork sysNetwork);

    /**
     * 新增网络信息
     *
     * @param sysNetwork 网络信息
     * @return 结果
     */
    public int insertSysNetwork(SysNetwork sysNetwork);

    /**
     * 清理过期的数据（90天之前）
     */
    public void clearExpired();
}
