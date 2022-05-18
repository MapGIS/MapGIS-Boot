package com.zondy.mapgis.job.domain;

import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "定时任务调度日志对象")
public class SysJobLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "日志序号")
    @Excel(name = "日志序号")
    private Long jobLogId;

    /**
     * 任务名称
     */
    @Schema(description = "任务名称")
    @Excel(name = "任务名称")
    private String jobName;

    /**
     * 任务组名
     */
    @Schema(description = "任务名称")
    @Excel(name = "任务组名")
    private String jobGroup;

    /**
     * 调用目标字符串
     */
    @Schema(description = "任务名称")
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /**
     * 日志信息
     */
    @Schema(description = "任务名称")
    @Excel(name = "日志信息")
    private String jobMessage;

    /**
     * 执行状态（0正常 1失败）
     */
    @Schema(description = "任务名称")
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /**
     * 异常信息
     */
    @Schema(description = "任务名称")
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /**
     * 开始时间
     */
    @Schema(description = "任务名称")
    private Date startTime;

    /**
     * 停止时间
     */
    @Schema(description = "任务名称")
    private Date stopTime;
}

