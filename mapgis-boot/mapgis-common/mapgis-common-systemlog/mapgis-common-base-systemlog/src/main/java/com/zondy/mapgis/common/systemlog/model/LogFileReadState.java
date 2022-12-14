package com.zondy.mapgis.common.systemlog.model;

import lombok.Data;

/**
 * 日志文件读取状态对象
 *
 * @author xiongbo
 * @since 2022/11/23 9:35
 */
@Data
public class LogFileReadState {
    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 当前行
     */
    private long line;
}
