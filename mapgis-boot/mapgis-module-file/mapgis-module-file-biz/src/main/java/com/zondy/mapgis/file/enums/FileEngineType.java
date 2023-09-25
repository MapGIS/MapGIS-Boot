package com.zondy.mapgis.file.enums;

import lombok.Getter;

/**
 * 文件存储引擎类型枚举
 *
 * @author xiongbo
 * @since 2023/9/22 11:45
 */
@Getter
public enum FileEngineType {
    /**
     * 本地
     */
    LOCAL("LOCAL"),

    /**
     * MINIO
     */
    MINIO("MINIO"),

    /**
     * FASTDFS
     */
    FASTDFS("FASTDFS");

    private final String value;

    FileEngineType(String value) {
        this.value = value;
    }
}
