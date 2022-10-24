package com.zondy.mapgis.system.service;

import com.zondy.mapgis.system.api.domain.SysAuthConfig;

import java.util.List;

/**
 * 第三方登录配置Service接口
 *
 * @author mapgis
 * @date 2022-10-21
 */
public interface ISysAuthConfigService {
    /**
     * 查询第三方登录配置
     *
     * @param configId 第三方登录配置主键
     * @return 第三方登录配置
     */
    public SysAuthConfig selectSysAuthConfigByConfigId(Integer configId);

    /**
     * 查询第三方登录配置列表
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 第三方登录配置集合
     */
    public List<SysAuthConfig> selectSysAuthConfigList(SysAuthConfig sysAuthConfig);

    /**
     * 新增第三方登录配置
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 结果
     */
    public int insertSysAuthConfig(SysAuthConfig sysAuthConfig);

    /**
     * 修改第三方登录配置
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 结果
     */
    public int updateSysAuthConfig(SysAuthConfig sysAuthConfig);

    /**
     * 批量删除第三方登录配置
     *
     * @param configIds 需要删除的第三方登录配置主键集合
     * @return 结果
     */
    public int deleteSysAuthConfigByConfigIds(Integer[] configIds);

    /**
     * 删除第三方登录配置信息
     *
     * @param configId 第三方登录配置主键
     * @return 结果
     */
    public int deleteSysAuthConfigByConfigId(Integer configId);

    /**
     * 校验第三方登录平台是否唯一
     *
     * @param type 登录平台
     * @return 结果
     */
    public boolean isAuthTypeUnique(String type);

    /**
     * 查询第三方平台配置
     *
     * @param type 登录平台
     * @return 第三方登录配置
     */
    public SysAuthConfig selectAuthConfigByType(String type);
}
