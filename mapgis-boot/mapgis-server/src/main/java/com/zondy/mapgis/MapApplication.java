package com.zondy.mapgis;

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
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, LdapAutoConfiguration.class})
public class MapApplication implements CommandLineRunner {
    @Autowired
    private SysEventPublisher sysEventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(MapApplication.class, args);
    }

    @Override
    public void run(String... args) {
        sysEventPublisher.publishConfigEvent(SysEventConstants.SECURITY_CAS_CONFIG_UPDATE, null);
        sysEventPublisher.publishConfigEvent(SysEventConstants.LOG_SYSTEM_LOG_CONFIG_UPDATE, null);
        sysEventPublisher.publishConfigEvent(SysEventConstants.LOG_HTTP_ACCESS_LOG_CONFIG_UPDATE, null);
    }
}
