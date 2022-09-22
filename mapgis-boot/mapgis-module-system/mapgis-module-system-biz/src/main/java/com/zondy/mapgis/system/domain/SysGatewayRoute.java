package com.zondy.mapgis.system.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 网关路由对象 sys_gateway_route
 *
 * @author mapgis
 * @date 2022-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "网关路由对象")
public class SysGatewayRoute extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 路由编号
     */
    @Schema(description = "路由编号")
    private Long gatewayRouteId;

    /**
     * 路由ID
     */
    @Schema(description = "路由ID")
    @Excel(name = "路由ID")
    private String routeId;

    /**
     * 服务地址
     */
    @Schema(description = "服务地址")
    @Excel(name = "服务地址")
    private String uri;

    /**
     * 断言
     */
    @Schema(description = "断言")
    @Excel(name = "断言")
    private String predicates;

    /**
     * 过滤器
     */
    @Schema(description = "过滤器")
    @Excel(name = "过滤器")
    private String filters;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    @Excel(name = "顺序")
    private Integer orderNum;

    /**
     * 路由状态（0正常 1停用）
     */
    @Schema(description = "路由状态（0正常 1停用）")
    @Excel(name = "路由状态", readConverterExp = "0=正常,1=停用")
    private String status;

}
