package com.zondy.mapgis.modules.tool.config;

import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger2的接口配置
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class SwaggerConfig {
    private static final String SECURITY_SCHEME_NAME = "Authorization";

    @Autowired
    private ApiPathProperties apiPathProperties;

    /**
     * 文档标题
     */
    @Value("${swagger.title}")
    private String title;

    /**
     * 文档描述
     */
    @Value("${swagger.description}")
    private String description;

    /**
     * 文档描述
     */
    @Value("${swagger.version}")
    private String version;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        // 设置标题
                        .title(title)
                        // 描述
                        .description(description)
                        // 版本
                        .version("版本号:" + version))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("ManagerApi")
                .pathsToMatch(apiPathProperties.getManagerPrefix() + "/**")
                .build();
    }

    @Bean
    public GroupedOpenApi serviceApi() {
        return GroupedOpenApi.builder()
                .group("ServiceApi")
                .pathsToMatch(apiPathProperties.getServicesPrefix() + "/**")
                .build();
    }
}
