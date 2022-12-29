package com.zondy.mapgis.auth.controller;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.captcha.service.IValidateCodeService;
import com.zondy.mapgis.common.controllerprefix.annotation.ServicesRestController;
import com.zondy.mapgis.common.core.constant.TokenConstants;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.domain.SysLoginConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

/**
 * 授权
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "授权管理", description = "授权控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ServicesRestController("/auth")
public class AuthController {

    private final SysLoginService loginService;

    private final IValidateCodeService validateCodeService;

    private final SysServiceProxy sysServiceProxy;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody);
        ajax.put(TokenConstants.TOKEN, token);
        return ajax;
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        loginService.register(registerBody);
        return AjaxResult.success();
    }

    /**
     * 生成验证码
     */
    @Operation(summary = "生成验证码")
    @GetMapping("/captchaImage")
    public AjaxResult createCaptcha() throws IOException {
        SysLoginConfig sysLoginConfig = sysServiceProxy.getLoginConfig();
        String captchaType = sysLoginConfig.getCaptchaType();

        return validateCodeService.createCaptcha(captchaType);
    }

    /**
     * 是否需要验证码
     */
    @Operation(summary = "是否需要验证码")
    @GetMapping("/isNeedCaptcha/{username}")
    public AjaxResult isNeedCaptcha(@PathVariable("username") String username) {
        AjaxResult ajax = AjaxResult.success();
        boolean isNeedCaptcha = loginService.isNeedCaptcha(username);
        ajax.put("isNeedCaptcha", isNeedCaptcha);
        return ajax;
    }
}
