package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 密码安全配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 15:01
 */
@Data
public class SysPasswordProtectedConfig {
    /**
     * 是否启用防暴力破解
     */
    Boolean enabled;

    /**
     * 允许连续失败次数
     */
    Integer maxRetryCount;

    /**
     * 自动解锁时间（分钟）
     */
    Integer lockTime;

    /**
     * 是否通过IP锁定账号
     */
    Boolean isLockedByIp;

    public SysPasswordProtectedConfig() {
        enabled = false;
        maxRetryCount = 5;
        lockTime = 10;
        isLockedByIp = true;
    }
}
