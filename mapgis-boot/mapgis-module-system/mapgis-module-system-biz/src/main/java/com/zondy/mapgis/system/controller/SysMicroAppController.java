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
import com.zondy.mapgis.system.domain.SysMicroApp;
import com.zondy.mapgis.system.service.ISysMicroAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 微应用路由Controller
 *
 * @author mapgis
 * @date 2022-09-24
 */
@Tag(name = "微应用路由管理", description = "微应用路由控制器")
@ManagerRestController("/system/microApp")
public class SysMicroAppController extends BaseController {
    @Autowired
    private ISysMicroAppService sysMicroAppService;

    /**
     * 查询微应用路由列表
     */
    @Operation(summary = "查询微应用路由列表")
    @PreAuthorize("@ss.hasPermi('system:microApp:list')")
    @RequiresPermissions("system:microApp:list")
    @GetMapping("/list")
    public TableDataInfo list(SysMicroApp sysMicroApp) {
        startPage();
        List<SysMicroApp> list = sysMicroAppService.selectSysMicroAppList(sysMicroApp);
        return getDataTable(list);
    }

    /**
     * 导出微应用路由列表
     */
    @Operation(summary = "导出微应用路由列表")
    @PreAuthorize("@ss.hasPermi('system:microApp:export')")
    @RequiresPermissions("system:microApp:export")
    @Log(title = "微应用路由", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMicroApp sysMicroApp) {
        List<SysMicroApp> list = sysMicroAppService.selectSysMicroAppList(sysMicroApp);
        ExcelUtil<SysMicroApp> util = new ExcelUtil<SysMicroApp>(SysMicroApp.class);
        util.exportExcel(response, list, "微应用路由数据");
    }

    /**
     * 获取微应用路由详细信息
     */
    @Operation(summary = "获取微应用路由详细信息")
    @PreAuthorize("@ss.hasPermi('system:microApp:query')")
    @RequiresPermissions("system:microApp:query")
    @GetMapping(value = "/{microAppId}")
    public AjaxResult getInfo(@PathVariable("microAppId") Long microAppId) {
        return AjaxResult.success(sysMicroAppService.selectSysMicroAppByMicroAppId(microAppId));
    }

    /**
     * 新增微应用路由
     */
    @Operation(summary = "新增微应用路由")
    @PreAuthorize("@ss.hasPermi('system:microApp:add')")
    @RequiresPermissions("system:microApp:add")
    @Log(title = "微应用路由", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMicroApp sysMicroApp) {
        sysMicroApp.setCreateBy(SecurityUtils.getUsername());
        return toAjax(sysMicroAppService.insertSysMicroApp(sysMicroApp));
    }

    /**
     * 修改微应用路由
     */
    @Operation(summary = "修改微应用路由")
    @PreAuthorize("@ss.hasPermi('system:microApp:edit')")
    @RequiresPermissions("system:microApp:edit")
    @Log(title = "微应用路由", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMicroApp sysMicroApp) {
        sysMicroApp.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysMicroAppService.updateSysMicroApp(sysMicroApp));
    }

    /**
     * 删除微应用路由
     */
    @Operation(summary = "删除微应用路由")
    @PreAuthorize("@ss.hasPermi('system:microApp:remove')")
    @RequiresPermissions("system:microApp:remove")
    @Log(title = "微应用路由", businessType = BusinessType.DELETE)
    @DeleteMapping("/{microAppIds}")
    public AjaxResult remove(@PathVariable Long[] microAppIds) {
        return toAjax(sysMicroAppService.deleteSysMicroAppByMicroAppIds(microAppIds));
    }

    /**
     * 获取微应用信息
     *
     * @return 微应用信息
     */
    @Operation(summary = "获取微应用信息")
    @GetMapping("/getMicroApps")
    public AjaxResult getMicroApps() {
        SysMicroApp sysMicroApp = new SysMicroApp();
        List<SysMicroApp> list = sysMicroAppService.selectSysMicroAppList(sysMicroApp);
        return AjaxResult.success(list);
    }
}
