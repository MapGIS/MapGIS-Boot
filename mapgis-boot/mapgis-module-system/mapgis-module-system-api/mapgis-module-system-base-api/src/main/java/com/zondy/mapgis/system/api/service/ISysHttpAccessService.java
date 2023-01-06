package com.zondy.mapgis.system.api.service;

import com.zondy.mapgis.system.api.domain.SysHttpAccess;

import java.util.List;

/**
 * Http访问日志Service接口
 *
 * @author xiongbo
 * @date 2022-11-28
 */
public interface ISysHttpAccessService {
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
     * 批量删除Http访问日志
     *
     * @param accessIds 需要删除的Http访问日志主键集合
     * @return 结果
     */
    public int deleteSysHttpAccessByAccessIds(Long[] accessIds);

    /**
     * 删除Http访问日志信息
     *
     * @param accessId Http访问日志主键
     * @return 结果
     */
    public int deleteSysHttpAccessByAccessId(Long accessId);

    /**
     * 清空Http访问日志
     */
    public void cleanHttpAccess();
}
