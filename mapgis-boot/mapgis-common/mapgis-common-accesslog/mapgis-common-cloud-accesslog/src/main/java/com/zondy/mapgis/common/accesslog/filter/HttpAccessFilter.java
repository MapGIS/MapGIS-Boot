package com.zondy.mapgis.common.accesslog.filter;

import com.zondy.mapgis.common.accesslog.config.HttpAccessConfig;
import com.zondy.mapgis.common.accesslog.recorder.AsyncHttpAccessRecorder;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.JsonUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * http访问监控的过滤器，优先级最高
 *
 * @author xiongbo
 * @since 2022/12/8 13:59
 */
@Slf4j
@Component
public class HttpAccessFilter implements GlobalFilter, Ordered {
    @Autowired
    private HttpAccessConfig httpAccessConfig;

    @Autowired
    private AsyncHttpAccessRecorder asyncHttpAccessRecorder;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        long start = System.currentTimeMillis();
        Date accessTime = DateUtils.getNowDate();

        return chain.filter(exchange.mutate().response(new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    String uri = request.getURI().getPath();

                    boolean httpMonitorEnable = httpAccessConfig.getLogConfig().getHttpAccessEnabled() && StringUtils.isNotEmpty(httpAccessConfig.getLogConfig().getHttpAccessMonitorUrls());
                    //请求开始
                    if (httpMonitorEnable) {
                        String[] monitors = httpAccessConfig.getLogConfig().getHttpAccessMonitorUrls();
                        if (Arrays.stream(monitors).anyMatch(t -> matcher.match(t, uri))) {
                            SysHttpAccess httpAccess = new SysHttpAccess();
                            httpAccess.setAccessTime(accessTime);
                            httpAccess.setIpaddr(IpUtils.getIpAddr(request));
                            httpAccess.setMethod(request.getMethodValue());
                            httpAccess.setQueryString(getQueryString(request));
                            httpAccess.setUrl(request.getURI().getPath());
                            httpAccess.setRequestHeaders(JsonUtils.toJsonString(getRequestHeaders(request)));
                            httpAccess.setResponseHeaders(JsonUtils.toJsonString(getResponseHeaders(response)));
                            httpAccess.setResponseStatus((long) response.getStatusCode().value());
                            httpAccess.setTime(System.currentTimeMillis() - start);
                            asyncHttpAccessRecorder.record(httpAccess);
                        }
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        }).build());
    }

    @Override
    public int getOrder() {
        return -300;
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

    private SysHttpAccess.MultiPair[] getRequestHeaders(ServerHttpRequest request) {
        List<SysHttpAccess.MultiPair> pairs = new ArrayList<>();
        for (String key : request.getHeaders().keySet()) {
            SysHttpAccess.MultiPair pair = new SysHttpAccess.MultiPair();
            pair.setK(key);
            pair.setV(request.getHeaders().get(key).toArray(new String[0]));
            pairs.add(pair);
        }
        return pairs.toArray(new SysHttpAccess.MultiPair[0]);
    }

    private SysHttpAccess.MultiPair[] getResponseHeaders(ServerHttpResponse response) {
        return response.getHeaders().keySet().stream().map(t -> {
            SysHttpAccess.MultiPair pair = new SysHttpAccess.MultiPair();
            pair.setK(t);
            pair.setV(response.getHeaders().get(t).toArray(new String[0]));
            return pair;
        }).toArray(SysHttpAccess.MultiPair[]::new);
    }
}
