package com.zondy.mapgis.common.service.session;

import java.util.List;

/**
 * 用户状态
 *
 * @author Chelsea
 * @since 2023/1/10 14:20
 */
public interface UserSession {
    /**
     * 获取用户id
     *
     * @return
     */
    String getId();

    /**
     * 移除指定属性
     *
     * @param name 属性名
     */
    void removeAttribute(String name);

    /**
     * 添加一个属性
     *
     * @param name 属性名
     * @param attr 属性值
     */
    void setAttribute(String name, Object attr);

    /**
     * 获取所有的属性名
     *
     * @return 属性名列表
     */
    List<String> getAttributeNames();

    /**
     * 获取指定属性
     *
     * @param name 属性名
     * @return 属性值
     */
    Object getAttribute(String name);

    /**
     * 该用户是否为无状态用户，可通过特殊的GUID来显式告诉服务器该请求为无状态
     *
     * @return 是否为无状态的结果
     */
    boolean isStateless();

    /**
     * 上一次调用的时间
     *
     * @return 调用时间，ms
     */
    long getLastAccessTime();
}
