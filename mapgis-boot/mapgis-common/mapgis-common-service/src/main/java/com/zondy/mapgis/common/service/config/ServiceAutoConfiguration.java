package com.zondy.mapgis.common.service.config;

import com.zondy.mapgis.common.service.session.impl.UserSessionManagerGuavaImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Service自动配置
 *
 * @author xiongbo
 * @since 2023/1/11 14:09
 */
@Configuration
@Import({UserSessionManagerGuavaImpl.class})
public class ServiceAutoConfiguration {
}
