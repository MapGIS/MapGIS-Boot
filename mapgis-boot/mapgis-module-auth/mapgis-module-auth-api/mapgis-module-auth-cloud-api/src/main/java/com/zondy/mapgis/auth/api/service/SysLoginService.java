package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.security.service.SysRecordLogService;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.service.UserDetailsService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 登录校验方法
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class SysLoginService {
    @Autowired
    private ISysServiceApi sysServiceApi;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Autowired
    private ILdapService ldapService;

    /**
     * 登录验证
     */
    public String login(LoginBody loginBody) {
        String username = loginBody.getUsername(), password = loginBody.getPassword();

        // 如果启用了LDAP登录
        Map<String, Object> ldapConfig = sysServiceProxy.getLdapConfig();

        if ((Boolean) ldapConfig.get("enabled")) {
            if (ldapService.authenticate(ldapConfig, username, password)) {
                // 登录成功，查看用户在原有列表中是否存在，存在则直接无密码登录，如果不存在，则需要创建
                // 登录失败，再继续原来的用户验证逻辑
                checkUserExistOrCreate(username, ldapConfig);
                return login(username);
            }
        }

        // 用户验证
        LoginUser loginUser = null;
        try {
            loginUser = loadUserByUsername(username, password);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        // 成功登录之后操作
        afterSuccessLogin(loginUser, username);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 无密码登录验证
     */
    public String login(String username) {
        LoginUser loginUser = null;
        try {
            loginUser = loadUserByUsername(username, null);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        // 成功登录之后操作
        afterSuccessLogin(loginUser, username);
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername(), password = registerBody.getPassword();
        Map<String, Object> registrConfig = sysServiceProxy.getRegisterConfig();
        Long[] roleIds = (Long[]) registrConfig.get("defaultRoleIds");

        register(username, password, roleIds);
    }

    /**
     * 注册
     */
    public void register(String username, String password, Long[] roleIds) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException(MessageUtils.message("user.not.null"));
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException(MessageUtils.message("user.username.not.valid"));
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException(MessageUtils.message("user.password.not.valid"));
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        if (!StringUtils.isEmpty(roleIds)) {
            sysUser.setRoleIds(roleIds);
        }

        sysServiceProxy.registerUserInfo(sysUser);
        recordLogService.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysServiceApi.updateUserProfile(sysUser, SecurityConstants.INNER);
    }

    /**
     * 验证登录用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录用户
     */
    public LoginUser loadUserByUsername(String username, String password) {
        return userDetailsService.loadUserByUsername(username, password);
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

    /**
     * 成功登录之后操作
     *
     * @param loginUser 登录用户
     * @param username  用户名
     */
    public void afterSuccessLogin(LoginUser loginUser, String username) {
        // 记录成功登录日志
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        // 记录用户登录信息
        recordLoginInfo(loginUser.getUserId());
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUserId());
    }

    /**
     * 查看用户是否存在，不存在就创建用户
     */
    public void checkUserExistOrCreate(String username, Map<String, Object> ldapConfig) {
        // 查询用户信息
        LoginUser loginUser = sysServiceProxy.getUserInfo(username);

        if (StringUtils.isNull(loginUser) || StringUtils.isNull(loginUser.getUser())) {
            // 不存在用户
            String password = sysServiceProxy.getInitPasswordConfig();

            // 注册账号
            Long[] roleIds = (Long[]) ldapConfig.get("defaultRoleIds");
            register(username, password, roleIds);
        }
    }
}