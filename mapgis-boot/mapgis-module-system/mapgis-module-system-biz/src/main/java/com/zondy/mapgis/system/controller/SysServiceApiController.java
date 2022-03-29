package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.security.annotation.InnerAuth;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@RestController
@RequestMapping("/system/api")
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
}
