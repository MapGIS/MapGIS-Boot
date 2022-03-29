package com.zondy.mapgis.system;

import com.zondy.mapgis.common.security.annotation.EnableMapConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableMapConfig
@EnableCustomSwagger2
@EnableMapFeignClients
@SpringBootApplication
public class MapSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapSystemApplication.class, args);
    }
}
