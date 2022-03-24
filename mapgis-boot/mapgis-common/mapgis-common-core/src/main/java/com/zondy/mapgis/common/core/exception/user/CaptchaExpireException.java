package com.zondy.mapgis.common.core.exception.user;

/**
 * 验证码失效异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class CaptchaExpireException extends UserException {
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException() {
        super("user.jcaptcha.expire", null);
    }
}
