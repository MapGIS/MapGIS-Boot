package com.zondy.mapgis.common.systemlog.model;

import lombok.Data;

import java.util.List;

/**
 * 系统日志列表
 *
 * @author xiongbo
 * @since 2022/11/22 18:32
 */
@Data
public class SysLoginfoList {
    /**
     * 日志列表
     */
    private List<SysLoginfo> logs;

    /**
     * 是否还有更多
     */
    private boolean hasMore;

    /**
     * 文件索引
     * 格式 fileIndex:line
     */
    private String position;
}
