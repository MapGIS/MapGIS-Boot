package com.zondy.mapgis.common.core.utils;

import org.springframework.core.env.Environment;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class EnvironmentUtil {
    /**
     * 控制台输出被禁用属性名
     */
    public static final String CONSOLE_OUT_DISABLED_PROP = "ConsoleOutDisabled";
    /**
     * 控制台输出是否被禁用
     */
    public static final boolean CONSOLE_OUT_DISABLED = "true".equals(System.getProperty(CONSOLE_OUT_DISABLED_PROP));

    /**
     * 获取当前项目路径
     */
    public static String getCurrentProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 是否是单体版模式
     *
     * @param env 环境对象
     * @return 结果
     */
    public static boolean isSingleServiceMode(Environment env) {
        return "ehcache".equals(env.getProperty("spring.cache.type"));
    }
}
