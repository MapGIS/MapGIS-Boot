package com.zondy.mapgis.file.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Accessors(chain = true)
@Schema(name = "文件信息业务对象")
public class FileStorage {
    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    private String name;

    /**
     * 文件地址
     */
    @Schema(description = "文件地址")
    private String url;
}
