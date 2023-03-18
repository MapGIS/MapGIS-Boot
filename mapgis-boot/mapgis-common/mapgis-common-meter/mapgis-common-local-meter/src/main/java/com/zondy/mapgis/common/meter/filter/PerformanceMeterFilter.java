package com.zondy.mapgis.common.meter.filter;

import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.meter.context.MetricContext;
import com.zondy.mapgis.system.api.domain.SysServerPerformanceData;

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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        SysServerPerformanceData serverPerformanceData = new SysServerPerformanceData();

        long start = System.currentTimeMillis();
        serverPerformanceData.setAccessTimeMillis(start);
        serverPerformanceData.setUrl(httpServletRequest.getRequestURI());
        serverPerformanceData.setRemoteAddr(IpUtils.getIpAddr(httpServletRequest));
        serverPerformanceData.setMethod(httpServletRequest.getMethod());
        serverPerformanceData.setQueryString(httpServletRequest.getQueryString());

        metricContext.start(httpServletRequest, serverPerformanceData);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            metricContext.end(httpServletRequest);
            // 性能监控记录
            serverPerformanceData.setTime(System.currentTimeMillis() - start);
            serverPerformanceData.setResponseStatus((long) httpServletResponse.getStatus());
            metricContext.record(serverPerformanceData);
        }
    }
}
