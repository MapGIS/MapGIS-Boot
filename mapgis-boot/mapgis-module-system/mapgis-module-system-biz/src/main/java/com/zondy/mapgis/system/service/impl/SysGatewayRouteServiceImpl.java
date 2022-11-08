package com.zondy.mapgis.system.service.impl;

import com.zondy.mapgis.common.cache.service.ICacheService;
import com.zondy.mapgis.common.core.constant.CacheConstants;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.domain.SysGatewayRoute;
import com.zondy.mapgis.system.mapper.SysGatewayRouteMapper;
import com.zondy.mapgis.system.service.ISysGatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 网关路由Service业务层处理
 *
 * @author mapgis
 * @date 2022-09-20
 */
@Service
public class SysGatewayRouteServiceImpl implements ISysGatewayRouteService {
    @Autowired
    private SysGatewayRouteMapper sysGatewayRouteMapper;

    @Autowired
    private ICacheService cacheService;

    /**
     * 查询网关路由
     *
     * @param gatewayRouteId 网关路由主键
     * @return 网关路由
     */
    @Override
    public SysGatewayRoute selectSysGatewayRouteByGatewayRouteId(Long gatewayRouteId) {
        return sysGatewayRouteMapper.selectSysGatewayRouteByGatewayRouteId(gatewayRouteId);
    }

    /**
     * 查询网关路由列表
     *
     * @param sysGatewayRoute 网关路由
     * @return 网关路由
     */
    @Override
    public List<SysGatewayRoute> selectSysGatewayRouteList(SysGatewayRoute sysGatewayRoute) {
        return sysGatewayRouteMapper.selectSysGatewayRouteList(sysGatewayRoute);
    }

    /**
     * 新增网关路由
     *
     * @param sysGatewayRoute 网关路由
     * @return 结果
     */
    @Override
    public int insertSysGatewayRoute(SysGatewayRoute sysGatewayRoute) {
        sysGatewayRoute.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isEmpty(sysGatewayRoute.getPredicates())) {
            sysGatewayRoute.setPredicates("[]");
        }
        if (StringUtils.isEmpty(sysGatewayRoute.getFilters())) {
            sysGatewayRoute.setFilters("[]");
        }

        final int result = sysGatewayRouteMapper.insertSysGatewayRoute(sysGatewayRoute);

        refreshRoutes();
        return result;
    }

    /**
     * 修改网关路由
     *
     * @param sysGatewayRoute 网关路由
     * @return 结果
     */
    @Override
    public int updateSysGatewayRoute(SysGatewayRoute sysGatewayRoute) {
        sysGatewayRoute.setUpdateTime(DateUtils.getNowDate());
        if (sysGatewayRoute.getPredicates().isEmpty()) {
            sysGatewayRoute.setPredicates("[]");
        }
        if (sysGatewayRoute.getFilters().isEmpty()) {
            sysGatewayRoute.setFilters("[]");
        }

        final int result = sysGatewayRouteMapper.updateSysGatewayRoute(sysGatewayRoute);

        refreshRoutes();
        return result;
    }

    /**
     * 批量删除网关路由
     *
     * @param gatewayRouteIds 需要删除的网关路由主键
     * @return 结果
     */
    @Override
    public int deleteSysGatewayRouteByGatewayRouteIds(Long[] gatewayRouteIds) {
        final int result = sysGatewayRouteMapper.deleteSysGatewayRouteByGatewayRouteIds(gatewayRouteIds);

        refreshRoutes();
        return result;
    }

    /**
     * 删除网关路由信息
     *
     * @param gatewayRouteId 网关路由主键
     * @return 结果
     */
    @Override
    public int deleteSysGatewayRouteByGatewayRouteId(Long gatewayRouteId) {
        final int result = sysGatewayRouteMapper.deleteSysGatewayRouteByGatewayRouteId(gatewayRouteId);

        refreshRoutes();
        return result;
    }

    /**
     * 刷新路由表缓存
     */
    @Override
    public void refreshRoutesCache() {
        SysGatewayRoute sysGatewayRoute = new SysGatewayRoute();
        final List<SysGatewayRoute> sysGatewayRoutes = sysGatewayRouteMapper.selectSysGatewayRouteList(sysGatewayRoute);

        cacheService.setCacheObject(CacheConstants.SYS_ROUTES_KEY, JsonUtils.toJsonString(sysGatewayRoutes));
    }

    /**
     * 刷新网关路由
     */
    private void refreshRoutes() {
        refreshRoutesCache();

        HashMap<String, Object> message = new HashMap<>();

        message.put(Constants.REDIS_LISTENER_NAME, Constants.REFRESH_ROUTES_LISTENER);
        cacheService.convertAndSend(Constants.REDIS_TOPIC_NAME, message);
    }
}
