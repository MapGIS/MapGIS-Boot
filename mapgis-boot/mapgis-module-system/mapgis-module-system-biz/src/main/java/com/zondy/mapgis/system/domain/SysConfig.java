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
 * 参数配置表 sys_config
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("参数配置业务对象")
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数主键
     */
    @ApiModelProperty(value = "参数主键")
    @Excel(name = "参数主键", cellType = ColumnType.NUMERIC)
    private Long configId;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    @Excel(name = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
    private String configName;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名")
    @Excel(name = "参数键名")
    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值")
    @Excel(name = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    @ApiModelProperty(value = "系统内置（Y是 N否）")
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;
}
