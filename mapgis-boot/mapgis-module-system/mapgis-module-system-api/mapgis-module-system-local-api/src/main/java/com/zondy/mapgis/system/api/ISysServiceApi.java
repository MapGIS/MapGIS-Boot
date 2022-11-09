package com.zondy.mapgis.system.api;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.domain.*;
import com.zondy.mapgis.system.api.model.LoginUser;

import java.util.List;

/**
 * 系统服务API，提供其他独立模块调用
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public interface ISysServiceApi {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    public R<LoginUser> getUserInfo(String username, String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    public R<Boolean> registerUserInfo(SysUser sysUser, String source);

    /**
     * 修改用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    public R<Boolean> updateUserProfile(SysUser sysUser, String source);

    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source     请求来源
     * @return 结果
     */
    public R<Boolean> saveLog(SysOperLog sysOperLog, String source);

    /**
     * 保存访问记录
     *
     * @param sysLogininfor 访问实体
     * @param source        请求来源
     * @return 结果
     */
    public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source);

    /**
     * 获取授权用户列表
     *
     * @param user   查询条件
     * @param source 请求来源
     * @return 结果
     */
    public R<List<SysAuthUser>> selectAuthUserList(SysAuthUser user, String source);

    /**
     * 根据uuid查询用户信息
     *
     * @param uuid   唯一信息
     * @param source 请求来源
     * @return 结果
     */
    public R<SysUser> selectUserByAuthUuid(String uuid, String source);

    /**
     * 新增第三方授权信息
     *
     * @param authUser 用户信息
     * @param source   请求来源
     * @return 结果
     */
    public R<Boolean> saveAuthUser(SysAuthUser authUser, String source);

    /**
     * 更新第三方授权用户信息
     *
     * @param authUser 授权用户信息
     * @param source   请求来源
     * @return 结果
     */
    public R<Boolean> updateAuthUser(SysAuthUser authUser, String source);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public R<String> selectConfigValueByKey(String configKey, String source);

    /**
     * 获取第三方登录配置列表
     *
     * @param type   第三方登录平台
     * @param source 请求来源
     * @return 结果
     */
    public R<SysAuthConfig> selectAuthConfigByType(String type, String source);
}
