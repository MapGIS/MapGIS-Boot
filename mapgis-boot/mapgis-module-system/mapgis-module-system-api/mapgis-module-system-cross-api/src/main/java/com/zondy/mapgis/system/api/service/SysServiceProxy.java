package com.zondy.mapgis.system.api.service;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.core.constant.ConfigConstants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.*;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务代理
 *
 * @author powanjuanshu
 * @since 2022/10/19 18:05
 */
@Component
public class SysServiceProxy {
    @Autowired
    private ISysServiceApi sysServiceApi;

    @Autowired
    private ApiPathProperties apiPathProperties;

    /**
     * 获取登录用户信息
     *
     * @param username 用户名
     * @return 登录用户
     */
    public LoginUser getUserInfo(String username) {
        R<LoginUser> loginUserResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == loginUserResult.getCode()) {
            throw new ServiceException(loginUserResult.getMsg());
        }

        return loginUserResult.getData();
    }

    public boolean registerUserInfo(SysUser sysUser) {
        R<Boolean> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }

        return true;
    }

    public boolean updateUserProfile(SysUser sysUser) {
        R<Boolean> updateResult = sysServiceApi.updateUserProfile(sysUser, SecurityConstants.INNER);

        if (R.FAIL == updateResult.getCode()) {
            throw new ServiceException(updateResult.getMsg());
        }

        return true;
    }

    /**
     * 查询第三方登录授权用户列表
     *
     * @param user 查询条件
     * @return 第三方登录授权用户列表
     */
    public List<SysAuthUser> selectAuthUserList(SysAuthUser user) {
        R<List<SysAuthUser>> sysAuthUserListResult = sysServiceApi.selectAuthUserList(user, SecurityConstants.INNER);

        if (R.FAIL == sysAuthUserListResult.getCode()) {
            throw new ServiceException(sysAuthUserListResult.getMsg());
        }

        return sysAuthUserListResult.getData();
    }

    /**
     * 更新第三方登录授权用户
     *
     * @param user 第三方登录授权用户
     * @return 更新结果
     */
    public boolean updateAuthUser(SysAuthUser user) {
        R<Boolean> updateResult = sysServiceApi.updateAuthUser(user, SecurityConstants.INNER);

        if (R.FAIL == updateResult.getCode()) {
            throw new ServiceException(updateResult.getMsg());
        }

        return updateResult.getData();
    }

    /**
     * 根据第三方登录授权用户UUID查询系统用户
     *
     * @param uuid 第三方登录授权用户UUID
     * @return 系统用户
     */
    public SysUser selectUserByAuthUuid(String uuid) {
        R<SysUser> userResult = sysServiceApi.selectUserByAuthUuid(uuid, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        return userResult.getData();
    }

    /**
     * 获取配置值
     *
     * @param key 配置key
     * @return 配置值
     */
    public String selectConfigValueByKey(String key) {
        R<String> configResult = sysServiceApi.selectConfigValueByKey(key, SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            return "";
        }

        return configResult.getData();
    }

    /**
     * 获取配置值
     *
     * @param key          配置key
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String selectConfigValueByKey(String key, String defaultValue) {
        R<String> configResult = sysServiceApi.selectConfigValueByKey(key, SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            return defaultValue;
        }

        return configResult.getData();
    }

    /**
     * 获取配置
     *
     * @param key   配置key
     * @param clazz 对象
     * @return 配置
     */
    public <T> T parseConfigObject(String key, Class<T> clazz) {
        String strConfig = selectConfigValueByKey(key, "{}");

        return JsonUtils.parseObject(strConfig, clazz);
    }

    /**
     * 获取用户初始密码配置
     *
     * @return 用户初始密码
     */
    public String getInitPasswordConfig() {
        return selectConfigValueByKey(ConfigConstants.CONFIG_KEY_SECURITY_INIT_PASSWORD);
    }

    /**
     * 获取注册配置
     *
     * @return 注册配置
     */
    public SysRegisterConfig getRegisterConfig() {
        return parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_REGISTER, SysRegisterConfig.class);
    }

    /**
     * 获取登录配置
     *
     * @return 登录配置
     */
    public SysLoginConfig getLoginConfig() {
        return parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_LOGIN, SysLoginConfig.class);
    }

    /**
     * 获取密码安全配置
     *
     * @return 密码安全配置
     */
    public SysPasswordProtectedConfig getPasswordProtectedConfig() {
        return parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_PASSWORD_PROTECTED, SysPasswordProtectedConfig.class);
    }

    /**
     * 根据平台获取第三方登录配置
     */
    public SysAuthConfig getAuthConfigByType(String type) {
        R<SysAuthConfig> sysAuthConfigResult = sysServiceApi.selectAuthConfigByType(type, SecurityConstants.INNER);

        if (R.FAIL == sysAuthConfigResult.getCode()) {
            throw new ServiceException(sysAuthConfigResult.getMsg());
        }

        return sysAuthConfigResult.getData();
    }

    /**
     * 获取第三方登录配置
     *
     * @return 登录配置
     */
    public SysAuthExtraConfig getOAuthConfig() {
        return parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_OAUTH, SysAuthExtraConfig.class);
    }

    /**
     * 获取CAS登录配置
     *
     * @return 登录配置
     */
    public SysCasConfig getCasConfig() {
        SysCasConfig sysCasConfig = parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_CAS, SysCasConfig.class);

        sysCasConfig.setServicesPrefix(apiPathProperties.getServicesPrefix());
        return sysCasConfig;
    }

    /**
     * 获取LDAP登录配置
     */
    public SysLdapConfig getLdapConfig() {
        return parseConfigObject(ConfigConstants.CONFIG_KEY_SECURITY_LDAP, SysLdapConfig.class);
    }
}
