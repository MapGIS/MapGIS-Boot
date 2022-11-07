package com.zondy.mapgis.auth.api.service;

import java.util.List;
import java.util.Map;

/**
 * LDAP 服务
 *
 * @author powanjuanshu
 * @since 2022/11/3 17:25
 */
public interface LdapService {

    /**
     * 认证
     *
     * @param username
     * @param password
     * @return
     */
    public boolean authenticate(Map<String, Object> ldapConfig, String username, String password);

    /**
     * 获取LDAP用户所属组
     *
     * @param userName
     * @return
     */
    public List<String> getUserGroups(Map<String, Object> ldapConfig, String username);

    /**
     * 获取LDAP所有组
     */
    public List<String> getAllGroups(Map<String, Object> ldapConfig);
}
