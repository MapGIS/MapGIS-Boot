package com.zondy.mapgis.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 磁盘信息对象 sys_disk
 *
 * @author mapgis
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "磁盘信息对象")
public class SysDisk extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 磁盘编号
     */
    @Schema(description = "磁盘编号")
    private Long diskId;

    /**
     * 磁盘读写速度
     */
    @Schema(description = "磁盘读写速度")
    @Excel(name = "磁盘读写速度")
    private Double speed;

    /**
     * 读写状态（0读取 1写入）
     */
    @Schema(description = "读写状态（0读取 1写入）")
    @Excel(name = "读写状态", readConverterExp = "0=读取,1=写入")
    private Integer readwrite;

    /**
     * 监控时间
     */
    @Schema(description = "监控时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "监控时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
