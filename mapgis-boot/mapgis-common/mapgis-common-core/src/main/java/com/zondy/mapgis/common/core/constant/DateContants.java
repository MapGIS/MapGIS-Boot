package com.zondy.mapgis.common.core.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日期时间常量
 *
 * @author xiongbo
 * @since 2022/11/28 16:19
 */
public class DateContants {
    public final static ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");
    public final static DateTimeFormatter TIME_WITH_MILLI_SECOND_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZONE_ID);
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZONE_ID);
    public final static DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZONE_ID);
    public final static DateTimeFormatter DATE_TIME_NO_SPLIT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZONE_ID);
    public final static DateTimeFormatter DATE_TIME_MILLI_SECOND_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZONE_ID);
    public final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZONE_ID);
    public final static DateTimeFormatter TIME_WITH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy").withZone(ZONE_ID);
}