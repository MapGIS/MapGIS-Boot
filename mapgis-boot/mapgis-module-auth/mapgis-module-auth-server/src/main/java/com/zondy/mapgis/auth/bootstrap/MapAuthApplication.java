package com.zondy.mapgis.auth.bootstrap;

import com.zondy.mapgis.auth.cas.config.CasSecurityConfig;
import com.zondy.mapgis.common.security.annotation.EnableMapFeignClients;
import com.zondy.mapgis.common.swagger.annotation.EnableCustomSwagger3;
import com.zondy.mapgis.common.systemlog.listener.UpdateSystemLogConfigListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.ldap.LdapAutoConfiguration;

/**
 * 认证授权中心
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@EnableCustomSwagger3
@EnableMapFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, LdapAutoConfiguration.class})
public class MapAuthApplication implements CommandLineRunner {
    @Autowired
    private CasSecurityConfig casSecurityConfig;

    @Autowired
    private UpdateSystemLogConfigListener updateSystemLogConfigListener;

    public static void main(String[] args) {
        SpringApplication.run(MapAuthApplication.class, args);
    }

    @Override
    public void run(String... args) {
        casSecurityConfig.updateCasSecurityConfig();
        updateSystemLogConfigListener.onMessage(null);
    }
}