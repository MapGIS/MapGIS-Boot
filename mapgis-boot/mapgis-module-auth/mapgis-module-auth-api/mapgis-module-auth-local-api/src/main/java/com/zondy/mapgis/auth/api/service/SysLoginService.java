package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.auth.api.domain.model.LoginBody;
import com.zondy.mapgis.auth.api.domain.model.RegisterBody;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.exception.user.UserPasswordNotMatchException;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.common.security.context.AuthenticationContextHolder;
import com.zondy.mapgis.common.security.manager.AsyncManager;
import com.zondy.mapgis.common.security.manager.factory.AsyncFactory;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysServiceApi sysServiceApi;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * ????????????
     */
    public String login(LoginBody loginBody) {
        String username = loginBody.getUsername(), password = loginBody.getPassword();

        // ???????????????
        validateCodeService.checkCaptcha(loginBody.getCode(), loginBody.getUuid());

        // ????????????
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // ?????????????????????UserDetailsServiceImpl.loadUserByUsername
            authentication = SpringUtils.getBean(AuthenticationManager.class)
                    .authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // ??????
        tokenService.kickoutLoginUser(loginUser.getUserId());
        // ??????token
        return tokenService.createToken(loginUser);
    }

    /**
     * ?????????????????????
     */
    public String login(String username) {
        LoginUser loginUser = null;

        try {
            loginUser = loadUserByUsername(username);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        recordLoginInfo(loginUser.getUserId());
        // ??????
        tokenService.kickoutLoginUser(loginUser.getUserId());
        // ??????token
        return tokenService.createToken(loginUser);
    }

    /**
     * ??????
     */
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername(), password = registerBody.getPassword();

        // ???????????????
        validateCodeService.checkCaptcha(registerBody.getCode(), registerBody.getUuid());

        register(username, password);
    }

    /**
     * ??????
     */
    public void register(String username, String password) {
        // ???????????????????????? ??????
        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("??????/??????????????????");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("?????????????????????2???20???????????????");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("?????????????????????5???20???????????????");
        }

        // ??????????????????
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = sysServiceApi.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                MessageUtils.message("user.register.success")));
    }

    /**
     * ??????????????????
     *
     * @param userId ??????ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    public LoginUser loadUserByUsername(String username) {
        return (LoginUser) userDetailsService.loadUserByUsername(username);
    }

    /**
     * ????????????????????????
     *
     * @return ????????????
     */
    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }

    /**
     * ?????????????????????
     *
     * @param loginUser ????????????
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