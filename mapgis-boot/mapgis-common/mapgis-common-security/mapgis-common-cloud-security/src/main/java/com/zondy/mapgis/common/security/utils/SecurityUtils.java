package com.zondy.mapgis.common.security.utils;

import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.context.SecurityContextHolder;
import com.zondy.mapgis.system.api.model.LoginUser;

/**
 * 安全服务工具类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class SecurityUtils extends BaseSecurityUtils {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户账号
     */
    public static String getUsername() {
        return SecurityContextHolder.getUserName();
    }

    public static String optUsername() {
        try {
            return getUsername();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }
}
