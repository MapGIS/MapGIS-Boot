package com.zondy.mapgis.system.api.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 第三方登录配置对象 sys_auth_config
 *
 * @author mapgis
 * @date 2022-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "第三方登录配置对象")
public class SysAuthConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @Schema(description = "配置ID")
    private Integer configId;

    /**
     * 登录平台
     */
    @Schema(description = "登录平台")
    @Excel(name = "登录平台")
    private String type;

    /**
     * 登录名称
     */
    @Schema(description = "登录名称")
    @Excel(name = "登录名称")
    private String name;

    /**
     * 登录图标
     */
    @Schema(description = "登录图标")
    private String icon;

    /**
     * 登录帮助
     */
    @Schema(description = "登录帮助")
    private String help;

    /**
     * 客户端Id
     */
    @Schema(description = "客户端Id")
    @Excel(name = "客户端Id")
    private String clientId;

    /**
     * 客户端秘钥
     */
    @Schema(description = "客户端秘钥")
    private String clientSecret;

    /**
     * 回调地址
     */
    @Schema(description = "回调地址")
    @Excel(name = "回调地址")
    private String redirectUri;

    /**
     * 授权请求Class
     */
    @Schema(description = "授权请求Class")
    private String authRequestClass;

    /**
     * 状态（0正常 1停用）
     */
    @Schema(description = "状态（0正常 1停用）")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

}
