package com.zondy.mapgis.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MapGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapGatewayApplication.class, args);
    }
}
