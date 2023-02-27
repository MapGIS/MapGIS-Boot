package com.zondy.mapgis.common.core.exception.user;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class UserPasswordNotMatchException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
        super("user.password.not.match", null);
    }

    public UserPasswordNotMatchException(String[] errorInfo) {
        super("user.password.retry.limit.count", errorInfo);
    }
}
