package com.zondy.mapgis.system.api.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 系统事件发布者
 *
 * @author powanjuanshu
 * @since 2022/10/29 13:34
 */
@Component
public class SysEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishConfigEvent(String name, String args) {
        publisher.publishEvent(new SysConfigEvent(this, name, args));
    }
}
