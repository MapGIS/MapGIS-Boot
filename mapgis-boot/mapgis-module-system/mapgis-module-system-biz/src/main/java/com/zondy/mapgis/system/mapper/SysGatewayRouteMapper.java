package com.zondy.mapgis.system.mapper;

import com.zondy.mapgis.system.domain.SysGatewayRoute;

import java.util.List;

/**
 * 网关路由Mapper接口
 *
 * @author mapgis
 * @date 2022-09-20
 */
public interface SysGatewayRouteMapper {
    /**
     * 查询网关路由
     *
     * @param gatewayRouteId 网关路由主键
     * @return 网关路由
     */
    public SysGatewayRoute selectSysGatewayRouteByGatewayRouteId(Long gatewayRouteId);

    /**
     * 查询网关路由列表
     *
     * @param sysGatewayRoute 网关路由
     * @return 网关路由集合
     */
    public List<SysGatewayRoute> selectSysGatewayRouteList(SysGatewayRoute sysGatewayRoute);

    /**
     * 新增网关路由
     *
     * @param sysGatewayRoute 网关路由
     * @return 结果
     */
    public int insertSysGatewayRoute(SysGatewayRoute sysGatewayRoute);

    /**
     * 修改网关路由
     *
     * @param sysGatewayRoute 网关路由
     * @return 结果
     */
    public int updateSysGatewayRoute(SysGatewayRoute sysGatewayRoute);

    /**
     * 删除网关路由
     *
     * @param gatewayRouteId 网关路由主键
     * @return 结果
     */
    public int deleteSysGatewayRouteByGatewayRouteId(Long gatewayRouteId);

    /**
     * 批量删除网关路由
     *
     * @param gatewayRouteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysGatewayRouteByGatewayRouteIds(Long[] gatewayRouteIds);
}
