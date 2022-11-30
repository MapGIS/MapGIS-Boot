package com.zondy.mapgis.common.accesslog.recorder;

import com.zondy.mapgis.common.accesslog.config.HttpAccessConfig;
import com.zondy.mapgis.common.accesslog.service.ISysHttpAccessLogService;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import com.zondy.mapgis.system.api.domain.SysLogConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Http访问记录信息，异步存储任务
 *
 * @author mapgis
 * @since 2022/11/29 13:45
 */
@Slf4j
@Component
public class AsyncHttpAccessRecorder {
    private final int MAX_BUFFER_SIZE = 1024;
    private final ArrayBlockingQueue<SysHttpAccess> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);
    private final SysLogConfig logConfig;
    private final AsyncTaskExecutor executor;
    private final AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    public AsyncHttpAccessRecorder(@Qualifier("asyncExecutor") AsyncTaskExecutor executor, HttpAccessConfig httpAccessConfig) {
        this.logConfig = httpAccessConfig.getLogConfig();
        this.executor = executor;
    }

    public void record(SysHttpAccess httpAccess) {
        if (logConfig.getHttpAccessEnabled()) {
            initIfNecessary();
            try {
                // 当队列满时，会阻塞
                queue.put(httpAccess);
            } catch (InterruptedException e) {
                log.warn("将HarEntry加入队列出现异常", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 如果未初始化，则执行初始化，只会执行一次
     */
    private void initIfNecessary() {
        if (running.compareAndSet(false, true)) {
            try {
                executor.execute(this::run);
            } catch (Exception e) {
                log.error("启动服务访问监控失败", e);
            }
        }
    }

    private void run() {
        while (running.get()) {
            try {
                SysHttpAccess httpAccess = queue.take();
                SpringUtils.getBean(ISysHttpAccessLogService.class).recordHttpAccess(httpAccess);
            } catch (InterruptedException e) {
                // 进程退出时，线程被强制中断会抛出异常
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("写入http_access记录出现异常", e);
            }
        }
    }

    public void stop() {
        if (running.compareAndSet(true, false)) {
            // 暂停后，继续将队列中剩余的全部保存
            for (SysHttpAccess httpAccess : queue.toArray(new SysHttpAccess[0])) {
                SpringUtils.getBean(ISysHttpAccessLogService.class).recordHttpAccess(httpAccess);
            }
            // 最后清空队列
            queue.clear();
        }
    }

    public void restart() {
        stop();
        running.compareAndSet(false, true);
    }
}
