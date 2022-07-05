package com.zondy.mapgis.common.core.constant;

/**
 * 缓存的key 常量
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class CacheConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 登录用户 redis key
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 登录用户编号 redis key
     */
    public static final String LOGIN_USERID_KEY = "login_userid:";
}
