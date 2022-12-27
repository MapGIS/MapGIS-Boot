package com.zondy.mapgis.system.api.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
     * 创建用户的默认用户组
     */
    Long[] defaultUserGroupIds;

    /**
     * 角色映射
     */
    List<SysLdapRoleMappingItem> roleMapping;

    public SysLdapConfig() {
        enabled = false;
        url = "";
        base = "";
        userDn = "";
        password = "";
        defaultUserGroupIds = new Long[0];
        roleMapping = new ArrayList<>();
    }

    @Data
    public static class SysLdapRoleMappingItem {
        /**
         * LDAP群组
         */
        String externalRole;

        /**
         * 映射角色
         */
        Long[] systemRoleIds;

        public SysLdapRoleMappingItem() {
            externalRole = "";
            systemRoleIds = new Long[0];
        }
    }
}
