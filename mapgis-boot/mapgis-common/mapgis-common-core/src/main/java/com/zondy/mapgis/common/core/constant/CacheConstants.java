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
     * 验证码有效期（分钟）
     */
    public static final long CAPTCHA_EXPIRATION = 2;

    /**
     * 登录用户 cache key
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 登录用户编号 cache key
     */
    public static final String LOGIN_USERID_KEY = "login_userid:";

    /**
     * 验证码 cache key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 防重提交 cache key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 cache key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 登录账号密码错误次数 cache key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 路由表 cache key
     */
    public static final String SYS_ROUTES_KEY = "sys_routes:";

    /**
     * 显示验证码登录账号密码错误次数 cache key
     */
    public static final String CAPTCHA_PWD_ERR_CNT_KEY = "captcha_pwd_err_cnt:";
}
