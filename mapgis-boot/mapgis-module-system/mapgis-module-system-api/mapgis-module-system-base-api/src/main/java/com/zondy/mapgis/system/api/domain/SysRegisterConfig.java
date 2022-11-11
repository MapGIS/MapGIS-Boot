package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 用户注册配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 15:21
 */
@Data
public class SysRegisterConfig {
    /**
     * 是否启用
     */
    Boolean enabled;

    /**
     * 注册用户的默认角色
     */
    Long[] defaultRoleIds;

    public SysRegisterConfig() {
        enabled = true;
        defaultRoleIds = new Long[0];
    }
}
