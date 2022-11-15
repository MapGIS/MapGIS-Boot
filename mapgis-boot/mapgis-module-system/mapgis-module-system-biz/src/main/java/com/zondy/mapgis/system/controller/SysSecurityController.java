package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.ldap.utils.LdapUtils;
import com.zondy.mapgis.system.api.domain.SysLdapConfig;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 系统安全
 *
 * @author xiongbo
 * @since 2022/11/14 16:32
 */
@Tag(name = "安全信息管理", description = "安全信息控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/security")
public class SysSecurityController {
    private final SysServiceProxy sysServiceProxy;

    @Operation(summary = "获取LDAP角色群组")
    @GetMapping("/ldap/roles")
    public AjaxResult getLdapRoles() {
        SysLdapConfig ldapConfig = sysServiceProxy.getLdapConfig();

        return AjaxResult.success(LdapUtils.getAllGroups(new LdapUtils.LdapConfig(ldapConfig.getUrl(), ldapConfig.getBase(), ldapConfig.getUserDn(), ldapConfig.getPassword())));
    }
}
