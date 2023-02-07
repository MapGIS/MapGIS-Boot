package com.zondy.mapgis;

import com.zondy.mapgis.config.DataSourceConfig;
import com.zondy.mapgis.system.api.event.SysEventConstants;
import com.zondy.mapgis.system.api.event.SysEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 启动程序
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, LdapAutoConfiguration.class},
        scanBasePackages = {"com.zondy.mapgis.modules"})
@Import({DataSourceConfig.class})
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
