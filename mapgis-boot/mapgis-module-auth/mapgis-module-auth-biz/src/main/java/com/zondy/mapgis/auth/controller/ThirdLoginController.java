package com.zondy.mapgis.auth.controller;

import com.xkcoding.justauth.AuthRequestFactory;
import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.auth.domain.vo.AuthUserCheckPasswordVo;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.TokenConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.enums.UserStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xionbgo
 * @since 2022/5/23 14:56
 */
@Tag(name = "第三方登录管理", description = "第三方登录控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
@RequestMapping("${api.path.services-prefix}/auth/thirdLogin")
public class ThirdLoginController extends BaseController {

    private final AuthRequestFactory factory;

    private final SysLoginService loginService;

    private final ISysServiceApi sysServiceApi;

    private final static String LOGIN_ERROR_KEY = "登录用户：";

    /**
     * 第三方登录认证授权
     */
    @Operation(summary = "认证授权")
    @RequestMapping("/render/{source}")
    public void render(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 第三方登录回调结果
     */
    @Operation(summary = "第三方登录回调结果")
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

            List<SysAuthUser> sysAuthUserList = loginService.selectAuthUserList(querySysAuthUser);

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
                sysAuthUserList = loginService.selectAuthUserList(querySysAuthUser);

                if (sysAuthUserList.size() == 0) {
                    throw new ServiceException("授权账号添加失败");
                }
            }

            SysAuthUser sysAuthUser = sysAuthUserList.get(0);

            if (StringUtils.isNull(sysAuthUser.getUserId())) {
                // 没有绑定用户，通过授权loginName去查找同名id
                R<LoginUser> loginUserResult = sysServiceApi.getUserInfo(sysAuthUser.getLoginName(), SecurityConstants.INNER);

                if (R.FAIL == loginUserResult.getCode()) {
                    if (!loginUserResult.getMsg().startsWith(LOGIN_ERROR_KEY)) {
                        throw new ServiceException(loginUserResult.getMsg());
                    }
                } else {
                    // 存在同名用户，将该用户ID跟第三方授权用户关联，前端提示用户绑定
                    SysUser sysUser = loginUserResult.getData().getUser();
                    // 如果该用户没有被删除且没有被禁用才可以绑定
                    if (!UserStatus.DELETED.getCode().equals(sysUser.getDelFlag()) && !UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
                        sysAuthUser.setUserId(sysUser.getUserId());
                    }
                }

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
            modelMap.addAttribute(TokenConstants.TOKEN, "登录失败");
        }

        return "thirdLogin";
    }

    /**
     * 获取第三方登录用户信息
     */
    @Operation(summary = "获取第三方登录用户信息")
    @GetMapping("/getLoginUser/{token}/{source}")
    @ResponseBody
    public AjaxResult getThirdLoginUser(@PathVariable("token") String token, @PathVariable("source") String source) {
        LoginUser loginUser = loginService.getLoginUser(token);
        SysUser sysUser = loginUser.getUser();

        // 根据uuid获取授权用户
        SysAuthUser querySysAuthUser = new SysAuthUser();

        querySysAuthUser.setSource(source);
        querySysAuthUser.setUserId(sysUser.getUserId());

        List<SysAuthUser> sysAuthUserList = loginService.selectAuthUserList(querySysAuthUser);

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

    /**
     * 第三方登录创建新账号
     */
    @Operation(summary = "第三方登录创建新账号")
    @PostMapping("/user/create")
    @ResponseBody
    public AjaxResult thirdUserCreate(@RequestBody SysAuthUser sysAuthUser) {
        // 用户名
        String username = sysAuthUser.getLoginName() + sysAuthUser.getSuffix();

        // 获取初始密码
        R<String> configResult = sysServiceApi.selectConfigValueByKey("security.initPassword", SecurityConstants.INNER);

        if (R.FAIL == configResult.getCode()) {
            throw new ServiceException(configResult.getMsg());
        }

        String password = configResult.getData();

        // 注册账号
        loginService.register(username, password, null);

        // 根据用户名获取用户id
        R<LoginUser> loginUserResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == loginUserResult.getCode()) {
            throw new ServiceException(loginUserResult.getMsg());
        }

        SysUser sysUser = loginUserResult.getData().getUser();
        // 设置用户id到第三方授权用户上
        sysAuthUser.setUserId(sysUser.getUserId());

        // 更新到授权表
        R<Boolean> updateResult = sysServiceApi.updateAuthUser(sysAuthUser, SecurityConstants.INNER);

        if (R.FAIL == updateResult.getCode()) {
            throw new ServiceException(updateResult.getMsg());
        }

        // 进行无密码登录，返回token
        String token = loginService.login(username);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(TokenConstants.TOKEN, token);

        return ajax;
    }

    /**
     * 第三方登录绑定账号,需要验证密码
     */
    @Operation(summary = "第三方登录绑定账号,需要验证密码")
    @PostMapping("/user/checkPassword")
    @ResponseBody
    public AjaxResult checkPwd(@RequestBody AuthUserCheckPasswordVo authUserCheckPasswordVo) {
        // 根据用户名获取用户id
        R<LoginUser> loginUserResult = sysServiceApi.getUserInfo(authUserCheckPasswordVo.getLoginName(), SecurityConstants.INNER);

        if (R.FAIL == loginUserResult.getCode()) {
            throw new ServiceException(loginUserResult.getMsg());
        }

        SysUser sysUser = loginUserResult.getData().getUser();

        String correctPassword = sysUser.getPassword();
        if (!SecurityUtils.matchesPassword(authUserCheckPasswordVo.getPassword(), correctPassword)) {
            return AjaxResult.error("密码不正确");
        }

        // 密码正确进行绑定
        // 绑定之前查找下该三方平台是否已经绑定过该用户
        SysAuthUser querySysAuthUser = new SysAuthUser();

        querySysAuthUser.setSource(authUserCheckPasswordVo.getSource());
        querySysAuthUser.setUserId(authUserCheckPasswordVo.getUserId());

        List<SysAuthUser> sysAuthUserList = loginService.selectAuthUserList(querySysAuthUser);

        if (sysAuthUserList.size() != 0) {
            throw new ServiceException("该账号已经被其他第三方账号绑定");
        }

        // 更新到授权表
        R<Boolean> updateResult = sysServiceApi.updateAuthUser(authUserCheckPasswordVo, SecurityConstants.INNER);

        if (R.FAIL == updateResult.getCode()) {
            throw new ServiceException(updateResult.getMsg());
        }

        // 进行无密码登录，返回token
        String token = loginService.login(authUserCheckPasswordVo.getLoginName());
        AjaxResult ajax = AjaxResult.success();
        ajax.put(TokenConstants.TOKEN, token);

        return ajax;
    }
}
