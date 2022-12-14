package com.zondy.mapgis.system.listener;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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

    @Autowired
    private ICacheService cacheService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        String context = "AnnotationConfigServletWebServerApplicationContext";

        if (applicationReadyEvent.getApplicationContext().getDisplayName().indexOf(context) > -1) {
            sysGatewayRouteService.refreshRoutesCache();

            HashMap<String, Object> message = new HashMap<>();

            String[] events = {
                    Constants.UPDATE_CAS_CONFIG_LISTENER,
                    Constants.UPDATE_SYSTEM_LOG_CONFIG_LISTENER,
                    Constants.UPDATE_HTTP_ACCESS_LOG_CONFIG_LISTENER};

            for (String event : events) {
                message.put(Constants.REDIS_LISTENER_NAME, event);
                cacheService.convertAndSend(Constants.REDIS_TOPIC_NAME, message);
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
