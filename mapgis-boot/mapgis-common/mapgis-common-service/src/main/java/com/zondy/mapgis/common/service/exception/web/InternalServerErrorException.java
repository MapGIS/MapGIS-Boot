package com.zondy.mapgis.common.service.exception.web;

import com.zondy.mapgis.common.core.constant.HttpStatus;

/**
 * 服务器内部错误异常
 *
 * @author Chelsea
 * @since 2023/1/10 15:21
 */
public class InternalServerErrorException extends WebApplicationException {
    public InternalServerErrorException() {
        this(null);
    }

    public InternalServerErrorException(final String message) {
        this(message, null);
    }

    public InternalServerErrorException(final String message, final Throwable cause) {
        super(message, HttpStatus.ERROR, cause);
    }
}