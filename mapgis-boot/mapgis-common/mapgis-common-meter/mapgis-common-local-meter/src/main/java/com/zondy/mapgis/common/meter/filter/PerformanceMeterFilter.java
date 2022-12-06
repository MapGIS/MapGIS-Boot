package com.zondy.mapgis.common.meter.filter;

import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.meter.context.MetricContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 性能指标监控
 *
 * @author xiongbo
 * @since 2022/12/5 9:48
 */
public class PerformanceMeterFilter implements Filter {
    private final MetricContext metricContext;

    public PerformanceMeterFilter(MetricContext metricContext) {
        this.metricContext = metricContext;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        long start = System.currentTimeMillis();

        filterChain.doFilter(servletRequest, servletResponse);

        // 性能监控记录
        metricContext.record(start, httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                httpServletRequest.getQueryString(),
                httpServletRequest.getMethod(),
                IpUtils.getIpAddr(httpServletRequest),
                httpServletResponse.getStatus());
    }
}
