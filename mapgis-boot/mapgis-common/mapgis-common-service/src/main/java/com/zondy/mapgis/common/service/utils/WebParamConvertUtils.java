package com.zondy.mapgis.common.service.utils;

/**
 * 参数转换对象类
 *
 * @author Chelsea
 * @since 2023/1/10 18:18
 */
public class WebParamConvertUtils {
    public static boolean ofBool(String param) {
        return "on".equalsIgnoreCase(param) || "true".equalsIgnoreCase(param) || "1".equalsIgnoreCase(param);
    }
}
