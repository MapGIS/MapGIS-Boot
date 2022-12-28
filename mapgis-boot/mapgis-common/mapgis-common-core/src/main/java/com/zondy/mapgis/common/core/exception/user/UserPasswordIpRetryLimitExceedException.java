package com.zondy.mapgis.common.core.exception.user;

/**
 * 用户错误最大次数异常类
 *
 * @author xiongbo
 * @since 2022/12/28 14:14
 */
public class UserPasswordIpRetryLimitExceedException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordIpRetryLimitExceedException(int retryLimitCount, String username, String ip, int lockTime) {
        super("user.password.ip.retry.limit.exceed", new Object[]{retryLimitCount, username, ip, lockTime});
    }
}
