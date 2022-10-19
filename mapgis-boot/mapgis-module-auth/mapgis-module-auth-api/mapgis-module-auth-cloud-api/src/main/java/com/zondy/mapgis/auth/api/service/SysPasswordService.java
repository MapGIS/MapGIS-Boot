package com.zondy.mapgis.auth.api.service;

import cn.hutool.core.lang.Dict;
import com.zondy.mapgis.common.cache.service.CacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CacheService cacheService;

    @Autowired
    private SysRecordLogService recordLogService;

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

    public void validate(SysUser user, String password) {
        String username = user.getUserName();

        // 获取密码安全配置
        R<String> configResult = sysServiceApi.selectConfigValueByKey("security.passwordProtected", SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            throw new ServiceException(configResult.getMsg());
        }

        Dict passwordProtectedInfo = JsonUtils.parseMap(configResult.getData());

        Boolean lockEnabled = false;
        Integer maxRetryCount = 5;
        Integer lockTime = 10;

        if (StringUtils.isNotEmpty(passwordProtectedInfo)) {
            lockEnabled = passwordProtectedInfo.getBool("enabled");
            maxRetryCount = passwordProtectedInfo.getInt("maxRetryCount");
            lockTime = passwordProtectedInfo.getInt("lockTime");
        }

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