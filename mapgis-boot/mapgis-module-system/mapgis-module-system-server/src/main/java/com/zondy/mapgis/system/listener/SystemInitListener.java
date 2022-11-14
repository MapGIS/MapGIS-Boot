package com.zondy.mapgis.system.listener;

import com.zondy.mapgis.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 系统模块初始化监听器
 *
 * @author xiongbo
 * @since 2022/9/20 17:53
 */
@Component
public class SystemInitListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {
    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String context = "AnnotationConfigServletWebServerApplicationContext";

        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf(context) > -1) {
            sysGatewayRouteService.refreshRoutesCache();
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
