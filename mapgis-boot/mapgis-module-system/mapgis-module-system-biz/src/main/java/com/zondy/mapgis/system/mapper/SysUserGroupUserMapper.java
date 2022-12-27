package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysUserGroupUser;

import java.util.List;

/**
 * 用户组和用户关联Mapper接口
 *
 * @author mapgis
 * @date 2022-12-26
 */
public interface SysUserGroupUserMapper {
    /**
     * 批量新增用户组和用户关联
     *
     * @param sysUserGroupUserList 用户组和用户关联
     * @return 结果
     */
    public int batchSysUserGroupUser(List<SysUserGroupUser> sysUserGroupUserList);

    /**
     * 删除用户组和用户关联
     *
     * @param userGroupId 用户组和用户关联主键
     * @return 结果
     */
    public int deleteSysUserGroupUserByUserGroupId(Long userGroupId);

    /**
     * 批量删除用户组和用户关联
     *
     * @param userGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserGroupUserByUserGroupIds(Long[] userGroupIds);

    /**
     * 删除用户组和用户关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteSysUserGroupUserByUserId(Long userId);
}
