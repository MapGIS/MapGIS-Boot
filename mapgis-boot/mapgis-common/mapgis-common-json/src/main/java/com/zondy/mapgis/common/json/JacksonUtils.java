package com.zondy.mapgis.common.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Jackson工具类
 *
 * @author Chelsea
 * @since 2023/1/10 16:58
 */
@Slf4j
public class JacksonUtils {
    /**
     * JsonOrg是否在当前classpath中,注意static字段的执行次序
     */
    public static final boolean JSON_ORG_MODULE_PRESENT = ClassUtils.isPresent("com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule", JacksonUtils.class.getClassLoader());

    /**
     * JsonOrg是否在当前classpath中,注意static字段的执行次序
     */
    public static final boolean JTS_MODULE_PRESENT = ClassUtils.isPresent("org.locationtech.jts.geom.Geometry", JacksonUtils.class.getClassLoader()) && ClassUtils.isPresent("com.bedatadriven.jackson.datatype.jts.JtsModule", JacksonUtils.class.getClassLoader());

    /**
     * 默认的序列化对象
     */
    public static ObjectMapper DEFAULT_OBJECT_MAPPER_CACHE = createDefaultObjectMapper();

    /**
     * 轻量json序列化，不包括值为empty的字段
     */
    public static ObjectMapper LIGHT_OBJECT_MAPPER = createDefaultObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    /**
     * 创建默认配置的mapper
     */
    public static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 加这一句是因为如果转换时间是会自动把日期格式转换成时间戳，这句话是防止这种转换
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 全局的扩展通过bean统一管理，无法使用bean容器，因为生命周期无法控制
//        Map<String, Module> moduleMap = SpringUtil.getBeansOfType(Module.class);
//        if (moduleMap != null) {
//            moduleMap.values().forEach(mapper::registerModule);
//        }

        // 有可能没有加JsonOrg的依赖，这里动态添加
        if (JSON_ORG_MODULE_PRESENT) {
            // 解析JSONObject,JSONArray
            mapper.registerModule(new JsonOrgModuleCondition().getModule());
        }
        // 支持locationtech的geometry json正反序列化
        if (JTS_MODULE_PRESENT) {
            mapper.registerModule(new GeoJsonModuleCondition().getModule());
        }
        return mapper;
    }

    public static <T> T fromJson(String json, Class<T> className) {
        T t = null;
        try {
            if (StringUtils.hasText(json)) {
                t = DEFAULT_OBJECT_MAPPER_CACHE.readValue(json, className);
            }
        } catch (Exception e) {
            log.warn("反序列化json出现异常{}", e.getMessage());
        }
        return t;
    }

    public static <T> T fromFile(File file, Class<T> className) {
        T t = null;
        try {
            if (file != null && file.exists()) {
                t = DEFAULT_OBJECT_MAPPER_CACHE.readValue(file, className);
            }
        } catch (Exception e) {
            log.warn("反序列化json出现异常{}", e.getMessage());
        }
        return t;
    }

    public static boolean toFile(File file, Object o) {
        try {
            if (file != null && file.exists()) {
                DEFAULT_OBJECT_MAPPER_CACHE.writeValue(file, o);
                return true;
            }
        } catch (Exception e) {
            log.warn("序列化json到文件出现异常{}", e.getMessage());
        }
        return false;
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        T t = null;
        try {
            if (StringUtils.hasText(json)) {
                t = DEFAULT_OBJECT_MAPPER_CACHE.readValue(json, typeReference);
            }
        } catch (Exception e) {
            log.warn("反序列化json出现异常{}", e.getMessage());
        }
        return t;
    }

    public static String toJson(Object o) {
        try {
            if (o != null) {
                return DEFAULT_OBJECT_MAPPER_CACHE.writeValueAsString(o);
            }
        } catch (Exception e) {
            log.warn("序列化json出现异常{}", e.getMessage());
        }
        return null;
    }
}
