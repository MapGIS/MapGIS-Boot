package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * 第三方用户登录额外配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 14:54
 */
@Data
public class SysAuthExtraConfig {
    /**
     * 创建用户的默认用户组
     */
    Long[] defaultUserGroupIds;

    public SysAuthExtraConfig() {
        defaultUserGroupIds = new Long[0];
    }
}
