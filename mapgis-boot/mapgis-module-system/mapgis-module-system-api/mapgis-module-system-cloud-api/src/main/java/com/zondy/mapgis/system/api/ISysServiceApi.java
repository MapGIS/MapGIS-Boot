package com.zondy.mapgis.system.api;

import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.factory.RemoteSysServiceApiFallbackFactory;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统服务API，提供其他独立模块调用
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@FeignClient(contextId = "remoteSysServiceApi", value = "${mapgis.product.full-name}-system-server", path = "${api.path.manager-prefix}", fallbackFactory = RemoteSysServiceApiFallbackFactory.class)
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

    /**
     * 获取授权用户列表
     *
     * @param user   查询条件
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/system/api/authUser/list")
    public R<List<SysAuthUser>> selectAuthUserList(@RequestBody SysAuthUser user, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据uuid查询用户信息
     *
     * @param uuid   唯一信息
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/system/api/authUser/user")
    public R<SysUser> selectUserByAuthUuid(@RequestParam("uuid") String uuid, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 新增第三方授权信息
     *
     * @param authUser 用户信息
     * @param source   请求来源
     * @return 结果
     */
    @PostMapping("/system/api/authUser")
    public R<Boolean> saveAuthUser(@RequestBody SysAuthUser authUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 更新第三方授权用户信息
     *
     * @param authUser 授权用户信息
     * @param source   请求来源
     * @return 结果
     */
    @PutMapping("/system/api/authUser")
    public R<Boolean> updateAuthUser(@RequestBody SysAuthUser authUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @param source    请求来源
     * @return 结果
     */
    @GetMapping("/system/api/config/configKey/{configKey}")
    public R<String> selectConfigByKey(@PathVariable("configKey") String configKey, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
