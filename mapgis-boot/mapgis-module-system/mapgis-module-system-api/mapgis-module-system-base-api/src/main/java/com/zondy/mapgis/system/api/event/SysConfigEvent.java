package com.zondy.mapgis.system.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 系统配置事件
 *
 * @author xiongbo
 * @since 2022/10/29 13:16
 */
@Setter
@Getter
public class SysConfigEvent extends ApplicationEvent {
    private String name;
    private Object args;

    public SysConfigEvent(Object source, String name, Object args) {
        super(source);
        this.name = name;
        this.args = args;
    }
}
