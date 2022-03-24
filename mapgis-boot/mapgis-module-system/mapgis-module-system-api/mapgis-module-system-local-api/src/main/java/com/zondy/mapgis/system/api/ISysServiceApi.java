package com.zondy.mapgis.system.api;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;

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
}
