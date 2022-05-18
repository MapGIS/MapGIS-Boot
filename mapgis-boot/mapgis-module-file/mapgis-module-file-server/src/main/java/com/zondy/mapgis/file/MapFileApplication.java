package com.zondy.mapgis.file;

import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
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
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MapFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapFileApplication.class, args);
    }
}
