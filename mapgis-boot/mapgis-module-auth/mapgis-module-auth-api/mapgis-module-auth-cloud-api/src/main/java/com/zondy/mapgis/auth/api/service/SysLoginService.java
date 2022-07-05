package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.enums.UserStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /**
     * 登录验证
     */
    public String login(LoginBody loginBody) {
        String username = loginBody.getUsername(), password = loginBody.getPassword();
        LoginUser loginUser = loadUserByUsername(username);
        SysUser user = loginUser.getUser();

        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match"));
            throw new ServiceException("用户不存在/密码错误");
        }
        recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUser().getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 无密码登录验证
     */
    public String login(String username) {
        LoginUser loginUser = loadUserByUsername(username);
        recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        // 踢人
        tokenService.kickoutLoginUser(loginUser.getUser().getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    public void logout(String loginName) {
        recordLogininfor(loginName, Constants.LOGOUT, MessageUtils.message("user.logout.success"));
    }

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername(), password = registerBody.getPassword();

        register(username, password);
    }

    /**
     * 注册
     */
    public void register(String username, String password) {
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
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }

        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLogininfor(String username, String status, String message) {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        sysServiceApi.saveLogininfor(logininfor, SecurityConstants.INNER);
    }

    public LoginUser loadUserByUsername(String username) {
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUser> userResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        LoginUser loginUser = userResult.getData();
        SysUser user = loginUser.getUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete"));
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked"));
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
}