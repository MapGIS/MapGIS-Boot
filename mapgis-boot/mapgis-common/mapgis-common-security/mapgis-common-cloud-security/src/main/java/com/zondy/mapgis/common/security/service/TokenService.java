package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.utils.JwtUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.core.utils.uuid.IdUtils;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class TokenService {
    @Autowired
    private ICacheService cacheService;

    @Autowired
    private SysServiceProxy sysServiceProxy;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static String USERID_KEY = CacheConstants.LOGIN_USERID_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = cacheService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token, Long userId) {
        boolean soloLogin = isSoloLoginEnabled();

        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            cacheService.deleteObject(getTokenKey(userkey));
        }
        if (!soloLogin && StringUtils.isNotNull(userId)) {
            String userIdKey = getUserIdKey(userId);
            cacheService.deleteObject(userIdKey);
        }
    }

    /**
     * 根据用户Id踢出登录用户，用于不允许多终端登录时，清除用户Id关联的用户信息
     */
    public void kickoutLoginUser(Long userId) {
        boolean soloLogin = isSoloLoginEnabled();

        if (!soloLogin) {
            // 如果用户不允许多终端同时登录，清除缓存信息
            String userIdKey = getUserIdKey(userId);
            String userKey = cacheService.getCacheObject(userIdKey);
            if (StringUtils.isNotEmpty(userKey)) {
                cacheService.deleteObject(userIdKey);
                cacheService.deleteObject(userKey);
            }
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getUser().getUserId();
        String userName = loginUser.getUser().getUserName();
        loginUser.setToken(token);
        loginUser.setUserId(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 生成token
        return JwtUtils.createToken(claimsMap);
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        boolean soloLogin = isSoloLoginEnabled();

        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        cacheService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        if (!soloLogin) {
            // 缓存用户唯一标识，防止同一帐号，同时登录
            String userIdKey = getUserIdKey(loginUser.getUser().getUserId());
            cacheService.setCacheObject(userIdKey, userKey, expireTime, TimeUnit.MINUTES);
        }
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }

    private String getUserIdKey(Long userId) {
        return USERID_KEY + userId;
    }

    private boolean isSoloLoginEnabled() {
        return (Boolean) sysServiceProxy.getLoginConfig().get("soloLoginEnabled");
    }
}