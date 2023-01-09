package com.zondy.mapgis.common.security.annotation;

import com.zondy.mapgis.common.security.config.ApplicationConfig;
import com.zondy.mapgis.common.security.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// 开启线程异步执行
@EnableAsync
// 自动加载类
@Import({ApplicationConfig.class, FeignAutoConfiguration.class})
public @interface EnableMapConfig {

}
