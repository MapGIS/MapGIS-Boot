package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.enums.UserStatus;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.MessageUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author xiongbo
 * @since 2022/11/10 9:56
 */
@Service
public class UserDetailsService {
    @Autowired
    private ISysServiceApi sysServiceApi;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 验证登录用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录用户
     */
    public LoginUser loadUserByUsername(String username, String password) {
        // 查询用户信息
        R<LoginUser> userResult = sysServiceApi.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser loginUser = userResult.getData();
        if (StringUtils.isNull(loginUser) || StringUtils.isNull(loginUser.getUser())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists"));
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        SysUser user = loginUser.getUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete"));
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked"));
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        passwordService.validate(user, password);

        return loginUser;
    }
}
