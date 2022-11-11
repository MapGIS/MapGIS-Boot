package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * LDAP配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 11:42
 */
@Data
public class SysLdapConfig {
    /**
     * 是否启用
     */
    Boolean enabled;

    /**
     * LDAP服务器地址
     */
    String url;

    /**
     * 根条目位置
     */
    String base;

    /**
     * LDAP管理员名称
     */
    String userDn;

    /**
     * LDAP管理员密码
     */
    String password;

    /**
     * 创建用户的默认角色
     */
    Long[] defaultRoleIds;

    public SysLdapConfig() {
        enabled = false;
        url = "";
        base = "";
        userDn = "";
        password = "";
        defaultRoleIds = new Long[0];
    }
}
