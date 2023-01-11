package com.zondy.mapgis.common.service.web.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.service.exception.web.BadRequestException;
import com.zondy.mapgis.common.service.factory.ResponseFactory;
import com.zondy.mapgis.common.service.format.ResponseFormat;
import com.zondy.mapgis.common.service.id.ServiceId;
import com.zondy.mapgis.common.service.session.UserSession;
import com.zondy.mapgis.common.service.session.UserSessionManager;
import com.zondy.mapgis.common.service.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * web层通用数据处理（适用于服务API，非管理API）
 *
 * @author xiongbo
 * @since 2023/1/10 10:57
 */
public class BaseController {
    @Autowired
    protected UserSessionManager userSessionManager;

    @Autowired
    protected ResponseFactory responseFactory;

    private static final Map<String, String> DEFAULT_MEDIA_TYPES = new HashMap<>();

    static {
        // 摘自swagger插件代码
        DEFAULT_MEDIA_TYPES.put("html", "text/html");
        DEFAULT_MEDIA_TYPES.put("png", "image/png");
        DEFAULT_MEDIA_TYPES.put("gif", "image/gif");
        DEFAULT_MEDIA_TYPES.put("css", "text/css");
        DEFAULT_MEDIA_TYPES.put("js", "application/javascript");
        DEFAULT_MEDIA_TYPES.put("eot", "application/vnd.ms-fontobject");
        DEFAULT_MEDIA_TYPES.put("ttf", "application/font-sfnt");
        DEFAULT_MEDIA_TYPES.put("svg", "image/svg+xml");
        DEFAULT_MEDIA_TYPES.put("woff", "application/font-woff");
        DEFAULT_MEDIA_TYPES.put("woff2", "application/font-woff2");
        DEFAULT_MEDIA_TYPES.put("ico", "image/x-icon");
    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return WebUtil.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return WebUtil.getResponse();
    }

    /**
     * 获取目录名
     *
     * @return 目录名
     */
    public String getFolderName() {
        return getPathVariables("folder");
    }

    public String getServiceCompositeName() {
        return getServiceId().toCompositeName();
    }

    public ServiceId getServiceId() {
        return ServiceId.from(getPathVariables("serviceName"), getFolderName());
    }

    protected UserSession getUserSession() {
        HttpServletRequest request = getRequest();
        // 需要判断当前请求是否跨域，如果跨域，不能创建新的session，因为cookie在跨域下不可用
        boolean isCorsReq = CorsUtils.isCorsRequest(request);
        // 如果是跨域情况下，根据远程地址创建id
        return userSessionManager.get(!isCorsReq ? request.getSession().getId() : request.getRemoteHost());
    }

    protected UserSession getUserSession(String userID) {
        if (StringUtils.isEmpty(userID)) {
            return getUserSession();
        }
        return userSessionManager.get(userID);
    }

    public String getPathVariables(String pathVariable) {
        HttpServletRequest request = getRequest();
        if (StringUtils.isNotBlank(pathVariable) && request != null) {
            Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            return variables.get(pathVariable);
        }
        return null;
    }

    /**
     * 获取响应格式对象
     *
     * @param f 格式名称
     * @return 格式对象
     */
    protected ResponseFormat getResponseFormat(String f) {
        // 默认返回JSON格式，XML解析不通用
        return ResponseFormat.fromName(f, ResponseFormat.JSON);
    }

    /**
     * 响应头添加gzip压缩
     */
    protected void addResponseGzipHeader() {
        getResponse().setHeader("Content-Encoding", "gzip");
    }

    /**
     * 响应头添加缓存有效期
     *
     * @param maxAgeSecond 以秒为单位的缓存时间
     */
    protected void addResponseCacheControlHeader(int maxAgeSecond) {
        maxAgeSecond = Math.max(maxAgeSecond, 1);
        getResponse().setHeader("Cache-Control", "max-age=" + maxAgeSecond);
    }

    /**
     * 响应头添加空瓦片标识
     */
    protected void addResponseBlankTileHeader() {
        getResponse().setHeader("blank-tile", "true");
    }

    /**
     * 是否是html请求格式
     *
     * @param f 格式类型
     * @return
     */
    public boolean isHtmlRequest(String f) {
        String method = getRequest().getMethod();
        return (StrUtil.isBlank(f) && "get".equalsIgnoreCase(method)) || "html".equalsIgnoreCase(f);
    }

    /**
     * 断言格式是否支持，不支持抛出异常
     *
     * @param f              请求格式
     * @param supportFormats 支持格式列表
     */
    public void assertFormat(String f, String... supportFormats) {
        if (supportFormats.length > 0 && Arrays.stream(supportFormats).noneMatch(t -> t.equalsIgnoreCase(f))) {
            throw new BadRequestException("不支持此格式:" + f);
        }
    }

    /**
     * 将POST、PUT体（application/x-www-form-urlencoded或text/plain格式）和
     * QueryString解析为Map<String,String>, 当QueryString和Body同时存在某参数时，QueryString优先级高
     *
     * @param postBody post字符串
     * @return key，value
     */
    public Map<String, String> getRequestParams(String postBody) {
        // 注意queryString优先级高
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotBlank(postBody)) {
            sb.append(postBody);
        }
        String query = getRequest().getQueryString();
        if (StrUtil.isNotBlank(query)) {
            sb.append("&").append(query);
        }
        String urlEncode = sb.toString();
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isNotBlank(urlEncode)) {
            map = HttpUtil.decodeParamMap(postBody + "&" + query, StandardCharsets.UTF_8);
        }
        // put方式请求 Content-Type: application/x-www-form-urlencoded 时postBody获取不到put内容，
        // 只有 text/plain才能拿到，这里兼容application/x-www-form-urlencoded 方式，从request里面拿
        Map<String, String[]> kv = getRequest().getParameterMap();
        for (Map.Entry<String, String[]> entry : kv.entrySet()) {
            if (entry.getValue() != null && entry.getValue().length > 0) {
                map.put(entry.getKey(), URLUtil.decode(entry.getValue()[0]));
            }
        }
        return map;
    }
}
