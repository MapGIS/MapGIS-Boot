package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.exception.user.UserPasswordIpRetryLimitExceedException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordRetryLimitExceedException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.common.security.utils.BaseSecurityUtils;
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

    /**
     * 登录账号密码错误次数缓存键名
     *
     * @param username     用户名
     * @param ip           ip地址
     * @param isLockedByIp 是否通过IP锁定
     * @return 缓存键key
     */
    private String getCacheKey(String username, String ip, Boolean isLockedByIp) {
        if (isLockedByIp) {
            return CacheConstants.PWD_ERR_CNT_KEY + username + ip;
        } else {
            return CacheConstants.PWD_ERR_CNT_KEY + username;
        }
    }

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
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());

        if (!lockEnabled) {
            if (!matches(user, password)) {
                SpringUtils.getBean(ISysRecordLogService.class).recordLogininfor(username, Constants.LOGIN_FAIL,
                        MessageUtils.message("user.password.not.match"));
                throw new UserPasswordNotMatchException();
            }
            return;
        }

        Integer retryCount = cacheService.getCacheObject(getCacheKey(username, ip, isLockedByIp));

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
            cacheService.setCacheObject(getCacheKey(username, ip, isLockedByIp), retryCount, lockTime.longValue(), TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username, ip, isLockedByIp);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return BaseSecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName, String ip, Boolean isLockedByIp) {
        if (cacheService.hasKey(getCacheKey(loginName, ip, isLockedByIp))) {
            cacheService.deleteObject(getCacheKey(loginName, ip, isLockedByIp));
        }
    }
}