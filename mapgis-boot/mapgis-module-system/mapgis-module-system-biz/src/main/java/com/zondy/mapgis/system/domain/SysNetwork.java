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
 * 网络信息对象 sys_network
 *
 * @author mapgis
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "网络信息对象")
public class SysNetwork extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 网络ID
     */
    @Schema(description = "网络ID")
    private Long networkId;

    /**
     * 网络访问速度
     */
    @Schema(description = "网络访问速度")
    @Excel(name = "网络访问速度")
    private Double speed;

    /**
     * 上行下行状态（0上行 1下行）
     */
    @Schema(description = "上行下行状态（0上行 1下行）")
    @Excel(name = "上行下行状态", readConverterExp = "0=上行,1=下行")
    private Integer updown;

    /**
     * 监控时间
     */
    @Schema(description = "监控时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "监控时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
