package com.zondy.mapgis.gateway.handler;

import com.zondy.mapgis.common.captcha.service.IValidateCodeService;
import com.zondy.mapgis.common.core.exception.user.CaptchaException;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.domain.SysLoginConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 验证码获取
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class ValidateCodeHandler implements HandlerFunction<ServerResponse> {
    @Autowired
    private IValidateCodeService validateCodeService;

    @Lazy
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        AjaxResult ajax;
        try {
            CompletableFuture<SysLoginConfig> future = CompletableFuture.supplyAsync(() -> sysServiceProxy.getLoginConfig());
            SysLoginConfig sysLoginConfig = future.get();
            boolean captchaEnabled = sysLoginConfig.getCaptchaEnabled();
            String captchaType = sysLoginConfig.getCaptchaType();

            ajax = validateCodeService.createCaptcha(captchaEnabled, captchaType);
        } catch (CaptchaException | IOException | ExecutionException | InterruptedException e) {
            return Mono.error(e);
        }
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(ajax));
    }
}