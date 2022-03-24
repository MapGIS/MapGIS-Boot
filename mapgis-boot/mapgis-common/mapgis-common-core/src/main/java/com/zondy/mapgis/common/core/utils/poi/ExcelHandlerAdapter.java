package com.zondy.mapgis.common.core.utils.poi;

/**
 * Excel数据格式处理适配器
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public interface ExcelHandlerAdapter {
    /**
     * 格式化
     *
     * @param value 单元格数据值
     * @param args  excel注解args参数组
     * @return 处理后的值
     */
    Object format(Object value, String[] args);
}
