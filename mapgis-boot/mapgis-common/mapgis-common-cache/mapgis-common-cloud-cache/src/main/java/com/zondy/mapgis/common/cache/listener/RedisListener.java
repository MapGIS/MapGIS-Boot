package com.zondy.mapgis.common.cache.listener;

import java.util.HashMap;

/**
 * Redis监听器
 *
 * @author powanjuanshu
 * @since 2022/9/20 17:09
 */
public interface RedisListener {
    /**
     * 消息响应
     *
     * @param message
     */
    void onMessage(HashMap<String, Object> message);
}
