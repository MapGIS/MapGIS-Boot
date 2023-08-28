package com.zondy.mapgis.system.service;

import com.zondy.mapgis.system.api.domain.SysUserGroup;

import java.util.List;

/**
 * 用户组Service接口
 *
 * @author mapgis
 * @date 2022-12-23
 */
public interface ISysUserGroupService {
    /**
     * 查询所有用户组
     *
     * @return 用户组列表
     */
    public List<SysUserGroup> selectSysUserGroupAll();

    /**
     * 查询用户组
     *
     * @param userGroupId 用户组主键
     * @return 用户组
     */
    public SysUserGroup selectSysUserGroupByUserGroupId(Long userGroupId);

    /**
     * 查询用户组列表
     *
     * @param sysUserGroup 用户组
     * @return 用户组集合
     */
    public List<SysUserGroup> selectSysUserGroupList(SysUserGroup sysUserGroup);

    /**
     * 校验用户组名称是否唯一
     *
     * @param userGroupName 用户组名称
     * @return 结果
     */
    public boolean checkUserGroupNameUnique(String userGroupName);

    /**
     * 新增用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    public int insertSysUserGroup(SysUserGroup sysUserGroup);

    /**
     * 修改用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    public int updateSysUserGroup(SysUserGroup sysUserGroup);

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 需要删除的用户组主键集合
     * @return 结果
     */
    public int deleteSysUserGroupByUserGroupIds(Long[] userGroupIds);

    /**
     * 删除用户组信息
     *
     * @param userGroupId 用户组主键
     * @return 结果
     */
    public int deleteSysUserGroupByUserGroupId(Long userGroupId);
}
