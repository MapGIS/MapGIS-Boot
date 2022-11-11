package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.common.security.utils.BaseSecurityUtils;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author xiongbo
 * @since 2022/8/16 16:50
 */
@Component
public class SysPasswordService {
    @Autowired
    private ICacheService cacheService;

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
        if (StringUtils.isEmpty(password)) {
            return;
        }

        String username = user.getUserName();
        // 获取密码安全配置
        Map<String, Object> passwordProtectedConfig = sysServiceProxy.getPasswordProtectedConfig();

        Boolean lockEnabled = (Boolean) passwordProtectedConfig.get("enabled");
        Integer maxRetryCount = (Integer) passwordProtectedConfig.get("maxRetryCount");
        Integer lockTime = (Integer) passwordProtectedConfig.get("lockTime");

        if (!lockEnabled) {
            if (!matches(user, password)) {
                SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
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
            SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, lockTime));
            throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount));
            cacheService.setCacheObject(getCacheKey(username), retryCount, lockTime.longValue(), TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return BaseSecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        if (cacheService.hasKey(getCacheKey(loginName))) {
            cacheService.deleteObject(getCacheKey(loginName));
        }
    }
}