package com.zondy.mapgis.common.core.exception.web;

import com.zondy.mapgis.common.core.constant.HttpStatus;

/**
 * 错误请求异常
 *
 * @author Chelsea
 * @since 2023/1/10 15:24
 */
public class BadRequestException extends WebApplicationException {
    public BadRequestException() {
        this(null);
    }

    public BadRequestException(final String message) {
        this(message, null);
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, cause);
    }
}
