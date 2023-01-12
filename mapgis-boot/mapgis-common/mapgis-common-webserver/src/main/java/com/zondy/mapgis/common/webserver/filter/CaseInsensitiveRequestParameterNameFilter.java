package com.zondy.mapgis.common.webserver.filter;

import cn.hutool.core.net.URLDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * 请求参数大小写不敏感的问题
 *
 * @author Chelsea
 * @since 2023/1/12 10:08
 */
@Slf4j
public class CaseInsensitiveRequestParameterNameFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(new CaseInsensitiveParameterNameHttpServletRequest(request), response);
    }

    public static class CaseInsensitiveParameterNameHttpServletRequest extends HttpServletRequestWrapper {
        private final LinkedCaseInsensitiveMap<String[]> map = new LinkedCaseInsensitiveMap<>();

        @SuppressWarnings("unchecked")
        public CaseInsensitiveParameterNameHttpServletRequest(HttpServletRequest request) {
            super(request);
            // 解决gateway中不支持query中的特殊字符，这里需要多次解码的问题
            // 特殊字符"<[空格{" 对应编码后 "%3C%5B%20%7B"
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                for (int i = 0; i < entry.getValue().length; i++) {
                    String oldValue = entry.getValue()[i];
                    if (oldValue != null && (oldValue.contains("%3C") || oldValue.contains("%5B") || oldValue.contains("%20") || oldValue.contains("%7B"))) {
                        try {
                            entry.getValue()[i] = URLDecoder.decode(oldValue, StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            log.warn("特殊字符解码失败:" + oldValue);
                        }
                    }
                }
                map.put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public String getParameter(String name) {

            String[] array = this.map.get(name);
            if (array != null && array.length > 0) {
                return array[0];
            }
            return null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(this.map);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(this.map.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return this.map.get(name);
        }
    }
}