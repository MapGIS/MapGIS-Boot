package com.zondy.mapgis.system.api.domain;

import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "部门业务对象")
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID")
    private Long parentId;

    /**
     * 祖级列表
     */
    @Schema(description = "祖级列表")
    private String ancestors;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String deptName;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    @NotNull(message = "显示顺序不能为空")
    private Integer orderNum;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Schema(description = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /**
     * 父部门名称
     */
    @Schema(description = "父部门名称")
    private String parentName;

    /**
     * 子部门
     */
    @Schema(description = "子部门")
    private List<SysDept> children = new ArrayList<SysDept>();
}
