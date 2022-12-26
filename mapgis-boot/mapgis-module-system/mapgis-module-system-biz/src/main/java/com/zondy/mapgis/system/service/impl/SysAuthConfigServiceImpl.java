package com.zondy.mapgis.system.service.impl;

import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.system.api.domain.SysAuthConfig;
import com.zondy.mapgis.system.mapper.SysAuthConfigMapper;
import com.zondy.mapgis.system.service.ISysAuthConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 第三方登录配置Service业务层处理
 *
 * @author mapgis
 * @date 2022-10-21
 */
@Service
public class SysAuthConfigServiceImpl implements ISysAuthConfigService {
    @Autowired
    private SysAuthConfigMapper sysAuthConfigMapper;

    /**
     * 查询第三方登录配置
     *
     * @param configId 第三方登录配置主键
     * @return 第三方登录配置
     */
    @Override
    public SysAuthConfig selectSysAuthConfigByConfigId(Integer configId) {
        return sysAuthConfigMapper.selectSysAuthConfigByConfigId(configId);
    }

    /**
     * 查询第三方登录配置列表
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 第三方登录配置
     */
    @Override
    public List<SysAuthConfig> selectSysAuthConfigList(SysAuthConfig sysAuthConfig) {
        return sysAuthConfigMapper.selectSysAuthConfigList(sysAuthConfig);
    }

    /**
     * 新增第三方登录配置
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 结果
     */
    @Override
    public int insertSysAuthConfig(SysAuthConfig sysAuthConfig) {
        return sysAuthConfigMapper.insertSysAuthConfig(sysAuthConfig);
    }

    /**
     * 修改第三方登录配置
     *
     * @param sysAuthConfig 第三方登录配置
     * @return 结果
     */
    @Override
    public int updateSysAuthConfig(SysAuthConfig sysAuthConfig) {
        return sysAuthConfigMapper.updateSysAuthConfig(sysAuthConfig);
    }

    /**
     * 批量删除第三方登录配置
     *
     * @param configIds 需要删除的第三方登录配置主键
     * @return 结果
     */
    @Override
    public int deleteSysAuthConfigByConfigIds(Integer[] configIds) {
        return sysAuthConfigMapper.deleteSysAuthConfigByConfigIds(configIds);
    }

    /**
     * 删除第三方登录配置信息
     *
     * @param configId 第三方登录配置主键
     * @return 结果
     */
    @Override
    public int deleteSysAuthConfigByConfigId(Integer configId) {
        return sysAuthConfigMapper.deleteSysAuthConfigByConfigId(configId);
    }

    /**
     * 校验第三方登录平台是否唯一
     *
     * @param type 登录平台
     * @return 结果
     */
    @Override
    public boolean isAuthTypeUnique(String type) {
        return sysAuthConfigMapper.checkAuthTypeUnique(type) > 0;
    }

    /**
     * 查询第三方平台配置
     *
     * @param type 登录平台
     * @return 第三方登录配置
     */
    @Override
    public SysAuthConfig selectAuthConfigByType(String type) {
        return sysAuthConfigMapper.selectAuthConfigByType(type);
    }
}
