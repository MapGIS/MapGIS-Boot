package com.zondy.mapgis.gen;

import com.zondy.mapgis.common.security.annotation.EnableMapConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码生成
 *
 * @author xiongbo
 * @since 2022/8/29 11:57
 */
@EnableMapConfig
@EnableCustomSwagger3
@EnableMapFeignClients
@SpringBootApplication
public class MapGenApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapGenApplication.class, args);
    }
}
