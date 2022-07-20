package com.zondy.mapgis.common.cache.config;

import com.zondy.mapgis.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * @author xiongbo
 * @since 2022/4/14 9:35
 */
@Component
public class RedisKeySerializer implements RedisSerializer<String> {

    private final Charset charset;

    /**
     * 产品名称
     */
    @Value("${mapgis.product.full-name}")
    public String name;

    public RedisKeySerializer() {
        this(Charset.forName("UTF8"));
    }

    public RedisKeySerializer(Charset charset) {
        Assert.notNull(charset, "字符集不允许为NULL");
        this.charset = charset;
    }

    @Override
    public byte[] serialize(String string) throws SerializationException {
        // 通过项目名称mapgis.product.full-name来定义Redis前缀，用于区分项目缓存
        if (StringUtils.isNotEmpty(name)) {
            return new StringBuilder(name).append(":").append(string).toString().getBytes(charset);
        }
        return string.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}