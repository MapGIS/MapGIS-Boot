package com.zondy.mapgis.common.accesslog.filter;

import com.zondy.mapgis.common.accesslog.config.HttpAccessConfig;
import com.zondy.mapgis.common.accesslog.recorder.AsyncHttpAccessRecorder;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * http访问监控的过滤器，优先级最高
 *
 * @author xiongbo
 * @since 2022/11/29 14:45
 */
@Slf4j
public class HttpAccessFilter implements Filter {
    private static final String HTTP_ACCESS_RECORD_ATTR = "HTTP_ACCESS_RECORD_ATTR";
    private HttpAccessConfig httpAccessConfig;
    private AsyncHttpAccessRecorder asyncHttpAccessRecorder;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public HttpAccessFilter(HttpAccessConfig httpAccessConfig, AsyncHttpAccessRecorder asyncHttpAccessRecorder) {
        this.httpAccessConfig = httpAccessConfig;
        this.asyncHttpAccessRecorder = asyncHttpAccessRecorder;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String uri = httpServletRequest.getRequestURI();

        boolean httpMonitorEnable = httpAccessConfig.getLogConfig().getHttpAccessEnabled() && StringUtils.isNotEmpty(httpAccessConfig.getLogConfig().getHttpAccessMonitorUrls());
        //请求开始
        if (httpMonitorEnable) {
            String[] monitors = httpAccessConfig.getLogConfig().getHttpAccessMonitorUrls();
            if (Arrays.stream(monitors).anyMatch(t -> matcher.match(t, uri))) {
                SysHttpAccess httpAccess = new SysHttpAccess();
                httpAccess.setAccessTimeMillis(System.currentTimeMillis());
                httpAccess.setAccessTime(DateUtils.getNowDate());
                servletRequest.setAttribute(HTTP_ACCESS_RECORD_ATTR, httpAccess);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

        //返回请求
        if (httpMonitorEnable) {
            SysHttpAccess httpAccess = (SysHttpAccess) servletRequest.getAttribute(HTTP_ACCESS_RECORD_ATTR);
            if (httpAccess != null) {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                httpAccess.setIpaddr(IpUtils.getIpAddr(request));
                httpAccess.setMethod(request.getMethod());
                httpAccess.setQueryString(request.getQueryString());
                httpAccess.setUrl(request.getRequestURI());
                httpAccess.setRequestBody(getPostData(request));
                httpAccess.setRequestHeaders(JsonUtils.toJsonString(getRequestHeaders(request)));
                httpAccess.setResponseHeaders(JsonUtils.toJsonString(getResponseHeaders(response)));
                httpAccess.setResponseStatus((long) response.getStatus());
                httpAccess.setTime(System.currentTimeMillis() - httpAccess.getAccessTimeMillis());
                asyncHttpAccessRecorder.record(httpAccess);
                servletRequest.removeAttribute(HTTP_ACCESS_RECORD_ATTR);
            }
        }
    }

    private static String getPostData(HttpServletRequest request) {
        String contentType = request.getContentType();
        String method = request.getMethod();
        String content = "";
        boolean shouldRecord = ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) && "application/x-www-form-urlencoded".equals(contentType);
        if (shouldRecord) {
            try {
                // 通过流永远也读不到，tomcat容器只允许ServletInputStream被他自己消费，否则，会导致容器无法获取post内容
                // if (request.getInputStream().available() != 0) {
                //     content = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
                // }
                // 这里获取解析后的内容
                List<String> paramList = new ArrayList<>();
                request.getParameterMap().forEach((k, v) -> {
                    paramList.add(k + "=" + String.join(",", v));
                });
                content = String.join("&", paramList);
            } catch (Exception e) {
                log.warn("获取ParameterMap内容时出现异常", e);
            }
        }
        return content;
    }

    private SysHttpAccess.MultiPair[] getRequestHeaders(HttpServletRequest request) {
        List<SysHttpAccess.MultiPair> pairs = new ArrayList<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Enumeration<String> headers = request.getHeaders(name);
            SysHttpAccess.MultiPair pair = new SysHttpAccess.MultiPair();
            pair.setK(name);
            List<String> values = new ArrayList<>();
            while (headers.hasMoreElements()) {
                values.add(headers.nextElement());
            }
            pair.setV(values.toArray(new String[0]));
            pairs.add(pair);
        }
        return pairs.toArray(new SysHttpAccess.MultiPair[0]);
    }

    private SysHttpAccess.MultiPair[] getResponseHeaders(HttpServletResponse response) {
        return response.getHeaderNames().stream().map(t -> {
            SysHttpAccess.MultiPair pair = new SysHttpAccess.MultiPair();
            pair.setK(t);
            pair.setV(response.getHeaders(t).toArray(new String[0]));
            return pair;
        }).toArray(SysHttpAccess.MultiPair[]::new);
    }
}
