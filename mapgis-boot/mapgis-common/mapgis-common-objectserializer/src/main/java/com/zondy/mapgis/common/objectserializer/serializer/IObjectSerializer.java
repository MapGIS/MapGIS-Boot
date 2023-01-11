package com.zondy.mapgis.common.objectserializer.serializer;

/**
 * 对象序列化接口
 *
 * @author Chelsea
 * @since 2023/1/11 11:04
 */
public interface IObjectSerializer {
    /**
     * 对象序列化为字节流
     *
     * @param input 需要序列化的对象
     * @return 序列化结果
     */
    byte[] serialize(Object input);

    /**
     * 反序列化字节流为对象
     *
     * @param input 反序列的字节流
     * @param <T>   结果类型
     * @return 反序列化结果
     */
    <T> T deserialize(byte[] input, Class<T> type);
}
