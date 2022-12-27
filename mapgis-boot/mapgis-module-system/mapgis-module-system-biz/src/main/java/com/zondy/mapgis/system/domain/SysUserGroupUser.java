package com.zondy.mapgis.system.domain;

import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户组和用户关联对象 sys_user_group_user
 *
 * @author mapgis
 * @date 2022-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "用户组和用户关联对象")
public class SysUserGroupUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户组ID
     */
    @Schema(description = "用户组ID")
    private Long userGroupId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

}
