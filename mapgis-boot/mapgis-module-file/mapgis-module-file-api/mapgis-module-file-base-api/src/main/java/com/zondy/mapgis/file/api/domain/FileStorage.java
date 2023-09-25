package com.zondy.mapgis.file.api.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "文件信息业务对象")
public class FileStorage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    @Schema(description = "文件ID")
    private Long fileId;

    /**
     * 存储引擎
     */
    @Schema(description = "存储引擎")
    @Excel(name = "存储引擎")
    private String engine;

    /**
     * 存储桶
     */
    @Schema(description = "存储桶")
    @Excel(name = "存储桶")
    private String bucket;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    @Excel(name = "文件名称")
    private String name;

    /**
     * 文件后缀
     */
    @Schema(description = "文件后缀")
    @Excel(name = "文件后缀")
    private String suffix;

    /**
     * 文件大小kb
     */
    @Schema(description = "文件大小kb")
    @Excel(name = "文件大小kb")
    private String sizeKb;

    /**
     * 文件大小（格式化后）
     */
    @Schema(description = "文件大小（格式化后）")
    @Excel(name = "文件大小", readConverterExp = "格=式化后")
    private String size;

    /**
     * 文件地址
     */
    @Schema(description = "文件地址")
    @Excel(name = "文件地址")
    private String url;

    /**
     * 图片缩略图
     */
    @Schema(description = "图片缩略图")
    @Excel(name = "图片缩略图")
    private String thumbnail;
}
