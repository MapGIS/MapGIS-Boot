package com.zondy.mapgis.common.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zondy.mapgis.common.core.utils.ServletUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web工具类
 *
 * @author xiongbo
 * @since 2023/1/10 13:41
 */
public class WebUtil {
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 返回服务器的基地址，格式为 http[s]://ip:port
     *
     * @return 服务器的基地址
     */
    public static String getServerAddress() {
        return ServletUtils.getServerAddress();
    }

    /**
     * 获取请求全路径
     *
     * @return 请求全路径
     */
    public static String getRequestURL() {
        return ServletUtils.getRequestURL();
    }

    /**
     * 将Json对象渲染到客户端
     *
     * @param response 渲染对象
     * @param jsonObj  待渲染的Json对象
     * @return null
     */
    public static void renderJson(HttpServletResponse response, @Nullable Object jsonObj) throws JsonProcessingException {
        ServletUtils.renderJson(response, jsonObj);
    }

    /**
     * 将Json文本渲染到客户端
     *
     * @param response 渲染对象
     * @param jsonText 待渲染的Json文本
     * @return null
     */
    public static void renderJson(HttpServletResponse response, @Nullable String jsonText) {
        ServletUtils.renderJson(response, jsonText);
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
        ServletUtils.renderText(response, text, contentType);
    }

    /**
     * 获取客户端IP
     *
     * @return IP地址
     */
    public static String getIp() {
        return IpUtils.getIpAddr(ServletUtils.getRequest());
    }
}
