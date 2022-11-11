package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 基础登录服务
 *
 * @author xiongbo
 * @since 2022/11/10 17:29
 */
public abstract class BaseLoginService {
    @Autowired
    private SysServiceProxy sysServiceProxy;

    @Autowired
    private IValidateCodeService validateCodeService;

    @Autowired
    private ILdapService ldapService;

    /**
     * 登录验证
     *
     * @param loginBody 登录对象
     * @return 返回Token
     */
    public String login(LoginBody loginBody) {
        // 登录之前操作
        beforeLogin(loginBody);

        // 用户名和密码
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

        // 用户登录验证
        LoginUser loginUser = null;
        try {
            loginUser = authenticateLogin(username, password);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        // 成功登录之后操作
        afterSuccessLogin(loginUser, username);
        // 生成token
        return createToken(loginUser);
    }

    /**
     * 无密码登录验证，用户第三方用户系统对接，需要根据有效的用户返回Token
     */
    public String login(String username) {
        // 无密码加载用户
        LoginUser loginUser = null;
        try {
            loginUser = loadUserByUsername(username);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        // 成功登录之后操作
        afterSuccessLogin(loginUser, username);
        // 生成token
        return createToken(loginUser);
    }

    /**
     * 登录之前操作
     *
     * @param loginBody 登录对象
     */
    public void beforeLogin(LoginBody loginBody) {

    }

    /**
     * 验证用户名密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录用户
     */
    public abstract LoginUser authenticateLogin(String username, String password);

    /**
     * 加载用户
     *
     * @param username 用户名
     * @return 登录用户
     */
    public abstract LoginUser loadUserByUsername(String username);

    /**
     * 成功登录之后操作
     *
     * @param loginUser 登录用户
     * @param username  用户名
     */
    public abstract void afterSuccessLogin(LoginUser loginUser, String username);

    /**
     * 创建Token
     *
     * @param loginUser 登录用户
     * @return Token
     */
    public abstract String createToken(LoginUser loginUser);

    /**
     * 注册用户
     *
     * @param registerBody 注册对象
     */
    public void register(RegisterBody registerBody) {
        // 注册之前操作
        beforeRegister(registerBody);

        // 用户名和密码
        String username = registerBody.getUsername(), password = registerBody.getPassword();
        Map<String, Object> registrConfig = sysServiceProxy.getRegisterConfig();
        Long[] roleIds = (Long[]) registrConfig.get("defaultRoleIds");

        // 注册
        register(username, password, roleIds);

        // 注册之后操作
        afterSuccessRegister(username);
    }

    /**
     * 注册之前操作
     *
     * @param registerBody 注册对象
     */
    public void beforeRegister(RegisterBody registerBody) {

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
        sysUser.setPassword(getEncryptPassword(password));
        if (!StringUtils.isEmpty(roleIds)) {
            sysUser.setRoleIds(roleIds);
        }

        sysServiceProxy.registerUserInfo(sysUser);
    }

    // 获取加密后的密码
    public abstract String getEncryptPassword(String password);

    /**
     * 成功注册之后操作
     */
    public abstract void afterSuccessRegister(String username);

    /**
     * 校验验证码
     *
     * @param code 验证码
     * @param uuid 生成验证码的UUID
     */
    public void checkCaptcha(String code, String uuid) {
        boolean captchaEnabled = (Boolean) sysServiceProxy.getLoginConfig().get("captchaEnabled");

        // 校验验证码
        if (captchaEnabled) {
            validateCodeService.checkCaptcha(code, uuid);
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     * @param ip     登录IP
     */
    public void recordLoginInfo(Long userId, String ip) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ip);
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysServiceProxy.updateUserProfile(sysUser);
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
