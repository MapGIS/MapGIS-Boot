package com.zondy.mapgis.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import com.zondy.mapgis.system.api.service.ISysHttpAccessService;
import com.zondy.mapgis.system.mapper.SysHttpAccessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Http访问日志Service业务层处理
 *
 * @author xiongbo
 * @date 2022-11-28
 */
@Service
@DS("access_log")
public class SysHttpAccessServiceImpl implements ISysHttpAccessService {
    @Autowired
    private SysHttpAccessMapper sysHttpAccessMapper;

    /**
     * 查询Http访问日志
     *
     * @param accessId Http访问日志主键
     * @return Http访问日志
     */
    @Override
    public SysHttpAccess selectSysHttpAccessByAccessId(Long accessId) {
        return sysHttpAccessMapper.selectSysHttpAccessByAccessId(accessId);
    }

    /**
     * 查询Http访问日志列表
     *
     * @param sysHttpAccess Http访问日志
     * @return Http访问日志
     */
    @Override
    public List<SysHttpAccess> selectSysHttpAccessList(SysHttpAccess sysHttpAccess) {
        return sysHttpAccessMapper.selectSysHttpAccessList(sysHttpAccess);
    }

    /**
     * 新增Http访问日志
     *
     * @param sysHttpAccess Http访问日志
     * @return 结果
     */
    @Override
    public int insertSysHttpAccess(SysHttpAccess sysHttpAccess) {
        return sysHttpAccessMapper.insertSysHttpAccess(sysHttpAccess);
    }

    /**
     * 批量删除Http访问日志
     *
     * @param accessIds 需要删除的Http访问日志主键
     * @return 结果
     */
    @Override
    public int deleteSysHttpAccessByAccessIds(Long[] accessIds) {
        return sysHttpAccessMapper.deleteSysHttpAccessByAccessIds(accessIds);
    }

    /**
     * 删除Http访问日志信息
     *
     * @param accessId Http访问日志主键
     * @return 结果
     */
    @Override
    public int deleteSysHttpAccessByAccessId(Long accessId) {
        return sysHttpAccessMapper.deleteSysHttpAccessByAccessId(accessId);
    }

    /**
     * 清空Http访问日志
     */
    @Override
    public void cleanHttpAccess() {
        sysHttpAccessMapper.cleanHttpAccess();
    }

    /**
     * 每天清理一下Http访问日志，超过90天的将被清除
     */
    @Scheduled(initialDelay = 5 * DateUtils.MILLIS_PER_SECOND, fixedDelay = DateUtils.MILLIS_PER_DAY)
    public void clearGarbage() {
        sysHttpAccessMapper.clearExpired();
    }
}
