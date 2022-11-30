package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.api.domain.SysHttpAccess;

import java.util.List;

/**
 * Http访问日志Mapper接口
 *
 * @author xiongbo
 * @date 2022-11-28
 */
public interface SysHttpAccessMapper {
    /**
     * 查询Http访问日志
     *
     * @param accessId Http访问日志主键
     * @return Http访问日志
     */
    public SysHttpAccess selectSysHttpAccessByAccessId(Long accessId);

    /**
     * 查询Http访问日志列表
     *
     * @param sysHttpAccess Http访问日志
     * @return Http访问日志集合
     */
    public List<SysHttpAccess> selectSysHttpAccessList(SysHttpAccess sysHttpAccess);

    /**
     * 新增Http访问日志
     *
     * @param sysHttpAccess Http访问日志
     * @return 结果
     */
    public int insertSysHttpAccess(SysHttpAccess sysHttpAccess);

    /**
     * 删除Http访问日志
     *
     * @param accessId Http访问日志主键
     * @return 结果
     */
    public int deleteSysHttpAccessByAccessId(Long accessId);

    /**
     * 批量删除Http访问日志
     *
     * @param accessIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysHttpAccessByAccessIds(Long[] accessIds);

    /**
     * 清空Http访问日志
     */
    public void cleanHttpAccess();

    /**
     * 清理过期的数据（90天之前）
     */
    public void clearExpired();
}
