package com.zondy.mapgis.common.service.session.impl;

import com.zondy.mapgis.common.service.session.UserSession;
import com.zondy.mapgis.common.service.session.UserSessionManager;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户状态管理实现类
 *
 * @author xiongbo
 * @since 2023/1/11 15:54
 */
public class UserSessionManagerImpl implements UserSessionManager {
    private final ConcurrentHashMap<String, UserSession> userSessions = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public UserSession get(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        UserSession session = userSessions.get(id);
        if (session != null) {
            updateSessionAccessTime(session);
            return session;
        } else {
            lock.lock();
            try {
                session = userSessions.get(id);
                if (session != null) {
                    updateSessionAccessTime(session);
                    return session;
                } else {
                    session = new UserSessionImpl(this, id);
                    userSessions.put(id, session);
                    updateSessionAccessTime(session);
                    return session;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void updateSessionAccessTime(UserSession userSession) {
        if (userSession instanceof UserSessionImpl) {
            ((UserSessionImpl) userSession).updateAccessTime();
        }
    }

    @Override
    public UserSession invalidate(String id) {
        return userSessions.remove(id);
    }

    @Override
    public String getStatsInfo() {
        return null;
    }
}