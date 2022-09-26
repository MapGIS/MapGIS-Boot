package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysMicroApp;

import java.util.List;

/**
 * 微应用路由Mapper接口
 *
 * @author mapgis
 * @date 2022-09-24
 */
public interface SysMicroAppMapper {
    /**
     * 查询微应用路由
     *
     * @param microAppId 微应用路由主键
     * @return 微应用路由
     */
    public SysMicroApp selectSysMicroAppByMicroAppId(Long microAppId);

    /**
     * 查询微应用路由列表
     *
     * @param sysMicroApp 微应用路由
     * @return 微应用路由集合
     */
    public List<SysMicroApp> selectSysMicroAppList(SysMicroApp sysMicroApp);

    /**
     * 新增微应用路由
     *
     * @param sysMicroApp 微应用路由
     * @return 结果
     */
    public int insertSysMicroApp(SysMicroApp sysMicroApp);

    /**
     * 修改微应用路由
     *
     * @param sysMicroApp 微应用路由
     * @return 结果
     */
    public int updateSysMicroApp(SysMicroApp sysMicroApp);

    /**
     * 删除微应用路由
     *
     * @param microAppId 微应用路由主键
     * @return 结果
     */
    public int deleteSysMicroAppByMicroAppId(Long microAppId);

    /**
     * 批量删除微应用路由
     *
     * @param microAppIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMicroAppByMicroAppIds(Long[] microAppIds);
}
