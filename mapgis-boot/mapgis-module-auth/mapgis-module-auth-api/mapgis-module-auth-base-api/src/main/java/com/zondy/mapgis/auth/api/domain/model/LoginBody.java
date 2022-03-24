package com.zondy.mapgis.auth.api.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录对象
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Accessors(chain = true)
@ApiModel("用户登录对象")
public class LoginBody {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识")
    private String uuid = "";
}

