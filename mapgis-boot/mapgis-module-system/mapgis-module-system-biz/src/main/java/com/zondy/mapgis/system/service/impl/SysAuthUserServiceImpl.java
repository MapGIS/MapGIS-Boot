package com.zondy.mapgis.system.service.impl;

import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.mapper.SysAuthUserMapper;
import com.zondy.mapgis.system.service.ISysAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 第三方授权用户 业务层处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class SysAuthUserServiceImpl implements ISysAuthUserService {

    @Autowired
    private SysAuthUserMapper authUserMapper;

    /**
     * 根据条件分页查询授权用户列表
     *
     * @param authUser 授权用户信息
     * @return 授权列表
     */
    @Override
    public List<SysAuthUser> selectAuthUserList(SysAuthUser authUser) {
        return authUserMapper.selectAuthUserList(authUser);
    }

    /**
     * 根据用户编号查询授权列表
     *
     * @param userId 用户编号
     * @return 授权列表
     */
    @Override
    public List<SysAuthUser> selectAuthUserListByUserId(Long userId) {
        return authUserMapper.selectAuthUserListByUserId(userId);
    }

    /**
     * 根据uuid查询用户信息
     *
     * @param uuid 唯一信息
     * @return 结果
     */
    @Override
    public SysUser selectUserByAuthUuid(String uuid) {
        return authUserMapper.selectUserByAuthUuid(uuid);
    }

    /**
     * 新增第三方授权信息
     *
     * @param authUser 授权用户信息
     * @return 结果
     */
    @Override
    public int insertAuthUser(SysAuthUser authUser) {
        return authUserMapper.insertAuthUser(authUser);
    }

    /**
     * 更新第三方授权用户信息
     *
     * @param authUser 授权用户信息
     * @return 结果
     */
    @Override
    public int updateAuthUser(SysAuthUser authUser) {
        return authUserMapper.updateAuthUser(authUser);
    }

    /**
     * 校验source平台是否绑定
     *
     * @param userId 用户编号
     * @param source 绑定平台
     * @return 结果
     */
    @Override
    public int checkAuthUser(Long userId, String source) {
        return authUserMapper.checkAuthUser(userId, source);
    }

    /**
     * 根据编号删除第三方授权信息
     *
     * @param authId 授权编号
     * @return 结果
     */
    @Override
    public int deleteAuthUser(Long authId) {
        return authUserMapper.deleteAuthUser(authId);
    }
}
