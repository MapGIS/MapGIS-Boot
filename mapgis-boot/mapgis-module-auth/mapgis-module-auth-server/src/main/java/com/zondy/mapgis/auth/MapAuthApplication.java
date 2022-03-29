package com.zondy.mapgis.auth;

import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证授权中心
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableCustomSwagger2
@EnableMapFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MapAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapAuthApplication.class, args);
    }
}
