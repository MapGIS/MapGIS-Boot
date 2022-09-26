package com.zondy.mapgis.system.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微应用路由对象 sys_micro_app
 *
 * @author mapgis
 * @date 2022-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "微应用路由对象")
public class SysMicroApp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 微应用编号
     */
    @Schema(description = "微应用编号")
    private Long microAppId;

    /**
     * 微应用名称
     */
    @Schema(description = "微应用名称")
    @Excel(name = "微应用名称")
    private String name;

    /**
     * 微应用入口
     */
    @Schema(description = "微应用入口")
    @Excel(name = "微应用入口")
    private String entry;

    /**
     * 微应用路由
     */
    @Schema(description = "微应用路由")
    @Excel(name = "微应用路由")
    private String activeRule;

}
