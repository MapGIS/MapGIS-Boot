package com.zondy.mapgis.common.objectserializer.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.zondy.mapgis.common.objectserializer.serializer.impl.KryoObjectSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Kryo自动配置入口
 *
 * @author xiongbo
 * @since 2023/1/11 10:54
 */
@Configuration
public class ObjectSerializerAutoConfiguration {
    /**
     * Kryo序列号自动配置
     */
    @Configuration
    @Import({KryoObjectSerializer.class})
    static class KryoSerializerAutoConfiguration {
        @Bean
        public KryoPool kryoPool() {
            return new KryoPool.Builder(() -> {
                final Kryo kryo = new Kryo();
                kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
                return kryo;
            }).softReferences().build();
        }
    }
}
