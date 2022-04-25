package com.zondy.mapgis.modules.monitor.service;

import java.util.Map;

/**
 * @author xiongbo
 * @since 2022/4/24 17:35
 */
public interface ServerService {
    /**
     * 查询数据分页
     *
     * @return Map<String, Object>
     */
    Map<String, Object> getServers();
}
