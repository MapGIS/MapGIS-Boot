package com.zondy.mapgis.common.accesslog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 使用spring提供的异步任务执行线程池，支持@Async
 *
 * @author xiongbo
 * @since 2022/11/26 17:27
 */
@EnableAsync
@Slf4j
@Configuration
public class AsyncExecutorConfig implements AsyncConfigurer {
    /**
     * 异步任务使用策略：多线程处理
     *
     * @return ThreadPoolTaskScheduler 线程池
     */
    @Bean(name = "asyncExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        executor.setCorePoolSize(poolSize);
        executor.setDaemon(true);
        executor.setThreadNamePrefix("async-");
        executor.setQueueCapacity(poolSize * 1000);
        //设置5分钟，线程空闲则回收，包括核心线程
        executor.setKeepAliveSeconds(60 * 5);
        executor.setAllowCoreThreadTimeOut(true);
        return executor;
    }

    /**
     * 异步任务
     *
     * @return 异步任务执行者
     */
    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }

    /**
     * 异步任务 异常处理
     *
     * @return 异步任务处理对象
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     *
     * @author hry
     */
    static class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            StringBuilder sb = new StringBuilder();
            sb.append("Exception message - ").append(throwable.getMessage());
            sb.append(" Method name - ").append(method.getName());
            for (Object param : obj) {
                sb.append(" Parameter value - ").append(param);
            }
            log.error(sb.toString());
        }
    }
}
