package com.zondy.mapgis.bootstrap;

import com.zondy.mapgis.system.api.event.SysEventConstants;
import com.zondy.mapgis.system.api.event.SysEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;

/**
 * 启动程序
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, LdapAutoConfiguration.class},
        scanBasePackages = {"com.zondy.mapgis.config"})
public class MapApplication implements CommandLineRunner {
    @Autowired
    private SysEventPublisher sysEventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(MapApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String[] events = {
                SysEventConstants.UPDATE_CAS_CONFIG,
                SysEventConstants.UPDATE_SYSTEM_LOG,
                SysEventConstants.UPDATE_HTTP_ACCESS_LOG};

        for (String event : events) {
            sysEventPublisher.publishConfigEvent(event, null);
        }
    }
}
