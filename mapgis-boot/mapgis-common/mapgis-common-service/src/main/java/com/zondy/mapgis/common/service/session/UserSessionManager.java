package com.zondy.mapgis.common.service.session;

/**
 * 用户状态管理
 *
 * @author Chelsea
 * @since 2023/1/10 14:20
 */
public interface UserSessionManager {

    UserSession get(String id);

    UserSession invalidate(String id);

    /**
     * 统计信息
     */
    String getStatsInfo();

    default void addListener(Listener listener) {
    }

    default void removeListener(Listener listener) {
    }

    interface Listener {
        void onInvalidate(UserSession userSession);
    }
}
