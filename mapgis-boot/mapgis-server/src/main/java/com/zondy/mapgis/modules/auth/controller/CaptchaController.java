package com.zondy.mapgis.modules.auth.controller;

import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.zondy.mapgis.auth.api.domain.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 验证码操作处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Api(value = "授权控制器", tags = {"授权管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/auth")
public class CaptchaController {

    private final ValidateCodeService validateCodeService;

    /**
     * 生成验证码
     */
    @ApiOperation("生成验证码")
    @GetMapping("/captchaImage")
    public AjaxResult createCaptcha() throws IOException {
        return validateCodeService.createCaptcha();
    }
}
