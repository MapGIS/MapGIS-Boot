package com.zondy.mapgis.common.service.format;

import lombok.Getter;

import java.util.Arrays;

/**
 * 响应格式枚举
 *
 * @author Chelsea
 * @since 2023/1/10 14:20
 */
public enum ResponseFormat {
    JSON(1),
    XML(2),
    TEXT(3),
    IMAGE(4),
    BLANK_IMAGE(5),
    PBF(8),
    GEOJSON(9),
    HTML(10),
    KML(16),
    GML(17),
    GML2(18),
    GML3(19),
    GEORSS(20),
    ARCGISXML(21);

    @Getter
    private int value;

    ResponseFormat(int value) {
        this.value = value;
    }

    public static ResponseFormat fromName(final String name) {
        return fromName(name, ResponseFormat.JSON);
    }

    public static ResponseFormat fromName(final String name, ResponseFormat defaultValue) {
        return Arrays.stream(ResponseFormat.values()).filter(t -> t.name().equalsIgnoreCase(name)).findFirst().orElse(defaultValue);
    }
}
