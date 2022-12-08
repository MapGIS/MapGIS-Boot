package com.zondy.mapgis.common.meter.filter;

import com.zondy.mapgis.common.core.constant.SecurityConstants;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysServerPerformanceData;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 性能指标监控
 *
 * @author xionbgo
 * @since 2022/12/6 16:59
 */
@Component
public class PerformanceMeterFilter implements GlobalFilter, Ordered {
    @Autowired
    private ISysServiceApi sysServiceApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        long start = System.currentTimeMillis();

        return chain.filter(exchange.mutate().response(new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    SysServerPerformanceData serverPerformanceData = new SysServerPerformanceData();

                    // 性能监控记录
                    serverPerformanceData.setAccessTimeMillis(start);
                    serverPerformanceData.setTime(System.currentTimeMillis() - start);
                    serverPerformanceData.setUrl(request.getURI().getPath());
                    serverPerformanceData.setIpaddr(IpUtils.getIpAddr(request));
                    serverPerformanceData.setRemoteAddr(request.getRemoteAddress().getAddress().getHostAddress());
                    serverPerformanceData.setMethod(request.getMethodValue());
                    serverPerformanceData.setQueryString(getQueryString(request));
                    serverPerformanceData.setResponseStatus((long) response.getStatusCode().value());

                    CompletableFuture.supplyAsync(() -> sysServiceApi.savePerformanceMonitorRecord(serverPerformanceData, SecurityConstants.INNER));
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        }).build());
    }

    @Override
    public int getOrder() {
        return -301;
    }

    private String getQueryString(ServerHttpRequest request) {
        final MultiValueMap<String, String> queryParams = request.getQueryParams();

        if (StringUtils.isNotEmpty(queryParams)) {
            return queryParams.entrySet().stream()
                    .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue().get(0)))
                    .collect(Collectors.joining("&"));
        }

        return "";
    }
}