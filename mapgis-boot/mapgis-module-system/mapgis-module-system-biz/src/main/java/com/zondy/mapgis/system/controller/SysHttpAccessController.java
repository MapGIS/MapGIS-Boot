package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.har.utils.HarLogUtils;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import com.zondy.mapgis.system.api.service.ISysHttpAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Http访问日志Controller
 *
 * @author xiongbo
 * @date 2022-11-28
 */
@Tag(name = "Http访问日志管理", description = "Http访问日志控制器")
@ManagerRestController("/system/assesslog")
public class SysHttpAccessController extends BaseController {
    @Autowired
    private ISysHttpAccessService sysHttpAccessService;

    @Autowired
    private Environment env;

    /**
     * 查询Http访问日志列表
     */
    @Operation(summary = "查询Http访问日志列表")
    @PreAuthorize("@ss.hasPermi('system:assesslog:list')")
    @RequiresPermissions("system:assesslog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysHttpAccess sysHttpAccess) {
        startPage();
        List<SysHttpAccess> list = sysHttpAccessService.selectSysHttpAccessList(sysHttpAccess);
        return getDataTable(list);
    }

    /**
     * 导出Http访问日志列表
     */
    @Operation(summary = "导出Http访问日志列表")
    @PreAuthorize("@ss.hasPermi('system:assesslog:export')")
    @RequiresPermissions("system:assesslog:export")
    @Log(title = "Http访问日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysHttpAccess sysHttpAccess) {
        List<SysHttpAccess> list = sysHttpAccessService.selectSysHttpAccessList(sysHttpAccess);
        HarLogUtils.exportHar(response, env.resolvePlaceholders("${mapgis.product.name}"), "", "1.0.0", list);
    }

    /**
     * 获取Http访问日志详细信息
     */
    @Operation(summary = "获取Http访问日志详细信息")
    @PreAuthorize("@ss.hasPermi('system:assesslog:query')")
    @RequiresPermissions("system:assesslog:query")
    @GetMapping(value = "/{accessId}")
    public AjaxResult getInfo(@PathVariable("accessId") Long accessId) {
        return AjaxResult.success(sysHttpAccessService.selectSysHttpAccessByAccessId(accessId));
    }

    /**
     * 删除Http访问日志
     */
    @Operation(summary = "删除Http访问日志")
    @PreAuthorize("@ss.hasPermi('system:assesslog:remove')")
    @RequiresPermissions("system:assesslog:remove")
    @Log(title = "Http访问日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{accessIds}")
    public AjaxResult remove(@PathVariable Long[] accessIds) {
        return toAjax(sysHttpAccessService.deleteSysHttpAccessByAccessIds(accessIds));
    }

    /**
     * 清空Http访问日志
     */
    @Operation(summary = "清空Http访问日志")
    @PreAuthorize("@ss.hasPermi('system:assesslog:remove')")
    @RequiresPermissions("system:assesslog:remove")
    @Log(title = "Http访问日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        sysHttpAccessService.cleanHttpAccess();
        return AjaxResult.success();
    }
}
