package com.zondy.mapgis.gateway.listener;

import com.zondy.mapgis.common.cache.listener.RedisListener;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.gateway.config.SwaggerProvider;
import com.zondy.mapgis.gateway.service.SysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 刷新路由表监听器
 *
 * @author powanjuanshu
 * @since 2022/9/21 9:17
 */
@Component(Constants.REFRESH_ROUTES_LISTENER)
public class RefreshRoutesListener implements RedisListener {
    @Autowired
    private SysGatewayRouteService gatewayRouteService;

    @Autowired
    private SwaggerProvider swaggerProvider;

    @Override
    public void onMessage(HashMap<String, Object> message) {
        gatewayRouteService.refresh(message);
        swaggerProvider.refreshApis();
    }
}
