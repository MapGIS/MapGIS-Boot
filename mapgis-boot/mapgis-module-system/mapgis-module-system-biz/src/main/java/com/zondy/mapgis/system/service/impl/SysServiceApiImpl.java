package com.zondy.mapgis.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.zondy.mapgis.common.core.constant.ConfigConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.*;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.ISysLogininforService;
import com.zondy.mapgis.system.api.service.ISysOperLogService;
import com.zondy.mapgis.system.api.service.ISysPermissionService;
import com.zondy.mapgis.system.api.service.ISysUserService;
import com.zondy.mapgis.system.service.ISysAuthConfigService;
import com.zondy.mapgis.system.service.ISysAuthUserService;
import com.zondy.mapgis.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service("SysServiceApiImpl")
public class SysServiceApiImpl implements ISysServiceApi {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysAuthUserService authUserService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysLogininforService logininforService;

    @Autowired
    private ISysOperLogService operLogService;

    @Autowired
    private ISysAuthConfigService authConfigService;

    @Override
    public R<LoginUser> getUserInfo(String username, String source) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail(Constants.LOGIN_USER_KEY + username + " 不存在");
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(sysUser.getUserId());
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(sysUser.getUserId());
        LoginUser sysUserVo = new LoginUser();
        sysUserVo.setUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    @Override
    public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
        String username = sysUser.getUserName();
        String registerUserString = configService.selectConfigValueByKey(ConfigConstants.CONFIG_KEY_SECURITY_REGISTER);
        Dict registerUserInfo = JsonUtils.parseMap(registerUserString);
        if (StringUtils.isEmpty(registerUserInfo) || !(Boolean) registerUserInfo.get("enabled")) {
            return R.fail("当前系统没有开启注册功能！");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            return R.fail("保存用户'" + username + "'失败，注册账号已存在");
        }
        boolean registerResult = userService.registerUser(sysUser);

        if (registerResult && StringUtils.isNotEmpty(sysUser.getRoleIds())) {
            userService.insertUserAuth(userService.selectUserByUserName(username).getUserId(), sysUser.getRoleIds());
        }
        return R.ok(registerResult);
    }

    @Override
    public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
        if (operLogService.insertOperlog(sysOperLog) > 0) {
            return R.ok(true);
        } else {
            return R.fail("添加访问日志失败");
        }
    }

    @Override
    public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
        if (logininforService.insertLogininfor(sysLogininfor) > 0) {
            return R.ok(true);
        } else {
            return R.fail("添加访问日志失败");
        }
    }

    @Override
    public R<List<SysAuthUser>> selectAuthUserList(SysAuthUser user, String source) {
        List<SysAuthUser> sysAuthUsers = authUserService.selectAuthUserList(user);

        return R.ok(sysAuthUsers);
    }

    @Override
    public R<SysUser> selectUserByAuthUuid(String uuid, String source) {
        SysUser sysUser = authUserService.selectUserByAuthUuid(uuid);

        return R.ok(sysUser);
    }

    @Override
    public R<Boolean> saveAuthUser(SysAuthUser authUser, String source) {
        if (authUserService.insertAuthUser(authUser) > 0) {
            return R.ok(true);
        } else {
            return R.fail("添加授权用户失败");
        }
    }

    @Override
    public R<Boolean> updateAuthUser(SysAuthUser authUser, String source) {
        if (authUserService.updateAuthUser(authUser) > 0) {
            return R.ok(true);
        } else {
            return R.fail("添加授权用户失败");
        }
    }

    @Override
    public R<String> selectConfigValueByKey(String configKey, String source) {
        return R.ok(configService.selectConfigValueByKey(configKey));
    }

    @Override
    public R<SysAuthConfig> selectAuthConfigByType(String type, String source) {
        final SysAuthConfig sysAuthConfig = authConfigService.selectAuthConfigByType(type);

        return R.ok(sysAuthConfig);
    }
}
