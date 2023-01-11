package com.zondy.mapgis.common.service.session.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;
import com.zondy.mapgis.common.service.constant.Constants;
import com.zondy.mapgis.common.service.session.UserSession;
import com.zondy.mapgis.common.service.session.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 用户状态管理实现类
 *
 * @author xiongbo
 * @since 2023/1/11 15:55
 */
@Slf4j
public class UserSessionManagerGuavaImpl implements UserSessionManager, InitializingBean {
    private LoadingCache<String, UserSession> caches;
    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public UserSession get(String id) {
        if (caches != null && StringUtils.hasText(id)) {
            try {
                return caches.get(id);
            } catch (ExecutionException e) {
                log.warn("获取用户session缓存对象出现异常{}", e.getMessage());
            }
        }
        return null;
    }

    @Override
    public UserSession invalidate(String id) {
        caches.invalidate(id);
        return null;
    }

    @Override
    public String getStatsInfo() {
        return caches.stats().toString() + ",CacheSize:" + caches.size();
    }

    @Override
    public void addListener(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void onRemoval(RemovalNotification<String, UserSession> var1) {
        try {
            for (Listener listener : listeners) {
                listener.onInvalidate(var1.getValue());
            }
            log.info("正在清理用户{}的过期资源", var1.getKey());
        } catch (Exception e) {
            log.warn("清理用户的过期资源时出现异常{}", e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        caches = CacheBuilder.newBuilder().expireAfterAccess(Constants.TEMP_RESOURCE_MAX_IDLE_SECONDS, TimeUnit.SECONDS).recordStats().removalListener(this::onRemoval).build(new CacheLoader<String, UserSession>() {
            @Override
            public UserSession load(String o) {
                return new UserSessionImpl(UserSessionManagerGuavaImpl.this, o);
            }
        });
    }
}
