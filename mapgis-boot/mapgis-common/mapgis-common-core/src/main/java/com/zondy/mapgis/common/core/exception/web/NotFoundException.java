package com.zondy.mapgis.common.core.exception.web;

import com.zondy.mapgis.common.core.constant.HttpStatus;

/**
 * 资源未找到异常
 *
 * @author Chelsea
 * @since 2023/1/10 15:26
 */
public class NotFoundException extends WebApplicationException {
    public NotFoundException() {
        this(null);
    }

    public NotFoundException(final String message) {
        this(message, null);
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, cause);
    }
}