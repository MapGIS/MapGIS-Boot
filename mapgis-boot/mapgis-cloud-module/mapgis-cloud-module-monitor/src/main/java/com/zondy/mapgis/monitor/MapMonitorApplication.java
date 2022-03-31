package com.zondy.mapgis.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控中心
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableAdminServer
@SpringBootApplication
public class MapMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapMonitorApplication.class, args);
    }
}
