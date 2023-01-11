package com.zondy.mapgis.common.core.constant;

/**
 * OpenApi常量
 *
 * @author xiongbo
 * @since 2023/1/11 15:08
 */
public class OpenApiConstants {
    /**
     * 控制tag顺序，可以自定义顺序规则
     * 使用方式：用在@Tag注解上
     *
     * @Tag(name="", description="", extensions = {@Extension(properties = @ExtensionProperty(name = OpenApiConst.X_ORDER_PROPERTY, value = "1"))})
     */
    public final static String X_ORDER_PROPERTY = "x-order";
}
