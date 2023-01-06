package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.meter.service.IPerformanceMeterService;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author xiongbo
 * @since 2022/12/5 18:17
 */
@Tag(name = "服务器监控管理", description = "服务器监控控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/monitor")
public class SysPerformanceMonitorController extends BaseController {

    private final IPerformanceMeterService performanceMeterService;

    @Operation(summary = "获取服务器性能监控详细信息")
    @PreAuthorize("@ss.hasPermi('system:serverPerformance:list')")
    @RequiresPermissions("system:serverPerformance:list")
    @GetMapping("/serverPerformance")
    public AjaxResult getServerPerformanceInfo() {
        return AjaxResult.success(performanceMeterService.getPerformanceInfo());
    }
}
