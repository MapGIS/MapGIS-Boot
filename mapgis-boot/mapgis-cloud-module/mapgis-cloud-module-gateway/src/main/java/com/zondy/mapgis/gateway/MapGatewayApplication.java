package com.zondy.mapgis.gateway;

import com.zondy.mapgis.common.accesslog.listener.UpdateHttpAccessLogConfigListener;
import com.zondy.mapgis.gateway.config.SwaggerProvider;
import com.zondy.mapgis.gateway.service.SysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 网关启动程序
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableFeignClients(basePackages = {"com.zondy.mapgis"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MapGatewayApplication implements CommandLineRunner {
    @Autowired
    private SysGatewayRouteService sysGatewayRouteService;

    @Autowired
    private SwaggerProvider swaggerProvider;

    @Autowired
    private UpdateHttpAccessLogConfigListener updateHttpAccessLogConfigListener;

    public static void main(String[] args) {
        SpringApplication.run(MapGatewayApplication.class, args);
    }

    @Override
    public void run(String... args) {
        sysGatewayRouteService.refresh(null);
        swaggerProvider.refreshApis();
        updateHttpAccessLogConfigListener.onMessage(null);
    }
}
