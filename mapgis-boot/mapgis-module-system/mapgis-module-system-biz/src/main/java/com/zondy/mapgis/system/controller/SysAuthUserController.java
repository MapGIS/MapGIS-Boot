package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 第三方授权用户信息
 *
 * @author xiongbo
 * @since 2022/5/23 19:17
 */
@Tag(name = "授权用户信息管理", description = "授权用户信息控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/authUser")
public class SysAuthUserController extends BaseController {

    private final ISysUserService userService;

    /**
     * 获取当前用户授权信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户授权信息")
    @GetMapping("/info")
    public AjaxResult getInfo() {
        Long userId = SecurityUtils.getUserId();
        List<SysAuthUser> sysAuthUsers = userService.selectAuthUserListByUserId(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("auths", sysAuthUsers);
        return ajax;
    }

    /**
     * 检查指定授权平台来源是否授权
     */
    @Operation(summary = "检查指定授权平台来源是否授权")
    @PostMapping("/check")
    public AjaxResult checkAuthUser(String source) {
        Long userId = SecurityUtils.getUserId();
        if (userService.checkAuthUser(userId, source) > 0) {
            throw new ServiceException("平台账号已经绑定");
        }
        return AjaxResult.success();
    }

    /**
     * 绑定授权,传入{ uuid: xxx }
     */
    @Operation(summary = "绑定授权,传入{ uuid: xxx }")
    @PostMapping("/bind")
    public AjaxResult bindAuth(@RequestBody SysAuthUser authUser) {
        List<SysAuthUser> sysAuthUserList = userService.selectAuthUserList(authUser);
        if (sysAuthUserList.size() == 0) {
            throw new ServiceException("第三方账号不存在");
        }

        authUser = sysAuthUserList.get(0);
        authUser.setUserId(SecurityUtils.getUserId());
        return toAjax(userService.updateAuthUser(authUser));
    }

    /**
     * 取消授权,传入{ authId: xxx}
     */
    @Operation(summary = "取消授权,传入{ authId: xxx}")
    @PostMapping("/unbind")
    public AjaxResult unbindAuth(@RequestBody SysAuthUser authUser) {
        return toAjax(userService.deleteAuthUser(authUser.getAuthId()));
    }
}
