package com.zondy.mapgis.modules.tool.config;

import cn.hutool.core.util.NumberUtil;
import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.core.constant.OpenApiConstants;
import com.zondy.mapgis.common.core.parser.OpenApiMdParser;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Swagger2的接口配置
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class SwaggerConfig {
    private static final String JWT_SECURITY_SCHEME_NAME = "Authorization";

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
        OpenApiMdParser mdParser = new OpenApiMdParser();
        String des = mdParser.getMdContent("api-docs/info.md");
        des = des != null ? des : description;
        SecurityScheme jwtScheme = new SecurityScheme().name(JWT_SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT");
        return new OpenAPI()
                .info(new Info()
                        // 设置标题
                        .title(title)
                        // 描述
                        .description(des)
                        // 版本
                        .version(version))
                .addSecurityItem(new SecurityRequirement()
                        .addList(JWT_SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(JWT_SECURITY_SCHEME_NAME, jwtScheme)
                );
    }

    @Bean
    public GroupedOpenApi managerApi() {
        return GroupedOpenApi.builder()
                .group("ManagerApi")
                .pathsToMatch(apiPathProperties.getManagerPrefix() + "/**")
                .addOpenApiCustomiser(openApiCustomiser())
                .build();
    }

    @Bean
    public GroupedOpenApi serviceApi() {
        return GroupedOpenApi.builder()
                .group("ServiceApi")
                .pathsToMatch(apiPathProperties.getServicesPrefix() + "/**")
                .addOpenApiCustomiser(openApiCustomiser())
                .build();
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        OpenApiMdParser mdParser = new OpenApiMdParser();
        return openApi -> {
            // 根据X_ORDER扩展属性来实现TAG的排序
            if (openApi.getTags() != null) {
                List<Tag> tags = openApi.getTags().stream().sorted(Comparator.comparingInt(x -> {
                    int order = 1000;
                    if (x.getExtensions() != null) {
                        Object v = x.getExtensions().get(OpenApiConstants.X_ORDER_PROPERTY);
                        if (v != null && NumberUtil.isInteger(v.toString())) {
                            order = Integer.parseInt(v.toString());
                        }
                    }
                    return order;
                })).collect(Collectors.toList());
                tags.forEach(tag -> {
                    tag.setDescription(mdParser.parse(tag.getDescription()));
                });
                openApi.setTags(tags);
            }
            // 对paths的key排序
            Paths paths = openApi.getPaths();
            if (paths != null) {
                HashMap<String, PathItem> copy = new HashMap<>(paths.size());
                copy.putAll(paths);
                paths.clear();
                // 排序之后的key
                copy.keySet().stream().sorted().forEach(t -> {
                    PathItem item = copy.get(t);
                    List<Operation> operations = item.readOperations();
                    operations.forEach(operation -> {
                        operation.setDescription(mdParser.parse(operation.getDescription()));
                        if (operation.getParameters() != null) {
                            operation.getParameters().forEach(parameter -> {
                                parameter.setDescription(mdParser.parse(parameter.getDescription()));
                            });
                        }
                    });
                    paths.addPathItem(t, item);
                });
            }
        };
    }
}
