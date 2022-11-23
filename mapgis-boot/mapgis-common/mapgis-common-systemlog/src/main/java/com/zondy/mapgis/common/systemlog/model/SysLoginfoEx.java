package com.zondy.mapgis.common.systemlog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author powanjuanshu
 * @since 2022/11/22 18:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginfoEx extends SysLoginfo {
    /**
     * 行号
     */
    @JsonIgnore
    private int line;
}
