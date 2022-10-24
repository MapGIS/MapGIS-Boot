package com.zondy.mapgis.system.api.service;

import cn.hutool.core.lang.Dict;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务代理
 *
 * @author powanjuanshu
 * @since 2022/10/19 18:06
 */
@Component
public class SysServiceProxy {
    @Autowired
    private ISysServiceApi sysServiceApi;

    /**
     * 获取配置值
     *
     * @param key 配置key
     * @return 配置值
     */
    public String selectConfigValueByKey(String key) {
        R<String> configResult = sysServiceApi.selectConfigValueByKey(key, SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            throw new ServiceException(configResult.getMsg());
        }

        return configResult.getData();
    }

    /**
     * 获取用户初始密码配置
     *
     * @return 用户初始密码
     */
    public String getInitPasswordConfig() {
        return selectConfigValueByKey("security.initPassword");
    }

    /**
     * 获取注册配置
     *
     * @return 注册配置
     */
    public Map<String, Object> getRegisterConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        String strConfig = selectConfigValueByKey("security.register");

        Dict registerUserInfo = JsonUtils.parseMap(strConfig);
        Boolean enabled = true;
        Long[] roleIds = new Long[0];

        if (StringUtils.isNotEmpty(registerUserInfo)) {
            enabled = registerUserInfo.get("enabled", Boolean.TRUE);
            List<Integer> roleIdList = registerUserInfo.get("defaultRoleIds", new ArrayList<>());
            roleIds = new Long[roleIdList.size()];
            for (int i = 0; i < roleIdList.size(); i++) {
                roleIds[i] = roleIdList.get(i).longValue();
            }
        }

        config.put("enabled", enabled);
        config.put("defaultRoleIds", roleIds);
        return config;
    }

    /**
     * 获取登录配置
     *
     * @return 登录配置
     */
    public Map<String, Object> getLoginConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        String strConfig = selectConfigValueByKey("security.login");

        Dict loginInfo = JsonUtils.parseMap(strConfig);
        Boolean soloLoginEnabled = true;
        Boolean captchaEnabled = true;
        String captchaType = "math";

        if (StringUtils.isNotEmpty(loginInfo)) {
            soloLoginEnabled = loginInfo.get("soloLoginEnabled", Boolean.TRUE);
            captchaEnabled = loginInfo.get("captchaEnabled", Boolean.TRUE);
            captchaType = loginInfo.get("captchaType", "math");
        }

        config.put("soloLoginEnabled", soloLoginEnabled);
        config.put("captchaEnabled", captchaEnabled);
        config.put("captchaType", captchaType);
        return config;
    }

    /**
     * 获取密码安全配置
     *
     * @return 密码安全配置
     */
    public Map<String, Object> getPasswordProtectedConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        String strConfig = selectConfigValueByKey("security.passwordProtected");
        Dict passwordProtectedInfo = JsonUtils.parseMap(strConfig);

        Boolean enabled = Boolean.FALSE;
        Integer maxRetryCount = 5;
        Integer lockTime = 10;

        if (StringUtils.isEmpty(passwordProtectedInfo)) {
            enabled = passwordProtectedInfo.get("enabled", Boolean.FALSE);
            maxRetryCount = passwordProtectedInfo.get("maxRetryCount", 5);
            lockTime = passwordProtectedInfo.get("lockTime", 10);
        }

        config.put("enabled", enabled);
        config.put("maxRetryCount", maxRetryCount);
        config.put("lockTime", lockTime);
        return config;
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
    public Map<String, Object> getOAuthConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        String strConfig = selectConfigValueByKey("security.oauth");

        Dict thirdUserInfo = JsonUtils.parseMap(strConfig);
        Long[] roleIds = new Long[0];

        if (StringUtils.isNotEmpty(thirdUserInfo)) {
            List<Integer> roleIdList = thirdUserInfo.get("defaultRoleIds", new ArrayList<>());
            roleIds = new Long[roleIdList.size()];
            for (int i = 0; i < roleIdList.size(); i++) {
                roleIds[i] = roleIdList.get(i).longValue();
            }
        }

        config.put("defaultRoleIds", roleIds);
        return config;
    }
}
