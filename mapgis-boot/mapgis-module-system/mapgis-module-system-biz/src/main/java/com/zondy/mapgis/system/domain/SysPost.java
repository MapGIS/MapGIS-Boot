package com.zondy.mapgis.system.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.annotation.Excel.ColumnType;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 岗位表 sys_post
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("岗位信息业务对象")
public class SysPost extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 岗位序号
     */
    @ApiModelProperty(value = "岗位序号")
    @Excel(name = "岗位序号", cellType = ColumnType.NUMERIC)
    private Long postId;

    /**
     * 岗位编码
     */
    @ApiModelProperty(value = "岗位编码")
    @Excel(name = "岗位编码")
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    private String postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    @Excel(name = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    private String postName;

    /**
     * 岗位排序
     */
    @ApiModelProperty(value = "岗位排序")
    @Excel(name = "岗位排序")
    @NotBlank(message = "显示顺序不能为空")
    private String postSort;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 用户是否存在此岗位标识 默认不存在
     */
    @ApiModelProperty(value = "用户是否存在此岗位标识 默认不存在")
    private boolean flag = false;
}
