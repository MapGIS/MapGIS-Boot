package com.zondy.mapgis.auth.cas.handler;

import com.zondy.mapgis.auth.cas.model.CasLoginUser;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * CAS认证成功处理类
 *
 * @author powanjuanshu
 * @since 2022/6/7 8:54
 */
@Service
public class CasAuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        clearAuthenticationAttributes(request);

        CasLoginUser casLoginUser = (CasLoginUser) authentication.getPrincipal();

        String token = tokenService.createToken(casLoginUser.getLoginUser());

        // 登录成功后跳转到前端登录页面
        Map<String, Object> casConfig = sysServiceProxy.getCasConfig();

        getRedirectStrategy().sendRedirect(request, response, casConfig.get("casServiceWebUrl") + "?token=" + token + "&");
    }
}