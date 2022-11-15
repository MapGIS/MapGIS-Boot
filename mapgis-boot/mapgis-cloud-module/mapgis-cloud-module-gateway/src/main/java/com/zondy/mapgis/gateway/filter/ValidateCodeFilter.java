package com.zondy.mapgis.gateway.filter;

import cn.hutool.core.lang.Dict;
import com.zondy.mapgis.common.captcha.service.IValidateCodeService;
import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.gateway.utils.WebFluxUtils;
import com.zondy.mapgis.system.api.domain.SysLoginConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 验证码过滤器
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
    private static String[] VALIDATE_URL;

    @Autowired
    private IValidateCodeService validateCodeService;

    @Lazy
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Autowired
    private ApiPathProperties apiPathProperties;

    private static final String CODE = "code";

    private static final String UUID = "uuid";

    @PostConstruct
    public void init() {
        String strManagerPrefix = apiPathProperties.getManagerPrefix();

        VALIDATE_URL = new String[]{strManagerPrefix + "/auth/login", strManagerPrefix + "/auth/register"};
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            CompletableFuture<SysLoginConfig> future = CompletableFuture.supplyAsync(() -> sysServiceProxy.getLoginConfig());
            SysLoginConfig sysLoginConfig;
            try {
                sysLoginConfig = future.get();
            } catch (ExecutionException | InterruptedException e) {
                return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }
            boolean captchaEnabled = sysLoginConfig.getCaptchaEnabled();

            // 非登录/注册请求或验证码关闭，不处理
            if (!StringUtils.containsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL) || !captchaEnabled) {
                return chain.filter(exchange);
            }

            try {
                String rspStr = resolveBodyFromRequest(request);
                Dict obj = JsonUtils.parseMap(rspStr);
                validateCodeService.checkCaptcha(obj.getStr(CODE), obj.getStr(UUID));
            } catch (Exception e) {
                return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
            }
            return chain.filter(exchange);
        };
    }

    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        // 获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }
}
