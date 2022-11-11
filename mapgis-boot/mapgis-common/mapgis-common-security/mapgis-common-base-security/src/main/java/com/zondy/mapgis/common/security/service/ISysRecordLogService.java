package com.zondy.mapgis.common.security.service;

/**
 * 记录日志方法
 *
 * @author xiongbo
 * @since 2022/11/11 10:41
 */
public interface ISysRecordLogService {
    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLogininfor(String username, String status, String message);
}
