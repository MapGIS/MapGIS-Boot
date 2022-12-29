package com.zondy.mapgis.common.core.exception.user;

/**
 * @author xiongbo
 * @since 2022/12/29 16:24
 */
public class CaptchaNullException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaNullException() {
        super("user.jcaptcha.null", null);
    }
}
