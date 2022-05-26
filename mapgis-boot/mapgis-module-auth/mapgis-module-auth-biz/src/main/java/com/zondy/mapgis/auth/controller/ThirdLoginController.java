package com.zondy.mapgis.auth.controller;

import com.xkcoding.justauth.AuthRequestFactory;
import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.TokenConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xionbgo
 * @since 2022/5/23 14:56
 */
@Tag(name = "授权管理", description = "授权控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
@RequestMapping("/xxx/rest/services/auth/thirdLogin")
public class ThirdLoginController extends BaseController {

    private final AuthRequestFactory factory;

    private final SysLoginService loginService;

    private final ISysServiceApi sysServiceApi;

    /**
     * 认证授权
     *
     * @param source
     * @param response
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    public void render(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 回调结果
     */
    @RequestMapping("/callback/{source}")
    public String callbackAuth(@PathVariable("source") String source, AuthCallback callback, ModelMap modelMap) {
        AuthRequest authRequest = factory.get(source);
        AuthResponse authResponse = authRequest.login(callback);
        if (authResponse.ok()) {
            AuthUser authUser = (AuthUser) authResponse.getData();

            // 根据uuid获取授权用户
            SysAuthUser querySysAuthUser = new SysAuthUser();
            String uuid = source + authUser.getUuid();

            querySysAuthUser.setUuid(uuid);

            R<List<SysAuthUser>> sysAuthUserListResult = sysServiceApi.selectAuthUserList(querySysAuthUser, SecurityConstants.INNER);

            if (R.FAIL == sysAuthUserListResult.getCode()) {
                throw new ServiceException(sysAuthUserListResult.getMsg());
            }

            List<SysAuthUser> sysAuthUserList = sysAuthUserListResult.getData();

            if (sysAuthUserList.size() == 0) {
                //否则直接创建新账号
                SysAuthUser newSysAuthUser = new SysAuthUser();
                newSysAuthUser.setAvatar(authUser.getAvatar());
                newSysAuthUser.setUuid(uuid);
                newSysAuthUser.setUserName(authUser.getNickname());
                newSysAuthUser.setLoginName(authUser.getUsername());
                newSysAuthUser.setEmail(authUser.getEmail());
                newSysAuthUser.setSource(source);

                sysServiceApi.saveAuthUser(newSysAuthUser, SecurityConstants.INNER);

                // 再根据uuid获取授权用户，为了拿到authId
                sysAuthUserListResult = sysServiceApi.selectAuthUserList(querySysAuthUser, SecurityConstants.INNER);

                if (R.FAIL == sysAuthUserListResult.getCode()) {
                    throw new ServiceException(sysAuthUserListResult.getMsg());
                }
                sysAuthUserList = sysAuthUserListResult.getData();

                if (sysAuthUserList.size() == 0) {
                    throw new ServiceException("授权账号添加失败");
                }
            }

            SysAuthUser sysAuthUser = sysAuthUserList.get(0);

            if (StringUtils.isNull(sysAuthUser.getUserId())) {
                // 提示需要进行绑定
                modelMap.addAttribute("thirdLoginModel", JsonUtils.toJsonString(sysAuthUser));
            } else {
                // 直接进行无密码登录
                R<SysUser> userResult = sysServiceApi.selectUserByAuthUuid(uuid, SecurityConstants.INNER);

                if (R.FAIL == userResult.getCode()) {
                    throw new ServiceException(userResult.getMsg());
                }

                SysUser sysUser = userResult.getData();

                String token = loginService.login(sysUser.getUserName());
                modelMap.addAttribute(TokenConstants.TOKEN, token);
            }
        } else {
            modelMap.addAttribute("token", "登录失败");
        }

        return "thirdLogin";
    }

    /**
     * 第三方登录
     *
     * @param token
     * @param source
     * @return
     */
    @GetMapping("/getLoginUser/{token}/{source}")
    @ResponseBody
    public AjaxResult getThirdLoginUser(@PathVariable("token") String token, @PathVariable("source") String source) {
        LoginUser loginUser = loginService.getLoginUser(token);
        SysUser sysUser = loginUser.getUser();

        // 根据uuid获取授权用户
        SysAuthUser querySysAuthUser = new SysAuthUser();

        querySysAuthUser.setSource(source);
        querySysAuthUser.setUserId(sysUser.getUserId());

        R<List<SysAuthUser>> sysAuthUserListResult = sysServiceApi.selectAuthUserList(querySysAuthUser, SecurityConstants.INNER);

        if (R.FAIL == sysAuthUserListResult.getCode()) {
            throw new ServiceException(sysAuthUserListResult.getMsg());
        }

        List<SysAuthUser> sysAuthUserList = sysAuthUserListResult.getData();

        if (sysAuthUserList.size() == 0) {
            throw new ServiceException("授权账号获取失败");
        }
        SysAuthUser sysAuthUser = sysAuthUserList.get(0);

        if (StringUtils.isEmpty(sysUser.getNickName())) {
            sysUser.setNickName(sysAuthUser.getUserName());
        }
        if (StringUtils.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(sysAuthUser.getAvatar());
        }

        // 刷新用户
        loginService.refreshToken(loginUser);

        AjaxResult ajax = AjaxResult.success();
        ajax.put(TokenConstants.TOKEN, token);

        return ajax;
    }
}
