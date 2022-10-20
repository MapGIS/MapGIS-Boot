package com.zondy.mapgis.modules.auth.controller;

import com.zondy.mapgis.auth.api.service.ValidateCodeService;
import com.zondy.mapgis.common.controllerprefix.annotation.ServicesRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Map;

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

    private final ValidateCodeService validateCodeService;

    private final SysServiceProxy sysServiceProxy;

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码")
    @GetMapping("/captchaImage")
    public AjaxResult createCaptcha() throws IOException {
        Map<String, Object> loginConfig = sysServiceProxy.getLoginConfig();

        boolean captchaEnabled = (Boolean) loginConfig.get("captchaEnabled");
        String captchaType = (String) loginConfig.get("captchaType");

        return validateCodeService.createCaptcha(captchaEnabled, captchaType);
    }
}
