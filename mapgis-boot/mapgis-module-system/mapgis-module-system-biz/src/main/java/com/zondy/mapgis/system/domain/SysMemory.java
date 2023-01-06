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
 * 内存信息对象 sys_memory
 *
 * @author mapgis
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "内存信息对象")
public class SysMemory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 内存ID
     */
    @Schema(description = "内存ID")
    private Long memoryId;

    /**
     * 内存使用率
     */
    @Schema(description = "内存使用率")
    @Excel(name = "内存使用率")
    private Double percent;

    /**
     * 监控时间
     */
    @Schema(description = "监控时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "监控时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
