package com.zondy.mapgis.common.service.json;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.Module;

/**
 * 该类可能不会加载，主要用于类加载时，验证类是否存在
 * 如果开启了类加载验证（springboot idea中调试时-noverify默认会关闭符号引用验证）
 *
 * @author Chelsea
 * @since 2023/1/10 16:58
 */
public class GeoJsonModuleCondition {
    public Module getModule() {
        // 保证double数据精度，设置为16位
        return new JtsModule(16);
    }
}
