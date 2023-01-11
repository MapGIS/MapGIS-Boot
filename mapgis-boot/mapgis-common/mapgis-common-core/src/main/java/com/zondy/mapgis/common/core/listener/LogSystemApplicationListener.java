package com.zondy.mapgis.common.core.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.function.Consumer;

/**
 * 应用程序环境准备事件监听器
 *
 * @author xiongbo
 * @since 2023/1/10 17:32
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20 + 1)
public class LogSystemApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    public LogSystemApplicationListener(Consumer<ApplicationEnvironmentPreparedEvent> consumer) {
        this.consumer = consumer;
    }

    private Consumer<ApplicationEnvironmentPreparedEvent> consumer = null;

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        if (this.consumer != null) {
            this.consumer.accept(applicationEnvironmentPreparedEvent);
        }
    }
}