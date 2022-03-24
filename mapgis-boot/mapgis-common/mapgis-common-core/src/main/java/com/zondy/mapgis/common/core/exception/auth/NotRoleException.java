package com.zondy.mapgis.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * 未能通过的角色认证异常
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class NotRoleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotRoleException(String role) {
        super(role);
    }

    public NotRoleException(String[] roles) {
        super(StringUtils.join(roles, ","));
    }
}
