package com.zondy.mapgis.auth.domain.vo;

import com.zondy.mapgis.system.api.domain.SysAuthUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 第三方授权表 sys_auth_user
 *
 * @author xiongbo
 * @since 2022/6/1 20:26
 */
@Schema(name = "第三方授权用户验证密码对象")
@Data
public class AuthUserCheckPasswordVo extends SysAuthUser {
    /**
     * 待验证的密码
     */
    String password;
}
