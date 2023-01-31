package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.utils.poi.ExcelUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.domain.SysAuthConfig;
import com.zondy.mapgis.system.service.ISysAuthConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 第三方登录配置Controller
 *
 * @author mapgis
 * @date 2022-10-21
 */
@Tag(name = "第三方登录配置管理", description = "第三方登录配置控制器")
@ManagerRestController("/system/authConfig")
public class SysAuthConfigController extends BaseController {
    @Autowired
    private ISysAuthConfigService sysAuthConfigService;

    /**
     * 查询第三方登录配置列表
     */
    @Operation(summary = "查询第三方登录配置列表")
    @PreAuthorize("@ss.hasPermi('system:authConfig:list')")
    @RequiresPermissions("system:authConfig:list")
    @GetMapping("/list")
    public TableDataInfo list(SysAuthConfig sysAuthConfig) {
        startPage();
        List<SysAuthConfig> list = sysAuthConfigService.selectSysAuthConfigList(sysAuthConfig);
        return getDataTable(list);
    }

    /**
     * 导出第三方登录配置列表
     */
    @Operation(summary = "导出第三方登录配置列表")
    @PreAuthorize("@ss.hasPermi('system:authConfig:export')")
    @RequiresPermissions("system:authConfig:export")
    @Log(title = "第三方登录配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAuthConfig sysAuthConfig) {
        List<SysAuthConfig> list = sysAuthConfigService.selectSysAuthConfigList(sysAuthConfig);
        ExcelUtil<SysAuthConfig> util = new ExcelUtil<SysAuthConfig>(SysAuthConfig.class);
        util.exportExcel(response, list, "第三方登录配置数据");
    }

    /**
     * 获取第三方登录配置详细信息
     */
    @Operation(summary = "获取第三方登录配置详细信息")
    @PreAuthorize("@ss.hasPermi('system:authConfig:query')")
    @RequiresPermissions("system:authConfig:query")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Integer configId) {
        return success(sysAuthConfigService.selectSysAuthConfigByConfigId(configId));
    }

    /**
     * 新增第三方登录配置
     */
    @Operation(summary = "新增第三方登录配置")
    @PreAuthorize("@ss.hasPermi('system:authConfig:add')")
    @RequiresPermissions("system:authConfig:add")
    @Log(title = "第三方登录配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAuthConfig sysAuthConfig) {
        if (sysAuthConfigService.isAuthTypeUnique(sysAuthConfig.getType())) {
            return error("新增第三方配置'" + sysAuthConfig.getType() + "'失败，登录平台已存在");
        }
        sysAuthConfig.setCreateBy(SecurityUtils.getUsername());
        return toAjax(sysAuthConfigService.insertSysAuthConfig(sysAuthConfig));
    }

    /**
     * 修改第三方登录配置
     */
    @Operation(summary = "修改第三方登录配置")
    @PreAuthorize("@ss.hasPermi('system:authConfig:edit')")
    @RequiresPermissions("system:authConfig:edit")
    @Log(title = "第三方登录配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAuthConfig sysAuthConfig) {
        sysAuthConfig.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysAuthConfigService.updateSysAuthConfig(sysAuthConfig));
    }

    /**
     * 删除第三方登录配置
     */
    @Operation(summary = "删除第三方登录配置")
    @PreAuthorize("@ss.hasPermi('system:authConfig:remove')")
    @RequiresPermissions("system:authConfig:remove")
    @Log(title = "第三方登录配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Integer[] configIds) {
        return toAjax(sysAuthConfigService.deleteSysAuthConfigByConfigIds(configIds));
    }
}
