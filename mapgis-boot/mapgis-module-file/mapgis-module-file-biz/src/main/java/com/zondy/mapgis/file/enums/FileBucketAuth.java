package com.zondy.mapgis.file.enums;

/**
 * 文件存储桶的权限策略枚举
 *
 * @author xiongbo
 * @since 2023/9/22 11:00
 */
public enum FileBucketAuth {

    /**
     * 私有的（仅有 owner 可以读写）
     */
    PRIVATE,

    /**
     * 公有读，私有写（ owner 可以读写， 其他客户可以读）
     */
    PUBLIC_READ,

    /**
     * 公共读写（即所有人都可以读写，慎用）
     */
    PUBLIC_READ_WRITE

}
