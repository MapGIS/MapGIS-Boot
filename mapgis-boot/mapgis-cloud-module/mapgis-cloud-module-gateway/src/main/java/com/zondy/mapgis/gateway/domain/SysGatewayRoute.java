package com.zondy.mapgis.gateway.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * 网关路由对象
 *
 * @author xiongbo
 * @since 2022/9/21 13:49
 */
@Setter
@Getter
public class SysGatewayRoute extends RouteDefinition {
    /**
     * 路由状态（0正常 1停用）
     */
    private String status;
}
