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
import com.zondy.mapgis.system.domain.SysGatewayRoute;
import com.zondy.mapgis.system.service.ISysGatewayRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 网关路由Controller
 *
 * @author mapgis
 * @date 2022-09-20
 */
@Tag(name = "网关路由管理", description = "网关路由控制器")
@ManagerRestController("/system/route")
public class SysGatewayRouteController extends BaseController {
    @Autowired
    private ISysGatewayRouteService sysGatewayRouteService;

    /**
     * 查询网关路由列表
     */
    @Operation(summary = "查询网关路由列表")
    @PreAuthorize("@ss.hasPermi('system:route:list')")
    @RequiresPermissions("system:route:list")
    @GetMapping("/list")
    public TableDataInfo list(SysGatewayRoute sysGatewayRoute) {
        startPage();
        List<SysGatewayRoute> list = sysGatewayRouteService.selectSysGatewayRouteList(sysGatewayRoute);
        return getDataTable(list);
    }

    /**
     * 导出网关路由列表
     */
    @Operation(summary = "导出网关路由列表")
    @PreAuthorize("@ss.hasPermi('system:route:export')")
    @RequiresPermissions("system:route:export")
    @Log(title = "网关路由", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGatewayRoute sysGatewayRoute) {
        List<SysGatewayRoute> list = sysGatewayRouteService.selectSysGatewayRouteList(sysGatewayRoute);
        ExcelUtil<SysGatewayRoute> util = new ExcelUtil<SysGatewayRoute>(SysGatewayRoute.class);
        util.exportExcel(response, list, "网关路由数据");
    }

    /**
     * 获取网关路由详细信息
     */
    @Operation(summary = "获取网关路由详细信息")
    @PreAuthorize("@ss.hasPermi('system:route:query')")
    @RequiresPermissions("system:route:query")
    @GetMapping(value = "/{gatewayRouteId}")
    public AjaxResult getInfo(@PathVariable("gatewayRouteId") Long gatewayRouteId) {
        return success(sysGatewayRouteService.selectSysGatewayRouteByGatewayRouteId(gatewayRouteId));
    }

    /**
     * 新增网关路由
     */
    @Operation(summary = "新增网关路由")
    @PreAuthorize("@ss.hasPermi('system:route:add')")
    @RequiresPermissions("system:route:add")
    @Log(title = "网关路由", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGatewayRoute sysGatewayRoute) {
        sysGatewayRoute.setCreateBy(SecurityUtils.getUsername());
        return toAjax(sysGatewayRouteService.insertSysGatewayRoute(sysGatewayRoute));
    }

    /**
     * 修改网关路由
     */
    @Operation(summary = "修改网关路由")
    @PreAuthorize("@ss.hasPermi('system:route:edit')")
    @RequiresPermissions("system:route:edit")
    @Log(title = "网关路由", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGatewayRoute sysGatewayRoute) {
        sysGatewayRoute.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysGatewayRouteService.updateSysGatewayRoute(sysGatewayRoute));
    }

    /**
     * 删除网关路由
     */
    @Operation(summary = "删除网关路由")
    @PreAuthorize("@ss.hasPermi('system:route:remove')")
    @RequiresPermissions("system:route:remove")
    @Log(title = "网关路由", businessType = BusinessType.DELETE)
    @DeleteMapping("/{gatewayRouteIds}")
    public AjaxResult remove(@PathVariable Long[] gatewayRouteIds) {
        return toAjax(sysGatewayRouteService.deleteSysGatewayRouteByGatewayRouteIds(gatewayRouteIds));
    }
}
