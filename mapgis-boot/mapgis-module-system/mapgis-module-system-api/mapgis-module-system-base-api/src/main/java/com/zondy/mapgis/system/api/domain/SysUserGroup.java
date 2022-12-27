package com.zondy.mapgis.system.api.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.annotation.Excel.ColumnType;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import com.zondy.mapgis.common.core.xss.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户组对象 sys_user_group
 *
 * @author mapgis
 * @date 2022-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "用户组对象")
public class SysUserGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户组ID
     */
    @Schema(description = "用户组ID")
    @Excel(name = "用户组序号", cellType = ColumnType.NUMERIC, prompt = "用户组编号")
    private Long userGroupId;

    /**
     * 用户组名称
     */
    @Schema(description = "用户组名称")
    @Excel(name = "用户组名称")
    @Xss(message = "用户组名称不能包含脚本字符")
    @NotBlank(message = "用户组名称不能为空")
    @Size(min = 0, max = 30, message = "用户组名称长度不能超过30个字符")
    private String userGroupName;

    /**
     * 角色对象
     */
    @Schema(description = "角色对象")
    private List<SysRole> roles;

    /**
     * 用户对象
     */
    @Schema(description = "用户对象")
    private List<SysUser> users;

    /**
     * 角色组
     */
    @Schema(description = "角色组")
    private Long[] roleIds;

    /**
     * 用户组
     */
    @Schema(description = "用户组")
    private Long[] userIds;
}