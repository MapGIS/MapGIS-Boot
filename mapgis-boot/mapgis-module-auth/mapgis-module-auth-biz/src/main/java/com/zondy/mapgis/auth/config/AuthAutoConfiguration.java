package com.zondy.mapgis.auth.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2022/7/21.
 *
 * @author Chelsea
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.auth.controller", "com.zondy.mapgis.auth.cas"})
public class AuthAutoConfiguration {
}
