package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.systemlog.service.ISystemLogService;
import com.zondy.mapgis.system.domain.SysGatewayRoute;
import com.zondy.mapgis.system.service.ISysGatewayRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统日志记录
 *
 * @author xiongbo
 * @since 2022/11/23 10:31
 */
@Tag(name = "系统日志记录管理", description = "系统日志记录控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/systemlog")
public class SysLoginfoController extends BaseController {
    private final ISystemLogService systemLogService;

    private final ISysGatewayRouteService sysGatewayRouteService;

    private final Environment env;

    /**
     * 查询系统日志类别列表
     */
    @Operation(summary = "查询系统日志类别列表")
    @PreAuthorize("@ss.hasPermi('system:systemlog:list')")
    @RequiresPermissions("system:systemlog:list")
    @GetMapping("/getIds")
    public AjaxResult getIds() {
        List<String> ids = new ArrayList<>();

        // 单体版模式
        if (EnvironmentUtil.isSingleServiceMode(env)) {
            ids.add(env.resolvePlaceholders("${mapgis.product.full-name}-server"));
        } else {
            List<SysGatewayRoute> list = sysGatewayRouteService.selectSysGatewayRouteList(new SysGatewayRoute());

            ids = list.stream().map(route -> {
                // remove lb://
                return env.resolvePlaceholders(route.getUri().substring(5));
            }).collect(Collectors.toList());

            // add gateway
            ids.add(0, env.resolvePlaceholders("${mapgis.product.full-name}-gateway-server"));
        }

        return AjaxResult.success(ids);
    }

    /**
     * 查询系统日志记录列表
     */
    @Operation(summary = "查询系统日志记录列表")
    @PreAuthorize("@ss.hasPermi('system:systemlog:list')")
    @RequiresPermissions("system:systemlog:list")
    @GetMapping("/list")
    public AjaxResult getLogs(@RequestParam(name = "logId") String logId,
                              @RequestParam(name = "position", required = false) String position,
                              @RequestParam(name = "count", required = false, defaultValue = "1000") int count,
                              @RequestParam(name = "level", required = false, defaultValue = "ALL") String level,
                              @RequestParam(name = "beginTime", required = false) String beginTime,
                              @RequestParam(name = "endTime", required = false) String endTime,
                              @RequestParam(name = "keyword", required = false) String keyword) {
        return AjaxResult.success(systemLogService.getLogs(logId, position, count, level, beginTime, endTime, keyword));
    }

    /**
     * 导出系统日志
     */
    @Operation(summary = "导出系统日志")
    @PreAuthorize("@ss.hasPermi('system:systemlog:export')")
    @RequiresPermissions("system:systemlog:export")
    @Log(title = "日志管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(name = "logId") String logId) {
        systemLogService.exportLogFilesToZip(response, logId);
    }
}
