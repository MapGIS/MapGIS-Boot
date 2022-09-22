package com.zondy.mapgis.common.cache.receiver;

import com.zondy.mapgis.common.cache.listener.RedisListener;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Redis消息接收器
 *
 * @author powanjuanshu
 * @since 2022/9/20 16:50
 */
@Component
public class RedisReceiver {
    /**
     * 收到通道的消息之后执行的方法
     *
     * @param message
     */
    public void receiveMessage(HashMap<String, Object> message) {
        Object listenerName = message.get(Constants.REDIS_LISTENER_NAME);

        try {
            RedisListener redisListener = SpringUtils.getBean(listenerName.toString());
            if (StringUtils.isNotNull(redisListener)) {
                redisListener.onMessage(message);
            }
        } catch (BeansException exception) {

        }
    }
}
