package com.zondy.mapgis.system.api;

import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.constant.ServiceNameConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.factory.RemoteSysServiceApiFallbackFactory;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 系统服务API，提供其他独立模块调用
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@FeignClient(contextId = "remoteSysServiceApi", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
public interface ISysServiceApi {
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 结果
     */
    @GetMapping("/system/api/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PostMapping("/system/api/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping("/system/api/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 保存访问记录
     *
     * @param sysLogininfor 访问实体
     * @param source        请求来源
     * @return 结果
     */
    @PostMapping("/system/api/logininfor")
    public R<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
