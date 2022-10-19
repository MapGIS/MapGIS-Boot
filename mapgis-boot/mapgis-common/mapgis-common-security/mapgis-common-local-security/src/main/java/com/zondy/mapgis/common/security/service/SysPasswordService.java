package com.zondy.mapgis.common.security.service;

import cn.hutool.core.lang.Dict;
import com.zondy.mapgis.common.cache.service.CacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.security.context.AuthenticationContextHolder;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author powanjuanshu
 * @since 2022/8/16 16:50
 */
@Component
public class SysPasswordService {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private ISysServiceApi sysServiceApi;

    /**
     * 登录账号密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user) {
        // 获取密码安全配置
        R<String> configResult = sysServiceApi.selectConfigValueByKey("security.passwordProtected", SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            throw new ServiceException(configResult.getMsg());
        }

        Dict passwordProtectedInfo = JsonUtils.parseMap(configResult.getData());

        if (StringUtils.isEmpty(passwordProtectedInfo)) {
            return;
        }

        Boolean lockEnabled = passwordProtectedInfo.get("enabled", Boolean.FALSE);
        Integer maxRetryCount = passwordProtectedInfo.get("maxRetryCount", Integer.valueOf(5));
        Integer lockTime = passwordProtectedInfo.get("lockTime", Integer.valueOf(10));

        if (!lockEnabled) {
            return;
        }

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();

        if (usernamePasswordAuthenticationToken == null) {
            return;
        }

        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        Integer retryCount = cacheService.getCacheObject(getCacheKey(username));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount.intValue()) {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            cacheService.setCacheObject(getCacheKey(username), retryCount, lockTime.longValue(), TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (cacheService.hasKey(getCacheKey(loginName))) {
            cacheService.deleteObject(getCacheKey(loginName));
        }
    }
}
