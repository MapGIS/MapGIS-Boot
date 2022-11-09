package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.enums.UserStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.security.service.SysRecordLogService;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
    private SysPasswordService passwordService;

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

        LoginUser loginUser = loadUserByUsername(username);
        SysUser user = loginUser.getUser();

        passwordService.validate(user, password);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(loginUser.getUserId());
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 无密码登录验证
     */
    public String login(String username) {
        LoginUser loginUser = loadUserByUsername(username);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(loginUser.getUserId());
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUserId());
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
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账号长度必须在2到20个字符之间");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在8到16个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        if (!StringUtils.isEmpty(roleIds)) {
            sysUser.setRoleIds(roleIds);
        }
        R<?> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
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

    public LoginUser loadUserByUsername(String username) {
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new ServiceException(Constants.LOGIN_USER_KEY + username + " 不存在");
        }
        LoginUser loginUser = userResult.getData();
        SysUser user = loginUser.getUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete"));
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked"));
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        return loginUser;
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

    public List<SysAuthUser> selectAuthUserList(SysAuthUser user) {
        R<List<SysAuthUser>> sysAuthUserListResult = sysServiceApi.selectAuthUserList(user, SecurityConstants.INNER);

        if (R.FAIL == sysAuthUserListResult.getCode()) {
            throw new ServiceException(sysAuthUserListResult.getMsg());
        }

        return sysAuthUserListResult.getData();
    }

    /**
     * 查看用户是否存在，不存在就创建用户
     */
    public void checkUserExistOrCreate(String username, Map<String, Object> ldapConfig) {
        R<LoginUser> loginUserResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == loginUserResult.getCode()) {
            if (!loginUserResult.getMsg().startsWith(Constants.LOGIN_USER_KEY)) {
                throw new ServiceException(loginUserResult.getMsg());
            } else {
                // 不存在用户
                String password = sysServiceProxy.getInitPasswordConfig();

                // 注册账号
                Long[] roleIds = (Long[]) ldapConfig.get("defaultRoleIds");
                register(username, password, roleIds);
            }
        }
    }
}