package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.security.annotation.InnerAuth;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysAuthUser;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@ManagerRestController("/system/api")
public class SysServiceApiController extends BaseController {

    @Autowired
    @Qualifier("SysServiceApiImpl")
    private ISysServiceApi sysServiceApi;

    /**
     * 获取当前用户信息
     */
    @InnerAuth
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username) {
        return sysServiceApi.getUserInfo(username, "");
    }

    /**
     * 注册用户信息
     */
    @InnerAuth
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser) {
        return sysServiceApi.registerUserInfo(sysUser, "");
    }

    /**
     * 添加访问日志
     */
    @InnerAuth
    @PostMapping("/logininfor")
    public R<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor) {
        return sysServiceApi.saveLogininfor(sysLogininfor, "");
    }

    /**
     * 添加操作日志
     */
    @InnerAuth
    @PostMapping("/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog) {
        return sysServiceApi.saveLog(sysOperLog, "");
    }

    /**
     * 获取授权用户列表
     */
    @InnerAuth
    @PostMapping("/authUser/list")
    public R<List<SysAuthUser>> selectAuthUserList(@RequestBody SysAuthUser user) {
        return sysServiceApi.selectAuthUserList(user, "");
    }

    /**
     * 根据uuid查询用户信息
     */
    @InnerAuth
    @GetMapping("/authUser/user")
    public R<SysUser> selectUserByAuthUuid(String uuid) {
        return sysServiceApi.selectUserByAuthUuid(uuid, "");
    }

    /**
     * 新增第三方授权信息
     */
    @InnerAuth
    @PostMapping("/authUser")
    public R<Boolean> saveAuthUser(@RequestBody SysAuthUser authUser) {
        return sysServiceApi.saveAuthUser(authUser, "");
    }

    /**
     * 更新第三方授权信息
     */
    @InnerAuth
    @PutMapping("/authUser")
    public R<Boolean> updateAuthUser(@RequestBody SysAuthUser authUser) {
        return sysServiceApi.updateAuthUser(authUser, "");
    }

    /**
     * 根据键名查询参数配置信息
     */
    @InnerAuth
    @GetMapping("/config/configKey/{configKey}")
    public R<String> selectConfigValueByKey(@PathVariable("configKey") String configKey) {
        return sysServiceApi.selectConfigValueByKey(configKey, "");
    }
}
