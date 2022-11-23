package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.systemlog.service.ISystemLogService;
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

    private final Environment env;

    @Operation(summary = "查询系统日志记录列表")
    @PreAuthorize("@ss.hasPermi('system:systemlog:list')")
    @RequiresPermissions("system:systemlog:list")
    @GetMapping("/list")
    public AjaxResult getLogs(@RequestParam(name = "logId", required = false) String logId,
                              @RequestParam(name = "position", required = false) String position,
                              @RequestParam(name = "count", required = false, defaultValue = "1000") int count,
                              @RequestParam(name = "level", required = false) String level,
                              @RequestParam(name = "beginTime", required = false) String beginTime,
                              @RequestParam(name = "endTime", required = false) String endTime,
                              @RequestParam(name = "keyword", required = false) String keyword) {
        if (StringUtils.isEmpty(logId)) {
            logId = env.resolvePlaceholders("${mapgis.product.name}");
        }

        return AjaxResult.success(systemLogService.getLogs(logId, position, count, level, beginTime, endTime, keyword));
    }

    @Log(title = "日志管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:systemlog:export')")
    @RequiresPermissions("system:systemlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response) {
        systemLogService.exportLogFilesToZip(response);
    }
}
