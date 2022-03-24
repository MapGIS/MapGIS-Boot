package com.zondy.mapgis.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.annotation.Excel.ColumnType;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 系统访问记录表 sys_logininfor
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("系统访问记录业务对象")
public class SysLogininfor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "访问ID")
    @Excel(name = "序号", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    @Excel(name = "用户账号")
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    @ApiModelProperty(value = "登录状态 0成功 1失败")
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /**
     * 登录IP地址
     */
    @ApiModelProperty(value = "登录IP地址")
    @Excel(name = "登录地址")
    private String ipaddr;

    /**
     * 浏览器类型
     */
    @ApiModelProperty(value = "浏览器类型")
    @Excel(name = "浏览器")
    private String browser;

    /**
     * 操作系统
     */
    @ApiModelProperty(value = "操作系统")
    @Excel(name = "操作系统")
    private String os;

    /**
     * 提示消息
     */
    @ApiModelProperty(value = "提示消息")
    @Excel(name = "提示消息")
    private String msg;

    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;
}
