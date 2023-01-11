package com.zondy.mapgis.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zondy.mapgis.common.core.constant.Constants;
import com.zondy.mapgis.common.core.text.Convert;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

/**
 * 客户端工具类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Slf4j
public class ServletUtils {
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {
        return Convert.toBool(getRequest().getParameter(name));
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {
        return Convert.toBool(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        try {
            return getRequestAttributes().getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        if (StringUtils.isEmpty(value)) {
            return StringUtils.EMPTY;
        }
        return urlDecode(value);
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedCaseInsensitiveMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 将Json字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Json对象渲染到客户端
     *
     * @param response 渲染对象
     * @param jsonObj  待渲染的Json对象
     * @return null
     */
    public static void renderJson(HttpServletResponse response, @Nullable Object jsonObj) throws JsonProcessingException {
        ObjectMapper o = SpringUtils.getBean(ObjectMapper.class);
        String jsonText = o.writeValueAsString(jsonObj);
        if (jsonText != null) {
            renderText(response, jsonText, "application/json");
        }
    }

    /**
     * 将Json文本渲染到客户端
     *
     * @param response 渲染对象
     * @param jsonText 待渲染的Json文本
     * @return null
     */
    public static void renderJson(HttpServletResponse response, @Nullable String jsonText) {
        if (jsonText != null) {
            renderText(response, jsonText, "application/json");
        }
    }

    /**
     * 将文本渲染到客户端
     *
     * @param response    渲染对象
     * @param text        待渲染的文本
     * @param contentType 内容类型
     * @return null
     */
    public static void renderText(HttpServletResponse response, String text, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);

        try (PrintWriter out = response.getWriter()) {
            out.append(text);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return StringUtils.inStringIgnoreCase(ajax, "json", "xml");
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, Constants.UTF8);
        } catch (UnsupportedEncodingException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 返回服务器的基地址，格式为 http[s]://ip:port
     *
     * @return 服务器的基地址
     */
    public static String getServerAddress() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        }
        return null;
    }

    /**
     * 获取请求全路径
     *
     * @return 请求全路径
     */
    public static String getRequestURL() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getRequestURL().toString();
        }
        return null;
    }
}
