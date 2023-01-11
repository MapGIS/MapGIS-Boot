package com.zondy.mapgis.common.kryo.serializer.impl;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.zondy.mapgis.common.kryo.serializer.IObjectSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化
 *
 * @author Chelsea
 * @since 2023/1/11 11:06
 */
@Slf4j
public class KryoObjectSerializer implements IObjectSerializer {
    public KryoObjectSerializer(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    private KryoPool kryoPool;

    @Override
    public byte[] serialize(Object input) {
        try {
            return kryoPool.run((kryo) -> {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try (Output output = new Output(byteArrayOutputStream)) {
                    kryo.writeObject(output, input);
                    output.flush();
                    return byteArrayOutputStream.toByteArray();
                }
            });
        } catch (KryoException e) {
            log.warn("kryo序列化出现异常" + e.getMessage());
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] input, Class<T> type) {
        try {
            return kryoPool.run((kryo) -> {
                Input input1 = new Input(input);
                return kryo.readObject(input1, type);
            });
        } catch (KryoException e) {
            log.warn("kryo反序列化出现异常" + e.getMessage());
        }
        return null;
    }
}