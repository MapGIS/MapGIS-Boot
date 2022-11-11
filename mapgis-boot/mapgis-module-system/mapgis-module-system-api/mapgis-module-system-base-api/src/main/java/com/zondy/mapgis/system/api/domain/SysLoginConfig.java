package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 用户登录配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 15:09
 */
@Data
public class SysLoginConfig {
    /**
     * 是否允许同时登录
     */
    Boolean soloLoginEnabled;

    /**
     * 是否启用验证码
     */
    Boolean captchaEnabled;

    /**
     * 验证码类型
     */
    String captchaType;

    public SysLoginConfig() {
        soloLoginEnabled = true;
        captchaEnabled = true;
        captchaType = "math";
    }
}
