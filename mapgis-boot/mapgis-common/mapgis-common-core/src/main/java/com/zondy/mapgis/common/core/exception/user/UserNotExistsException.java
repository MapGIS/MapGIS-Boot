package com.zondy.mapgis.common.core.exception.user;

/**
 * 用户不存在异常类
 *
 * @author xiongbo
 * @since 2023/8/22 19:01
 */
public class UserNotExistsException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserNotExistsException() {
        super("user.not.exists", null);
    }
}
