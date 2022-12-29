package com.zondy.mapgis.common.core.utils;

import com.zondy.mapgis.common.core.constant.CacheConstants;

/**
 * 缓存工具类
 *
 * @author xiongbo
 * @since 2022/12/29 16:52
 */
public class CacheUtils {
    /**
     * 登录账号密码错误次数缓存键名
     *
     * @param username     用户名
     * @param ip           ip地址
     * @param isLockedByIp 是否通过IP锁定
     * @return 缓存键key
     */
    public static String getLoginCacheKey(String username, String ip, Boolean isLockedByIp) {
        if (isLockedByIp) {
            return CacheConstants.PWD_ERR_CNT_KEY + username + ip;
        } else {
            return CacheConstants.PWD_ERR_CNT_KEY + username;
        }
    }

    /**
     * 显示验证码登录账号密码错误次数缓存键名
     *
     * @param username 用户名
     * @param ip       ip地址
     * @return 缓存键key
     */
    public static String getCaptchaLoginCacheKey(String username, String ip) {
        return CacheConstants.CAPTCHA_PWD_ERR_CNT_KEY + username + ip;
    }
}
