package com.zondy.mapgis.common.core.utils;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 提供对代码块耗时监控功能的工具类
 *
 * @author Chelsea
 * @since 2023/1/11 10:05
 */
public class MetricUtil {
    /**
     * 最大的耗时，单位ms
     */
    public final static long MAX_DURATION_MS = 3000;

    /**
     * 执行带返回结果的方法，同时对执行代码块记录耗时
     *
     * @param supplier         执行方法体
     * @param callbackAfterRun 执行结束后回调，参数1为耗时毫秒,参数2为返回结果
     * @param <T>              返回类型
     * @return 返回方法执行的结果
     */
    public static <T> T run(@NonNull Supplier<T> supplier, @NonNull BiConsumer<Long, T> callbackAfterRun) {
        StopWatch sw = new StopWatch();
        T t = null;
        try {
            sw.start();
            t = supplier.get();
            return t;
        } finally {
            sw.stop();
            callbackAfterRun.accept(sw.getLastTaskTimeMillis(), t);
        }
    }

    /**
     * 执行带返回结果的方法，同时对执行代码块记录耗时
     *
     * @param supplier     执行方法体
     * @param logger       当前上下文日志类
     * @param logTitleFunc 生成日志标题的方法，参数1为方法执行结果，返回日志标题
     * @param <T>          返回类型
     * @return 返回方法执行的结果
     */
    public static <T> T run(@NonNull Supplier<T> supplier, @NonNull Logger logger, @NonNull Function<T, String> logTitleFunc) {
        return run(supplier, (ms, t) -> {
            if (ms > MAX_DURATION_MS) {
                logger.warn("{},time={}", logTitleFunc.apply(t), ms);
            } else {
                EnvUtils.debug("{},time={}", logTitleFunc.apply(t), ms);
            }
        });
    }


    /**
     * 执行带返回结果的方法，同时对执行代码块记录耗时
     *
     * @param supplier 执行方法体
     * @param logger   当前上下文日志类
     * @param logTitle 日志标题
     * @param <T>      返回类型
     * @return 返回方法执行的结果
     */
    public static <T> T run(@NonNull Supplier<T> supplier, @NonNull Logger logger, @NonNull String logTitle) {
        return run(supplier, (ms, t) -> {
            if (ms > MAX_DURATION_MS) {
                logger.warn("{},time={}", logTitle, ms);
            } else {
                EnvUtils.debug("{},time={}", logTitle, ms);
            }
        });
    }

    /**
     * 执行不带返回结果的方法，同时对执行代码块记录耗时
     *
     * @param runnable         执行方法体
     * @param callbackAfterRun 耗时回调，参数为耗时毫秒
     */
    public static void run(@NonNull Runnable runnable, @NonNull Consumer<Long> callbackAfterRun) {
        StopWatch sw = new StopWatch();
        try {
            sw.start();
            runnable.run();
        } finally {
            sw.stop();
            callbackAfterRun.accept(sw.getLastTaskTimeMillis());
        }
    }

    /**
     * 执行不带返回结果的方法，同时对执行代码块记录耗时
     *
     * @param runnable 执行方法体
     * @param logger   当前上下文日志类
     * @param logTitle 日志标题
     */
    public static void run(@NonNull Runnable runnable, @NonNull Logger logger, @NonNull String logTitle) {
        run(runnable, (ms) -> {
            if (ms > MAX_DURATION_MS) {
                logger.warn("{},time={}", logTitle, ms);
            } else {
                EnvUtils.debug("{},time={}", logTitle, ms);
            }
        });
    }
}
