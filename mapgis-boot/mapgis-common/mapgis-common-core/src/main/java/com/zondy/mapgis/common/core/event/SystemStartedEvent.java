package com.zondy.mapgis.common.core.event;

import java.util.EventObject;

/**
 * 系统进程启动完成后触发的事件
 *
 * @author Chelsea
 * @since 2023/1/10 16:30
 */
public class SystemStartedEvent extends EventObject {
    public SystemStartedEvent(Object source) {
        super(source);
    }
}