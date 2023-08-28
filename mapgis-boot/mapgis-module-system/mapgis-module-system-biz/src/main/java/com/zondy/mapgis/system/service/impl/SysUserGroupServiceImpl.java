package com.zondy.mapgis.system.service.impl;

import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.api.domain.SysUserGroup;
import com.zondy.mapgis.system.domain.SysUserGroupRole;
import com.zondy.mapgis.system.domain.SysUserGroupUser;
import com.zondy.mapgis.system.mapper.SysUserGroupMapper;
import com.zondy.mapgis.system.mapper.SysUserGroupRoleMapper;
import com.zondy.mapgis.system.mapper.SysUserGroupUserMapper;
import com.zondy.mapgis.system.service.ISysUserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户组Service业务层处理
 *
 * @author mapgis
 * @date 2022-12-23
 */
@Service
public class SysUserGroupServiceImpl implements ISysUserGroupService {
    @Autowired
    private SysUserGroupMapper sysUserGroupMapper;

    @Autowired
    private SysUserGroupUserMapper sysUserGroupUserMapper;

    @Autowired
    private SysUserGroupRoleMapper sysUserGroupRoleMapper;

    /**
     * 查询所有用户组
     *
     * @return 用户组列表
     */
    @Override
    public List<SysUserGroup> selectSysUserGroupAll() {
        return selectSysUserGroupList(new SysUserGroup());
    }

    /**
     * 查询用户组
     *
     * @param userGroupId 用户组主键
     * @return 用户组
     */
    @Override
    public SysUserGroup selectSysUserGroupByUserGroupId(Long userGroupId) {
        return sysUserGroupMapper.selectSysUserGroupByUserGroupId(userGroupId);
    }

    /**
     * 查询用户组列表
     *
     * @param sysUserGroup 用户组
     * @return 用户组
     */
    @Override
    public List<SysUserGroup> selectSysUserGroupList(SysUserGroup sysUserGroup) {
        return sysUserGroupMapper.selectSysUserGroupList(sysUserGroup);
    }

    /**
     * 校验用户组名称是否唯一
     *
     * @param userGroupName 用户组名称
     * @return 结果
     */
    @Override
    public boolean checkUserGroupNameUnique(String userGroupName) {
        int count = sysUserGroupMapper.checkUserGroupNameUnique(userGroupName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    @Override
    public int insertSysUserGroup(SysUserGroup sysUserGroup) {
        // 新增用户组信息
        int rows = sysUserGroupMapper.insertSysUserGroup(sysUserGroup);
        // 新增用户组与用户关联
        insertUserGroupUser(sysUserGroup);
        // 新增用户组与与角色关联
        insertUserGroupRole(sysUserGroup);
        return rows;
    }

    /**
     * 修改用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    @Override
    public int updateSysUserGroup(SysUserGroup sysUserGroup) {
        Long userGroupId = sysUserGroup.getUserGroupId();
        // 删除用户组与用户关联
        sysUserGroupUserMapper.deleteSysUserGroupUserByUserGroupId(userGroupId);
        // 新增用户组与用户关联
        insertUserGroupUser(sysUserGroup);
        // 删除用户组与角色关联
        sysUserGroupRoleMapper.deleteSysUserGroupRoleByUserGroupId(userGroupId);
        // 新增用户组与角色关联
        insertUserGroupRole(sysUserGroup);
        return sysUserGroupMapper.updateSysUserGroup(sysUserGroup);
    }

    /**
     * 批量删除用户组
     *
     * @param userGroupIds 需要删除的用户组主键
     * @return 结果
     */
    @Override
    public int deleteSysUserGroupByUserGroupIds(Long[] userGroupIds) {
        // 删除用户组与用户关联
        sysUserGroupUserMapper.deleteSysUserGroupUserByUserGroupIds(userGroupIds);
        // 删除用户组与角色关联
        sysUserGroupRoleMapper.deleteSysUserGroupRoleByUserGroupIds(userGroupIds);
        return sysUserGroupMapper.deleteSysUserGroupByUserGroupIds(userGroupIds);
    }

    /**
     * 删除用户组信息
     *
     * @param userGroupId 用户组主键
     * @return 结果
     */
    @Override
    public int deleteSysUserGroupByUserGroupId(Long userGroupId) {
        // 删除用户组与用户关联
        sysUserGroupUserMapper.deleteSysUserGroupUserByUserGroupId(userGroupId);
        // 删除用户组与角色关联
        sysUserGroupRoleMapper.deleteSysUserGroupRoleByUserGroupId(userGroupId);
        return sysUserGroupMapper.deleteSysUserGroupByUserGroupId(userGroupId);
    }

    /**
     * 新增用户角组与用户关联
     *
     * @param userGroup 用户组对象
     */
    public void insertUserGroupUser(SysUserGroup userGroup) {
        this.insertUserGroupUser(userGroup.getUserGroupId(), userGroup.getUserIds());
    }

    /**
     * 新增用户角组与角色关联
     *
     * @param userGroup 用户组对象
     */
    public void insertUserGroupRole(SysUserGroup userGroup) {
        this.insertUserGroupRole(userGroup.getUserGroupId(), userGroup.getRoleIds());
    }

    /**
     * 新增用户角组与用户关联
     *
     * @param userGroupId 用户组ID
     * @param userIds     用户组
     */
    public void insertUserGroupUser(Long userGroupId, Long[] userIds) {
        if (StringUtils.isNotEmpty(userIds)) {
            // 新增用户组与用户关联
            List<SysUserGroupUser> list = new ArrayList<SysUserGroupUser>(userIds.length);
            for (Long userId : userIds) {
                SysUserGroupUser ugu = new SysUserGroupUser();
                ugu.setUserGroupId(userGroupId);
                ugu.setUserId(userId);
                list.add(ugu);
            }
            sysUserGroupUserMapper.batchSysUserGroupUser(list);
        }
    }

    /**
     * 新增用户角组与角色关联
     *
     * @param userGroupId 用户组ID
     * @param roleIds     角色组
     */
    public void insertUserGroupRole(Long userGroupId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户组与角色户关联
            List<SysUserGroupRole> list = new ArrayList<SysUserGroupRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserGroupRole ugr = new SysUserGroupRole();
                ugr.setUserGroupId(userGroupId);
                ugr.setRoleId(roleId);
                list.add(ugr);
            }
            sysUserGroupRoleMapper.batchSysUserGroupRole(list);
        }
    }
}
