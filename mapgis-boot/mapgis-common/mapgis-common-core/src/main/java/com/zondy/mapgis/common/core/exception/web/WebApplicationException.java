package com.zondy.mapgis.common.core.exception.web;

import com.zondy.mapgis.common.core.constant.HttpStatus;
import lombok.Getter;

/**
 * Web服务异常
 *
 * @author Chelsea
 * @since 2023/1/10 14:51
 */
public class WebApplicationException extends RuntimeException {
    @Getter
    private int httpStatus = HttpStatus.ERROR;

    public WebApplicationException() {
    }

    public WebApplicationException(final String message, final int httpStatus) {
        this(message, httpStatus, null);
    }

    public WebApplicationException(final String message, final int httpStatus, final Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}