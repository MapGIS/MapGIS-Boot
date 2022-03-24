package com.zondy.mapgis.modules.monitor.controller;

import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.modules.monitor.controller.domain.Server;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Api(value = "服务器监控", tags = {"服务器监控管理"})
@RestController
@RequestMapping("/monitor/server")
public class ServerController {
    @ApiOperation("获取服务器监控详细信息")
    @PreAuthorize("@ss.hasPermi('monitor:server:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}