package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author powanjuanshu
 * @since 2022/8/16 17:11
 */
@Component
public class SysPasswordService {
    @Autowired
    private ICacheService cacheService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private SysServiceProxy sysServiceProxy;

    /**
     * 登录账号密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password) {
        String username = user.getUserName();

        // 获取密码安全配置
        Map<String, Object> passwordProtectedConfig = sysServiceProxy.getPasswordProtectedConfig();

        Boolean lockEnabled = (Boolean) passwordProtectedConfig.get("enabled");
        Integer maxRetryCount = (Integer) passwordProtectedConfig.get("maxRetryCount");
        Integer lockTime = (Integer) passwordProtectedConfig.get("lockTime");

        if (!lockEnabled) {
            if (!matches(user, password)) {
                recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL,
                        MessageUtils.message("user.password.not.match"));
                throw new UserPasswordNotMatchException();
            }
            return;
        }

        Integer retryCount = cacheService.getCacheObject(getCacheKey(username));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount.intValue()) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount));
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