package com.zondy.mapgis.common.core.constant;

/**
 * 通用常量信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 登录成功状态
     */
    public static final String LOGIN_SUCCESS_STATUS = "0";

    /**
     * 登录失败状态
     */
    public static final String LOGIN_FAIL_STATUS = "1";


    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = {"com.zondy.mapgis"};

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.zondy.mapgis.common.core.utils.file", "com.zondy.mapgis.common.core.config"};

    /**
     * Redis通道名称
     */
    public static final String REDIS_TOPIC_NAME = "redis_topic";

    /**
     * Redis Listener监听器名称key
     */
    public static final String REDIS_LISTENER_NAME = "listenerName";

    /**
     * 刷新网关路由表的监听器名称
     */
    public static final String REFRESH_ROUTES_LISTENER = "refreshRoutesListener";

    /**
     * 更新CAS配置的监听器名称
     */
    public static final String UPDATE_CAS_CONFIG_LISTENER = "updateCasConfigListener";

    /**
     * 更新系统日志配置的监听器名称
     */
    public static final String UPDATE_SYSTEM_LOG_CONFIG_LISTENER = "updateSystemLogConfigListener";

    /**
     * 更新HTTP访问日志配置的监听器名称
     */
    public static final String UPDATE_HTTP_ACCESS_LOG_CONFIG_LISTENER = "updateHttpAccessLogConfigListener";
}
