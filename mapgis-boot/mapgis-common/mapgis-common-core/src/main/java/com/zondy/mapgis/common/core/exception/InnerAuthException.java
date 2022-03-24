package com.zondy.mapgis.common.core.exception;

/**
 * 内部认证异常
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class InnerAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message) {
        super(message);
    }
}
