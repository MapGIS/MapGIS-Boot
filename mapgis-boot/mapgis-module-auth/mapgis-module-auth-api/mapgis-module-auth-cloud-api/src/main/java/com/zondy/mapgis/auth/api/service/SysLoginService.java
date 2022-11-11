package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.security.service.SysRecordLogService;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.service.UserDetailsService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class SysLoginService extends BaseLoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 验证用户名密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录用户
     */
    @Override
    public LoginUser authenticateLogin(String username, String password) {
        return userDetailsService.loadUserByUsername(username, password);
    }

    /**
     * 加载用户
     *
     * @param username 用户名
     * @return 登录用户
     */
    @Override
    public LoginUser loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username, null);
    }

    /**
     * 成功登录之后操作
     *
     * @param loginUser 登录用户
     * @param username  用户名
     */
    @Override
    public void afterSuccessLogin(LoginUser loginUser, String username) {
        // 记录成功登录日志
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        // 记录用户登录信息
        recordLoginInfo(loginUser.getUserId(), IpUtils.getIpAddr(ServletUtils.getRequest()));
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUserId());
    }

    /**
     * 创建Token
     *
     * @param loginUser 登录用户
     * @return Token
     */
    @Override
    public String createToken(LoginUser loginUser) {
        return tokenService.createToken(loginUser);
    }

    /**
     * 获取加密后的密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String password) {
        return SecurityUtils.encryptPassword(password);
    }

    /**
     * 成功注册之后操作
     */
    @Override
    public void afterSuccessRegister(String username) {
        recordLogService.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        tokenService.refreshToken(loginUser);
    }
}