package com.zondy.mapgis.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Accessors(chain = true)
@Schema(name = "角色和菜单关联")
public class SysRoleMenu {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private Long menuId;
}
