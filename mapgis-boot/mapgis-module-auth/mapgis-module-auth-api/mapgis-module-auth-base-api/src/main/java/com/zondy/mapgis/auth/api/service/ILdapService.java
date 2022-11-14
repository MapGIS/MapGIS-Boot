package com.zondy.mapgis.auth.api.service;

import com.zondy.mapgis.system.api.domain.SysLdapConfig;

import java.util.List;

/**
 * LDAP 服务
 *
 * @author xiongbo
 * @since 2022/11/3 17:25
 */
public interface ILdapService {

    /**
     * 认证
     *
     * @param ldapConfig LDAP配置
     * @param username   用户名
     * @param password   密码
     * @return 认证结果
     */
    public boolean authenticate(SysLdapConfig ldapConfig, String username, String password);

    /**
     * 获取LDAP用户所属组
     *
     * @param ldapConfig LDAP配置
     * @param username   用户名
     * @return LDAP用户所属组
     */
    public List<String> getUserGroups(SysLdapConfig ldapConfig, String username);

    /**
     * 获取LDAP所有组
     *
     * @param ldapConfig LDAP配置
     * @return LDAP所有组
     */
    public List<String> getAllGroups(SysLdapConfig ldapConfig);
}
