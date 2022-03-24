package com.zondy.mapgis.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Accessors(chain = true)
@ApiModel("用户和岗位关联")
public class SysUserPost {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 岗位ID
     */
    @ApiModelProperty(value = "岗位ID")
    private Long postId;
}
