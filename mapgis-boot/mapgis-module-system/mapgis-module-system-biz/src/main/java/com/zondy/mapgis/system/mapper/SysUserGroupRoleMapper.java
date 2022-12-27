package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysUserGroupRole;

import java.util.List;

/**
 * 用户组和角色关联Mapper接口
 *
 * @author mapgis
 * @date 2022-12-26
 */
public interface SysUserGroupRoleMapper {
    /**
     * 批量新增用户组和角色关联
     *
     * @param sysUserGroupRoleList 用户组和角色关联
     * @return 结果
     */
    public int batchSysUserGroupRole(List<SysUserGroupRole> sysUserGroupRoleList);

    /**
     * 删除用户组和角色关联
     *
     * @param userGroupId 用户组和角色关联主键
     * @return 结果
     */
    public int deleteSysUserGroupRoleByUserGroupId(Long userGroupId);

    /**
     * 批量删除用户组和角色关联
     *
     * @param userGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserGroupRoleByUserGroupIds(Long[] userGroupIds);
}
