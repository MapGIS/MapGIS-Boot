package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.system.service.ISysServerMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 服务器监控
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "服务器监控管理", description = "服务器监控控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/monitor")
public class SysServerMonitorController {

    private final ISysServerMonitorService serverMonitorService;

    @Operation(summary = "获取服务器监控详细信息")
    @PreAuthorize("@ss.hasPermi('system:server:list')")
    @GetMapping("/server")
    public AjaxResult getServerInfo() throws Exception {
        return AjaxResult.success(serverMonitorService.getMonitorInfo());
    }

    @Operation(summary = "获取指定时间周期内的服务器监控详细信息")
    @PreAuthorize("@ss.hasPermi('system:server:list')")
    @GetMapping("/server/range")
    public AjaxResult getServerInfoBetweenTime(@RequestParam("beginTime") String beginTime, @RequestParam("endTime") String endTime) throws Exception {
        return AjaxResult.success(serverMonitorService.getMonitorInfoBetweenTime(beginTime, endTime));
    }
}