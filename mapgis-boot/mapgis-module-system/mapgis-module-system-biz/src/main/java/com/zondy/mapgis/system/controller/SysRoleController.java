package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.constant.UserConstants;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.poi.ExcelUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.security.service.TokenService;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.domain.SysDept;
import com.zondy.mapgis.system.api.domain.SysRole;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.domain.SysUserRole;
import com.zondy.mapgis.system.api.model.LoginUser;
import com.zondy.mapgis.system.api.service.ISysPermissionService;
import com.zondy.mapgis.system.api.service.ISysRoleService;
import com.zondy.mapgis.system.api.service.ISysUserService;
import com.zondy.mapgis.system.service.ISysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "角色信息管理", description = "角色信息控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService roleService;

    private final TokenService tokenService;

    private final ISysPermissionService permissionService;

    private final ISysUserService userService;

    private final ISysDeptService deptService;

    @Operation(summary = "查询角色信息列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @RequiresPermissions("system:role:list")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Operation(summary = "导出角色信息列表")
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @RequiresPermissions("system:role:export")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @Operation(summary = "根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @RequiresPermissions("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(SecurityUtils.getUsername());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改保存角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SecurityUtils.getUsername());

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @Operation(summary = "修改保存数据权限")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @RequiresPermissions("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @Operation(summary = "获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @RequiresPermissions("system:role:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    @Operation(summary = "查询已分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @Operation(summary = "查询未分配用户角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @Operation(summary = "取消授权用户")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     */
    @Operation(summary = "批量取消授权用户")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     */
    @Operation(summary = "批量选择用户授权")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @RequiresPermissions("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     */
    @Operation(summary = "获取对应角色部门树列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @RequiresPermissions("system:role:query")
    @GetMapping(value = "/deptTree/{roleId}")
    public AjaxResult deptTree(@PathVariable("roleId") Long roleId) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
