package com.zondy.mapgis.common.webserver.factory;

import io.undertow.UndertowOptions;
import io.undertow.server.handlers.DisallowedMethodsHandler;
import io.undertow.util.HttpString;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import java.io.File;

/**
 * undertow自定义
 *
 * @author Chelsea
 * @since 2023/1/12 10:12
 */
public class UndertowCustomFactory {
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> createUndertowServletWebServerFactoryCustomizer() {
        return (factory) -> {
            // 禁用trace
            factory.addDeploymentInfoCustomizers(deploymentInfo ->
                    deploymentInfo.addInitialHandlerChainWrapper(handler -> {
                        HttpString[] disallowedHttpMethods = {HttpString.tryFromString("TRACE"), HttpString.tryFromString("TRACK")};
                        return new DisallowedMethodsHandler(handler, disallowedHttpMethods);
                    }));
            // 解决请求中包含'{}[]'字符串时报400报错
            factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, Boolean.TRUE));
            // 屏蔽springboot设置的默认资源目录，避免出现mvc路由重定向，见org.springframework.boot.web.servlet.server.DocumentRoot
            factory.setDocumentRoot(new File("documentRoot"));
        };
    }
}
