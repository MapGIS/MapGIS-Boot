package com.zondy.mapgis.modules.monitor.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.modules.monitor.service.ServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "服务器监控", tags = {"服务器监控管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/monitor/server")
public class ServerController {

    private final ServerService serverService;

    @ApiOperation("获取服务器监控详细信息")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        return AjaxResult.success(serverService.getServers());
    }
}