package com.zondy.mapgis.modules.auth.controller;

import com.zondy.mapgis.common.captcha.service.IValidateCodeService;
import com.zondy.mapgis.common.controllerprefix.annotation.ServicesRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.domain.SysLoginConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * 验证码操作处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "授权管理", description = "授权控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ServicesRestController("/auth")
public class CaptchaController {

    private final IValidateCodeService validateCodeService;

    private final SysServiceProxy sysServiceProxy;

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码")
    @GetMapping("/captchaImage")
    public AjaxResult createCaptcha() throws IOException {
        SysLoginConfig sysLoginConfig = sysServiceProxy.getLoginConfig();
        boolean captchaEnabled = sysLoginConfig.getCaptchaEnabled();
        String captchaType = sysLoginConfig.getCaptchaType();

        return validateCodeService.createCaptcha(captchaEnabled, captchaType);
    }
}
