package com.zondy.mapgis.common.service.session.impl;

import com.zondy.mapgis.common.service.constant.Constants;
import com.zondy.mapgis.common.service.session.UserSession;
import com.zondy.mapgis.common.service.session.UserSessionManager;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户状态实现类
 *
 * @author Chelsea
 * @since 2023/1/11 15:51
 */
public class UserSessionImpl implements UserSession {
    private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<>(16);
    private UserSessionManager sessionManager;
    private String userId;
    private volatile long lastAccessTime = 0;

    public UserSessionImpl(UserSessionManager sessionManager, String userId) {
        this.userId = userId;
        this.sessionManager = sessionManager;
        updateAccessTime();
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void setAttribute(String name, Object attr) {
        attributes.put(name, attr);
    }

    @Override
    public List<String> getAttributeNames() {
        List<String> names = new ArrayList<>();
        Enumeration<String> enumeration = attributes.keys();
        while (enumeration.hasMoreElements()) {
            names.add(enumeration.nextElement());
        }
        return names;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean isStateless() {
        return Constants.READONLY_USER_ID.equalsIgnoreCase(userId);
    }

    @Override
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * 包内可见
     */
    void updateAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
}
