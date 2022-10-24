package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.api.domain.SysAuthConfig;
import com.zondy.mapgis.system.service.ISysAuthConfigService;
import com.zondy.mapgis.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * 参数配置 信息操作处理
 *
 * @author powanjuanshu
 * @since 2022/10/17 11:44
 */
@Tag(name = "参数配置管理", description = "参数配置控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/webConfig")
public class SysWebConfigController {
    private final ISysConfigService configService;

    private final ISysAuthConfigService authConfigService;

    /**
     * 获取系统配置信息
     */
    @Operation(summary = "获取系统配置信息")
    @GetMapping(value = "/system")
    public AjaxResult getSystemConfig() {
        Map<String, Object> systemConfig = new LinkedHashMap<>();
        Properties props = System.getProperties();

        systemConfig.put("osName", props.getProperty("os.name"));
        systemConfig.put("osArch", props.getProperty("os.arch"));
        systemConfig.put("javaVersion", props.getProperty("java.version"));
        systemConfig.put("oauthConfig", getAuthConfig());
        return AjaxResult.success(systemConfig);
    }

    /**
     * 获取基本配置信息
     */
    @Operation(summary = "获取基本配置信息")
    @GetMapping(value = "/base")
    public AjaxResult getBaseConfig() {
        return AjaxResult.success().put(AjaxResult.DATA_TAG, configService.selectConfigValueByKey("system.base"));
    }

    private List<Map<String, Object>> getAuthConfig() {
        List<Map<String, Object>> sysAuthConfigVoList = new ArrayList<>();
        SysAuthConfig queryAuthConfig = new SysAuthConfig();
        queryAuthConfig.setStatus("0");

        List<SysAuthConfig> sysAuthConfigList = authConfigService.selectSysAuthConfigList(queryAuthConfig);

        sysAuthConfigList.forEach(sysAuthConfig -> {
            Map<String, Object> sysAuthConfigVo = new LinkedHashMap<>();

            sysAuthConfigVo.put("source", sysAuthConfig.getType());
            sysAuthConfigVo.put("name", sysAuthConfig.getName());
            sysAuthConfigVo.put("icon", sysAuthConfig.getIcon());

            sysAuthConfigVoList.add(sysAuthConfigVo);
        });

        return sysAuthConfigVoList;
    }
}
