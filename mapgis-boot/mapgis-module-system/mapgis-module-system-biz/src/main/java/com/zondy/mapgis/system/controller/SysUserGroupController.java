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
import com.zondy.mapgis.system.api.domain.SysUserGroup;
import com.zondy.mapgis.system.service.ISysUserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户组Controller
 *
 * @author mapgis
 * @date 2022-12-23
 */
@Tag(name = "用户组管理", description = "用户组控制器")
@ManagerRestController("/system/usergroup")
public class SysUserGroupController extends BaseController {
    @Autowired
    private ISysUserGroupService sysUserGroupService;

    /**
     * 查询用户组列表
     */
    @Operation(summary = "查询用户组列表")
    @PreAuthorize("@ss.hasPermi('system:usergroup:list')")
    @RequiresPermissions("system:usergroup:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUserGroup sysUserGroup) {
        startPage();
        List<SysUserGroup> list = sysUserGroupService.selectSysUserGroupList(sysUserGroup);
        return getDataTable(list);
    }

    /**
     * 导出用户组列表
     */
    @Operation(summary = "导出用户组列表")
    @PreAuthorize("@ss.hasPermi('system:usergroup:export')")
    @RequiresPermissions("system:usergroup:export")
    @Log(title = "用户组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserGroup sysUserGroup) {
        List<SysUserGroup> list = sysUserGroupService.selectSysUserGroupList(sysUserGroup);
        ExcelUtil<SysUserGroup> util = new ExcelUtil<SysUserGroup>(SysUserGroup.class);
        util.exportExcel(response, list, "用户组数据");
    }

    /**
     * 获取用户组详细信息
     */
    @Operation(summary = "获取用户组详细信息")
    @PreAuthorize("@ss.hasPermi('system:usergroup:query')")
    @RequiresPermissions("system:usergroup:query")
    @GetMapping(value = "/{userGroupId}")
    public AjaxResult getInfo(@PathVariable("userGroupId") Long userGroupId) {
        return success(sysUserGroupService.selectSysUserGroupByUserGroupId(userGroupId));
    }

    /**
     * 新增用户组
     */
    @Operation(summary = "新增用户组")
    @PreAuthorize("@ss.hasPermi('system:usergroup:add')")
    @RequiresPermissions("system:usergroup:add")
    @Log(title = "用户组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserGroup sysUserGroup) {
        if (!sysUserGroupService.checkUserGroupNameUnique(sysUserGroup.getUserGroupName())) {
            return error("新增用户组'" + sysUserGroup.getUserGroupName() + "'失败，用户组名称已存在");
        }
        sysUserGroup.setCreateBy(SecurityUtils.getUsername());
        return toAjax(sysUserGroupService.insertSysUserGroup(sysUserGroup));
    }

    /**
     * 修改用户组
     */
    @Operation(summary = "修改用户组")
    @PreAuthorize("@ss.hasPermi('system:usergroup:edit')")
    @RequiresPermissions("system:usergroup:edit")
    @Log(title = "用户组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserGroup sysUserGroup) {
        sysUserGroup.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(sysUserGroupService.updateSysUserGroup(sysUserGroup));
    }

    /**
     * 删除用户组
     */
    @Operation(summary = "删除用户组")
    @PreAuthorize("@ss.hasPermi('system:usergroup:remove')")
    @RequiresPermissions("system:usergroup:remove")
    @Log(title = "用户组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userGroupIds}")
    public AjaxResult remove(@PathVariable Long[] userGroupIds) {
        return toAjax(sysUserGroupService.deleteSysUserGroupByUserGroupIds(userGroupIds));
    }
}
