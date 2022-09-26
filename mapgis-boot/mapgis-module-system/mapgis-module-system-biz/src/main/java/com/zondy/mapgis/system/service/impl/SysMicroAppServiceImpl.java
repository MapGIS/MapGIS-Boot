package com.zondy.mapgis.system.service.impl;

import com.zondy.mapgis.system.domain.SysMicroApp;
import com.zondy.mapgis.system.mapper.SysMicroAppMapper;
import com.zondy.mapgis.system.service.ISysMicroAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微应用路由Service业务层处理
 *
 * @author mapgis
 * @date 2022-09-24
 */
@Service
public class SysMicroAppServiceImpl implements ISysMicroAppService {
    @Autowired
    private SysMicroAppMapper sysMicroAppMapper;

    /**
     * 查询微应用路由
     *
     * @param microAppId 微应用路由主键
     * @return 微应用路由
     */
    @Override
    public SysMicroApp selectSysMicroAppByMicroAppId(Long microAppId) {
        return sysMicroAppMapper.selectSysMicroAppByMicroAppId(microAppId);
    }

    /**
     * 查询微应用路由列表
     *
     * @param sysMicroApp 微应用路由
     * @return 微应用路由
     */
    @Override
    public List<SysMicroApp> selectSysMicroAppList(SysMicroApp sysMicroApp) {
        return sysMicroAppMapper.selectSysMicroAppList(sysMicroApp);
    }

    /**
     * 新增微应用路由
     *
     * @param sysMicroApp 微应用路由
     * @return 结果
     */
    @Override
    public int insertSysMicroApp(SysMicroApp sysMicroApp) {
        return sysMicroAppMapper.insertSysMicroApp(sysMicroApp);
    }

    /**
     * 修改微应用路由
     *
     * @param sysMicroApp 微应用路由
     * @return 结果
     */
    @Override
    public int updateSysMicroApp(SysMicroApp sysMicroApp) {
        return sysMicroAppMapper.updateSysMicroApp(sysMicroApp);
    }

    /**
     * 批量删除微应用路由
     *
     * @param microAppIds 需要删除的微应用路由主键
     * @return 结果
     */
    @Override
    public int deleteSysMicroAppByMicroAppIds(Long[] microAppIds) {
        return sysMicroAppMapper.deleteSysMicroAppByMicroAppIds(microAppIds);
    }

    /**
     * 删除微应用路由信息
     *
     * @param microAppId 微应用路由主键
     * @return 结果
     */
    @Override
    public int deleteSysMicroAppByMicroAppId(Long microAppId) {
        return sysMicroAppMapper.deleteSysMicroAppByMicroAppId(microAppId);
    }
}
