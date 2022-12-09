package com.zondy.mapgis.system.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zondy.mapgis.common.core.annotation.Excel;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Http访问日志对象 sys_http_access
 *
 * @author xiongbo
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(name = "Http访问日志对象")
public class SysHttpAccess extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 访问ID
     */
    @Schema(description = "访问ID")
    private Long accessId;

    /**
     * 客户端IP
     */
    @Schema(description = "客户端IP")
    @Excel(name = "客户端IP")
    private String ipaddr;

    /**
     * 请求URL
     */
    @Schema(description = "请求URL")
    @Excel(name = "请求URL")
    private String url;

    /**
     * 方法名称
     */
    @Schema(description = "方法名称")
    private String method;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    @Excel(name = "请求参数")
    private String queryString;

    /**
     * 请求头
     */
    @Schema(description = "请求头")
    private String requestHeaders;

    /**
     * 请求体
     */
    @Schema(description = "请求体")
    private String requestBody;

    /**
     * 请求体大小
     */
    @Schema(description = "请求体大小")
    private Long requestBodySize;

    /**
     * 返回状态码
     */
    @Schema(description = "返回状态码")
    @Excel(name = "返回状态码")
    private Long responseStatus;

    /**
     * 响应头
     */
    @Schema(description = "响应头")
    private String responseHeaders;

    /**
     * 响应体
     */
    @Schema(description = "响应体")
    private String responseBody;

    /**
     * 响应体大小
     */
    @Schema(description = "响应体大小")
    private Long responseBodySize;

    /**
     * 耗时
     */
    @Schema(description = "耗时")
    @Excel(name = "耗时")
    private Long time;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    public MultiPair[] getRequestHeaderPairs() {
        return JsonUtils.parseObject(requestHeaders, MultiPair[].class);
    }

    public MultiPair[] getResponseHeadersPairs() {
        return JsonUtils.parseObject(responseHeaders, MultiPair[].class);
    }

    private Long accessTimeMillis;

    @Data
    public static class MultiPair {
        private String k;
        private String[] v;
    }
}
