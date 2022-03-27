package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.core.utils.poi.ExcelUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.service.ISysOperLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Api(value = "操作日志记录控制器", tags = {"操作日志记录管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/system/operlog")
public class SysOperlogController extends BaseController {

    private final ISysOperLogService operLogService;

    @ApiOperation("查询操作日志记录列表")
    @PreAuthorize("@ss.hasPermi('system:operlog:list')")
    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @ApiOperation("导出操作日志记录列表")
    @PreAuthorize("@ss.hasPermi('system:operlog:export')")
    @RequiresPermissions("system:operlog:export")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @ApiOperation("删除操作日志记录")
    @PreAuthorize("@ss.hasPermi('system:operlog:remove')")
    @RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @ApiOperation("清空操作日志记录")
    @PreAuthorize("@ss.hasPermi('system:operlog:remove')")
    @RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.cleanOperLog();
        return AjaxResult.success();
    }
}