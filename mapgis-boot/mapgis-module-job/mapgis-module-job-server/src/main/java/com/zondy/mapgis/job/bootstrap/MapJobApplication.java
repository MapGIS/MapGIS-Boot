package com.zondy.mapgis.job.bootstrap;

import com.zondy.mapgis.common.security.annotation.EnableMapConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import com.zondy.mapgis.common.systemlog.listener.UpdateSystemLogConfigListener;
import com.zondy.mapgis.job.config.ScheduleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * 定时任务
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableMapConfig
@EnableCustomSwagger3
@EnableMapFeignClients
@SpringBootApplication
@Import(ScheduleConfig.class)
public class MapJobApplication implements CommandLineRunner {
    @Autowired
    private UpdateSystemLogConfigListener updateSystemLogConfigListener;

    public static void main(String[] args) {
        SpringApplication.run(MapJobApplication.class, args);
    }

    @Override
    public void run(String... args) {
        updateSystemLogConfigListener.onMessage(null);
    }
}
