package com.zondy.mapgis.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Accessors(chain = true)
@Schema(name = "角色和部门关联")
public class SysRoleDept {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;
}
