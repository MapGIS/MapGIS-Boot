package com.zondy.mapgis.common.core.exception.web;

/**
 * 合法性检验异常
 *
 * @author Chelsea
 * @since 2023/1/10 16:55
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
