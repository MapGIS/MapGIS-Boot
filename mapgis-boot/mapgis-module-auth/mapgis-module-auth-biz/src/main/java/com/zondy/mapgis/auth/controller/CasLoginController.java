package com.zondy.mapgis.auth.controller;

import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.constant.TokenConstants;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.model.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * CAS授权
 *
 * @author xiongbo
 * @since 2022/6/9 17:49
 */
@Tag(name = "CAS登录管理", description = "CAS登录控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/auth/casLogin")
public class CasLoginController {

    private final SysLoginService loginService;

    /**
     * 获取CAS登录用户信息
     *
     * @return 结果
     */
    @Operation(summary = "获取CAS登录用户信息")
    @GetMapping("/getLoginUser/{token}")
    public AjaxResult getCasLoginUser(@PathVariable("token") String token) {
        LoginUser loginUser = loginService.getLoginUser(token);

        if (StringUtils.isNotNull(loginUser)) {
            // 刷新用户
            loginService.refreshToken(loginUser);

            AjaxResult ajax = AjaxResult.success();
            ajax.put(TokenConstants.TOKEN, token);

            return ajax;
        }

        return AjaxResult.error("用户不存在");
    }
}
