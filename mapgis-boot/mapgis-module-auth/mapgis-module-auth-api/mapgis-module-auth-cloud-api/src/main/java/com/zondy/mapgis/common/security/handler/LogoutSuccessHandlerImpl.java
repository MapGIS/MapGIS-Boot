package com.zondy.mapgis.common.security.handler;

import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.core.constant.HttpStatus;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.security.auth.AuthUtil;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 * 跟common-local-security中同名，为了使CasSecurityConfig在微服务中编译通过，后面微服务全面切换到spring security后，可以被common-local-security中替换
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            AuthUtil.logoutByToken(SecurityUtils.getToken(request));
            // 记录用户退出日志
            loginService.logout(userName);
            ServletUtils.renderString(response, JsonUtils.toJsonString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
            return;
        }
        ServletUtils.renderString(response, JsonUtils.toJsonString(AjaxResult.error(HttpStatus.SUCCESS, "用户不存在")));
    }
}
