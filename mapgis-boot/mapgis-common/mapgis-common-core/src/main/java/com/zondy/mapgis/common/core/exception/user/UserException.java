package com.zondy.mapgis.common.core.exception.user;

import com.zondy.mapgis.common.core.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
