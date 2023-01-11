package com.zondy.mapgis.common.service.json;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

/**
 * 该类可能不会加载，主要用于类加载时，验证类是否存在
 * 如果开启了类加载验证（springboot idea中调试时-noverify默认会关闭符号引用验证）
 *
 * @author Chelsea
 * @since 2023/1/10 16:58
 */
public class JsonOrgModuleCondition {
    /**
     * 这里是JsonOrgModule的包装类，实现根据classpath条件查找JsonOrgModule
     * 注意，如果没有JsonOrgModule类，该类不能实例化，但是可以被外部类链接
     * 这里返回类型千万不能是JsonOrgModule，否则解析类链接时，会抛加载JsonOrgModule类失败的异常
     * <p>
     * 整个生命周期包括：加载——【验证——准备——解析（连接Linking）】——初始化——使用——卸载 这7个阶段
     * 解决验证阶段，检查类是否存在
     *
     * @return
     */
    public Module getModule() {
        return new JsonOrgModule();
    }
}
