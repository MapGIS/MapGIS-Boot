package com.zondy.mapgis.common.core.utils;

/**
 * 处理并记录日志文件
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class LogUtils {
    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
