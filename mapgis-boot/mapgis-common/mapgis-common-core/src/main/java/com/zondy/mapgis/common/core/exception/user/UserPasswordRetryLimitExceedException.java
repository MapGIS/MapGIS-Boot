package com.zondy.mapgis.common.core.exception.user;

/**
 * 用户错误最大次数异常类
 *
 * @author powanjuanshu
 * @since 2022/8/16 16:58
 */
public class UserPasswordRetryLimitExceedException extends UserException {
    private static final long serialVersionUID = 1L;

    public UserPasswordRetryLimitExceedException(int retryLimitCount, int lockTime) {
        super("user.password.retry.limit.exceed", new Object[]{retryLimitCount, lockTime});
    }
}
