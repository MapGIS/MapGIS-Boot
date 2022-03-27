package com.zondy.mapgis.auth;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Api(value = "授权控制器", tags = {"授权管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SysLoginService loginService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @ApiOperation("登录方法")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody) {
        // 用户注册
        loginService.register(registerBody);
        return AjaxResult.success();
    }
}
