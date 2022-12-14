package com.zondy.mapgis.common.core.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

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

    /**
     * 设置所有的日志级别
     *
     * @param level 日志级别
     */
    public static void setAllLogLevel(String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.toLevel(level));
    }

    /**
     * 修改某一个包下的日志级别
     *
     * @param name  需要修改日志级别的包名或类名
     * @param level 新的日志级别
     */
    public static void setLogLevel(String name, String level) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(name);

        if (logger != null) {
            logger.setLevel(Level.toLevel(level));
        }
    }
}
