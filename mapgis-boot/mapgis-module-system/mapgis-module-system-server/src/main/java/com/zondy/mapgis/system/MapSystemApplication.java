package com.zondy.mapgis.system;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.security.annotation.EnableMapConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

/**
 * 系统模块
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableMapConfig
@EnableCustomSwagger3
@EnableMapFeignClients
@SpringBootApplication
public class MapSystemApplication implements CommandLineRunner {
    @Autowired
    private ICacheService cacheService;

    public static void main(String[] args) {
        SpringApplication.run(MapSystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        HashMap<String, Object> message = new HashMap<>();

        message.put(Constants.REDIS_LISTENER_NAME, Constants.REFRESH_ROUTES_LISTENER);
        cacheService.convertAndSend(Constants.REDIS_TOPIC_NAME, message);

        message.put(Constants.REDIS_LISTENER_NAME, Constants.UPDATE_CAS_CONFIG_LISTENER);
        cacheService.convertAndSend(Constants.REDIS_TOPIC_NAME, message);
    }
}
