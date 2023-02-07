package com.zondy.mapgis.common.security.utils;

import com.zondy.mapgis.common.core.constant.HttpStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        try {
            return getLoginUser().getDeptId();
        } catch (Exception e) {
            throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户账号
     */
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("获取用户账号异常", HttpStatus.UNAUTHORIZED);
        }
    }

    public static String optUsername() {
        try {
            return getUsername();
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 获取用户
     */
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
