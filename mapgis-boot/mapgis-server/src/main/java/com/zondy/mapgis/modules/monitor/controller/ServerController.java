package com.zondy.mapgis.modules.monitor.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.modules.monitor.service.IServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 服务器监控
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "服务器监控管理", description = "服务器监控")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/monitor/server")
public class ServerController {

    private final IServerService serverService;

    @Operation(summary = "获取服务器监控详细信息")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        return AjaxResult.success(serverService.getServers());
    }
}