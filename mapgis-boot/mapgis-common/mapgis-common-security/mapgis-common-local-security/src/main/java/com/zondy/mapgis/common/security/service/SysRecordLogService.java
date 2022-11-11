package com.zondy.mapgis.common.security.service;

import com.zondy.mapgis.common.security.manager.AsyncManager;
import com.zondy.mapgis.common.security.manager.factory.AsyncFactory;
import org.springframework.stereotype.Component;

/**
 * 记录日志方法
 *
 * @author xiongbo
 * @since 2022/11/9 16:59
 */
@Component
public class SysRecordLogService implements ISysRecordLogService {
    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    @Override
    public void recordLogininfor(String username, String status, String message) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, status, message));
    }
}
