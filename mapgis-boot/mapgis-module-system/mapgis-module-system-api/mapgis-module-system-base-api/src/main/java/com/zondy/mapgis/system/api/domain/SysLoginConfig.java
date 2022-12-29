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

    /**
     * 连续失败N次显示验证码
     */
    Integer maxRetryCount;

    /**
     * 失败记录有效期（分钟）
     */
    Integer recordTime;

    public SysLoginConfig() {
        soloLoginEnabled = true;
        captchaEnabled = true;
        captchaType = "math";
        maxRetryCount = 1;
        recordTime = 10;
    }
}
