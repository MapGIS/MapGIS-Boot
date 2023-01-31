package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.enums.UserStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.security.context.AuthenticationContextHolder;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.ISysPermissionService;
import com.zondy.mapgis.system.api.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private ISysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new ServiceException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete"));
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked"));
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();

        if (usernamePasswordAuthenticationToken != null) {
            String password = usernamePasswordAuthenticationToken.getCredentials().toString();

            passwordService.validate(user, password);
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
