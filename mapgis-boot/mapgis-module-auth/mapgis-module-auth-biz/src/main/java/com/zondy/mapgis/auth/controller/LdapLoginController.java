package com.zondy.mapgis.auth.controller;

import com.zondy.mapgis.auth.api.service.ILdapService;
import com.zondy.mapgis.common.controllerprefix.annotation.ServicesRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * LDAP用户登录授权
 *
 * @author xiongbo
 * @since 2022/11/12 9:49
 */
@Tag(name = "LDAP登录管理", description = "LDAP登录控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ServicesRestController("/auth/ldapLogin")
public class LdapLoginController {
    private final SysServiceProxy sysServiceProxy;

    private final ILdapService ldapService;

    @Operation(summary = "获取LDAP角色群组")
    @GetMapping("/ldapRoles")
    public AjaxResult getLdapRoles() {
        return AjaxResult.success(ldapService.getAllGroups(sysServiceProxy.getLdapConfig()));
    }
}
