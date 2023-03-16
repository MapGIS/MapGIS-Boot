package com.zondy.mapgis.common.security.config;

import com.zondy.mapgis.common.security.manager.ShutdownManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created on 2023/2/3.
 *
 * @author Chelsea
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.common.security.config", "com.zondy.mapgis.common.security.handler", "com.zondy.mapgis.common.security.service"})
@Import({ShutdownManager.class})
public class CommonSecurityAutoConfiguration {
}
