package com.zondy.mapgis.auth.controller;

import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.core.utils.JwtUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.security.auth.AuthUtil;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
public class LoginController {

    private final SysLoginService loginService;

    @ApiOperation("用户退出")
    @DeleteMapping("logout")
    public AjaxResult logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            loginService.logout(username);
        }
        return AjaxResult.success();
    }
}
