package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.api.domain.SysUserGroup;

import java.util.List;

/**
 * 用户组Mapper接口
 *
 * @author mapgis
 * @date 2022-12-23
 */
public interface SysUserGroupMapper {
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
     * 根据用户ID查询用户组列表
     *
     * @param userId 用户Id
     * @return 用户组集合
     */
    public List<SysUserGroup> selectSysUserGroupListByUserId(Long userId);

    /**
     * 查询用户所属用用户组
     *
     * @param userName 用户名
     * @return 结果
     */
    public List<SysUserGroup> selectSysUserGroupsByUserName(String userName);

    /**
     * 校验用户组名称是否唯一
     *
     * @param userGroupName 用户组名称
     * @return 结果
     */
    public int checkUserGroupNameUnique(String userGroupName);

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
     * 删除用户组
     *
     * @param userGroupId 用户组主键
     * @return 结果
     */
    public int deleteSysUserGroupByUserGroupId(Long userGroupId);

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserGroupByUserGroupIds(Long[] userGroupIds);
}
