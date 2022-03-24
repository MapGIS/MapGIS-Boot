package com.zondy.mapgis.auth.api.domain.service;

import com.zondy.mapgis.common.core.exception.user.CaptchaException;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;

import java.io.IOException;

/**
 * 验证码处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
