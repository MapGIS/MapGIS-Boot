package com.zondy.mapgis.common.security.handler;

import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.HttpStatus;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.security.service.SysRecordLogService;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private SysRecordLogService recordLogService;

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
            tokenService.delLoginUser(SecurityUtils.getToken(), loginUser.getUser().getUserId());
            // 记录用户退出日志
            recordLogService.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
            ServletUtils.renderString(response, JsonUtils.toJsonString(AjaxResult.error(HttpStatus.SUCCESS, MessageUtils.message("user.logout.success"))));
            return;
        }
        ServletUtils.renderString(response, JsonUtils.toJsonString(AjaxResult.success(MessageUtils.message("user.not.exists"))));
    }
}
