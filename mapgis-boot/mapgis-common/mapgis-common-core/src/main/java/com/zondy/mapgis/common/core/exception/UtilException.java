package com.zondy.mapgis.common.core.exception;

/**
 * 工具类异常
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class UtilException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(Throwable e) {
        super(e.getMessage(), e);
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
