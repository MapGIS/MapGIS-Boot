package com.zondy.mapgis.common.core.exception.user;

/**
 * 验证码错误异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super("user.jcaptcha.error", null);
    }
}
