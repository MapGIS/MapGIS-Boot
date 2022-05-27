package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysUser;

import java.util.List;

/**
 * 第三方授权用户表 数据层
 *
 * @author xiongbo
 * @since 2022/5/27 10:00
 */
public interface SysAuthUserMapper {
    /**
     * 根据条件查询授权用户列表
     *
     * @param authUser 授权用户信息
     * @return 授权列表
     */
    public List<SysAuthUser> selectAuthUserList(SysAuthUser authUser);

    /**
     * 根据用户编号查询授权列表
     *
     * @param userId 用户编号
     * @return 授权列表
     */
    public List<SysAuthUser> selectAuthUserListByUserId(Long userId);

    /**
     * 根据uuid查询用户信息
     *
     * @param uuid 唯一信息
     * @return 结果
     */
    public SysUser selectUserByAuthUuid(String uuid);

    /**
     * 新增第三方授权信息
     *
     * @param authUser 授权用户信息
     * @return 结果
     */
    public int insertAuthUser(SysAuthUser authUser);

    /**
     * 更新第三方授权用户信息
     *
     * @param authUser 授权用户信息
     * @return 结果
     */
    public int updateAuthUser(SysAuthUser authUser);

    /**
     * 校验source平台是否绑定
     *
     * @param userId 用户编号
     * @param source 绑定平台
     * @return 结果
     */
    public int checkAuthUser(Long userId, String source);

    /**
     * 根据编号删除第三方授权信息
     *
     * @param authId 授权编号
     * @return 结果
     */
    public int deleteAuthUser(Long authId);
}
