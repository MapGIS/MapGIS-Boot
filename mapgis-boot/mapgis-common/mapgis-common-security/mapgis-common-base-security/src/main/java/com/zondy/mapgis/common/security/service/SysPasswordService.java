package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.exception.user.UserPasswordIpRetryLimitExceedException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.CacheUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.common.security.utils.BaseSecurityUtils;
import com.zondy.mapgis.system.api.domain.SysLoginConfig;
import com.zondy.mapgis.system.api.domain.SysPasswordProtectedConfig;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void validate(SysUser user, String password) {
        if (StringUtils.isEmpty(password)) {
            return;
        }

        String username = user.getUserName();
        // 获取密码安全配置
        SysPasswordProtectedConfig sysPasswordProtectedConfig = sysServiceProxy.getPasswordProtectedConfig();

        Boolean lockEnabled = sysPasswordProtectedConfig.getEnabled();
        Integer maxRetryCount = sysPasswordProtectedConfig.getMaxRetryCount();
        Integer lockTime = sysPasswordProtectedConfig.getLockTime();
        Boolean isLockedByIp = sysPasswordProtectedConfig.getIsLockedByIp();
        String ip = IpUtils.getIpAddr();

        if (!lockEnabled) {
            if (!matches(user, password)) {
                SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                        MessageUtils.message("user.password.not.match"));
                // 记录验证码显示登录错误次数缓存
                recordCaptchaLoginCache(username, ip);
                throw new UserPasswordNotMatchException();
            } else {
                clearCaptchaLoginCache(username, ip);
            }
            return;
        }

        Integer retryCount = cacheService.getCacheObject(CacheUtils.getLoginCacheKey(username, ip, isLockedByIp));

        if (retryCount == null) {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount.intValue()) {
            if (isLockedByIp) {
                SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                        MessageUtils.message("user.password.ip.retry.limit.exceed", maxRetryCount, username, ip, lockTime));
                throw new UserPasswordIpRetryLimitExceedException(maxRetryCount, username, ip, lockTime);
            } else {
                SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                        MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount, username, lockTime));
                throw new UserPasswordRetryLimitExceedException(maxRetryCount, username, lockTime);
            }
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.retry.limit.count", retryCount));
            cacheService.setCacheObject(CacheUtils.getLoginCacheKey(username, ip, isLockedByIp), retryCount, lockTime.longValue(), TimeUnit.MINUTES);
            // 记录验证码显示登录错误次数缓存
            recordCaptchaLoginCache(username, ip);
            String[] errorInfo = new String[1];
            errorInfo[0] = String.valueOf(retryCount);
            throw new UserPasswordNotMatchException(errorInfo);
        } else {
            clearLoginCache(username, ip, isLockedByIp);
            clearCaptchaLoginCache(username, ip);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return BaseSecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginCache(String loginName, String ip, Boolean isLockedByIp) {
        if (cacheService.hasKey(CacheUtils.getLoginCacheKey(loginName, ip, isLockedByIp))) {
            cacheService.deleteObject(CacheUtils.getLoginCacheKey(loginName, ip, isLockedByIp));
        }
    }

    public void clearCaptchaLoginCache(String loginName, String ip) {
        if (cacheService.hasKey(CacheUtils.getCaptchaLoginCacheKey(loginName, ip))) {
            cacheService.deleteObject(CacheUtils.getCaptchaLoginCacheKey(loginName, ip));
        }
    }

    public void recordCaptchaLoginCache(String loginName, String ip) {
        // 获取用户登录安全配置
        SysLoginConfig sysLoginConfig = sysServiceProxy.getLoginConfig();
        final Boolean captchaEnabled = sysLoginConfig.getCaptchaEnabled();
        final Integer maxRetryCount = sysLoginConfig.getMaxRetryCount();
        final Integer recordTime = sysLoginConfig.getRecordTime();

        // 启用验证码并且需要N次失败后才显示
        if (captchaEnabled && maxRetryCount > 0) {
            Integer retryCount = cacheService.getCacheObject(CacheUtils.getCaptchaLoginCacheKey(loginName, ip));

            if (retryCount == null) {
                retryCount = 0;
            }

            retryCount = retryCount + 1;
            cacheService.setCacheObject(CacheUtils.getCaptchaLoginCacheKey(loginName, ip), retryCount, recordTime.longValue(), TimeUnit.MINUTES);
        }
    }
}