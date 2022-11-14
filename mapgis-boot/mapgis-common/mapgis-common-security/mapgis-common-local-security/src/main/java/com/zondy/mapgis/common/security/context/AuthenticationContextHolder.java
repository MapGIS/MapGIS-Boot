package com.zondy.mapgis.common.security.context;

import org.springframework.security.core.Authentication;

/**
 * 身份验证信息
 *
 * @author xiongbo
 * @since 2022/8/16 16:21
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext() {
        return contextHolder.get();
    }

    public static void setContext(Authentication context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}