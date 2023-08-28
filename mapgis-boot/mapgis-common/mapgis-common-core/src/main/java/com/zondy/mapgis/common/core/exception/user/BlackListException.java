package com.zondy.mapgis.common.core.exception.user;

/**
 * 黑名单IP异常类
 *
 * @author xiongbo
 * @since 2023/8/22 18:59
 */
public class BlackListException extends UserException {
    private static final long serialVersionUID = 1L;

    public BlackListException() {
        super("login.blocked", null);
    }
}
