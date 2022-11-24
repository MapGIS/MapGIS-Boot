package com.zondy.mapgis.common.systemlog.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author xiongbo
 * @since 2022/11/24 14:19
 */
public class LogUtils {
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
