package com.zondy.mapgis.common.core.web.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Schema(name = "分页响应对象")
public class TableDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private long total;

    /**
     * 列表数据
     */
    @Schema(description = "列表数据")
    private List<?> rows;

    /**
     * 消息状态码
     */
    @Schema(description = "消息状态码")
    private int code;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }
}