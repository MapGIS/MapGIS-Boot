package com.zondy.mapgis.gen;

import com.zondy.mapgis.common.security.annotation.EnableMapConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import com.zondy.mapgis.common.systemlog.listener.UpdateSystemLogConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class MapGenApplication implements CommandLineRunner {
    @Autowired
    private UpdateSystemLogConfigListener updateSystemLogConfigListener;

    public static void main(String[] args) {
        SpringApplication.run(MapGenApplication.class, args);
    }

    @Override
    public void run(String... args) {
        updateSystemLogConfigListener.onMessage(null);
    }
}
