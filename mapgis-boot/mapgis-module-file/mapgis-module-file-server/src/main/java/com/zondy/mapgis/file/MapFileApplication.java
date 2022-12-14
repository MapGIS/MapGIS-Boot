package com.zondy.mapgis.file;

import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import com.zondy.mapgis.common.systemlog.listener.UpdateSystemLogConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 文件服务
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableCustomSwagger3
@EnableMapFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MapFileApplication implements CommandLineRunner {
    @Autowired
    private UpdateSystemLogConfigListener updateSystemLogConfigListener;

    public static void main(String[] args) {
        SpringApplication.run(MapFileApplication.class, args);
    }

    @Override
    public void run(String... args) {
        updateSystemLogConfigListener.onMessage(null);
    }
}
