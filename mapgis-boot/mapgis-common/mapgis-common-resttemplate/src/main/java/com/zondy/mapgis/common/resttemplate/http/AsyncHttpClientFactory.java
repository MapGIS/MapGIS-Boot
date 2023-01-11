package com.zondy.mapgis.common.resttemplate.http;

import com.zondy.mapgis.common.resttemplate.properties.HttpClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


/**
 * 提供同步、异步httpclient工厂类
 * 同时支持http、https发送请求，https免验证
 *
 * @author Chelsea
 * @since 2023/1/11 12:00
 */
@Slf4j
public class AsyncHttpClientFactory {
    private final static String CONNECT_CLOSED = "远程主机强迫关闭了一个现有的连接。";
    private final static String CONNECT_CLOSED_EN = "Connection reset by peer";
    private final HttpClientProperties httpClientProperties;

    public AsyncHttpClientFactory(HttpClientProperties httpClientProperties) {
        this.httpClientProperties = httpClientProperties;
    }

    public CloseableHttpAsyncClient createHttpAsyncClient() {
        PoolingNHttpClientConnectionManager conMgr = createNHttpClientConnectionManager();
        RequestConfig requestConfig = createRequestConfig();
        HttpAsyncRequestExecutor executor = new HttpAsyncRequestExecutor(HttpAsyncRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE, ex -> {
            // 远程主机强迫关闭了一个现有的连接
            if (ex instanceof IOException && (CONNECT_CLOSED.equals(ex.getMessage()) || CONNECT_CLOSED_EN.equals(ex.getMessage()))) {
                log.warn("HttpAsyncClient IOException:" + ex.getMessage());
            } else {
                log.error("HttpAsyncClient未捕获的异常", ex);
            }
        });
        CloseableHttpAsyncClient httpAsyncClient =
                HttpAsyncClients.custom().setConnectionManager(conMgr).setDefaultRequestConfig(requestConfig).setEventHandler(executor).build();
        // 直接启动
        httpAsyncClient.start();
        return httpAsyncClient;
    }

    private PoolingNHttpClientConnectionManager createNHttpClientConnectionManager() {
        // 配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(Runtime.getRuntime().availableProcessors()).build();
        // 设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            log.error("创建IOReactor出现异常", e);
        }
        SSLIOSessionStrategy sslioSessionStrategy = null;
        try {
            // noinspection deprecation
            sslioSessionStrategy = new SSLIOSessionStrategy(createIgnoreVerifySSL(), SSLIOSessionStrategy.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            sslioSessionStrategy = SSLIOSessionStrategy.getDefaultStrategy();
        }

        Registry<SchemeIOSessionStrategy> registry = RegistryBuilder.<SchemeIOSessionStrategy>create().register("http",
                NoopIOSessionStrategy.INSTANCE).register("https", sslioSessionStrategy).build();
        PoolingNHttpClientConnectionManager conMgr = new PoolingNHttpClientConnectionManager(ioReactor, registry);

        if (httpClientProperties.getMaxConnectCount() > 0) {
            conMgr.setMaxTotal(httpClientProperties.getMaxConnectCount());
        }

        if (httpClientProperties.getMaxPerRoutCount() > 0) {
            conMgr.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoutCount());
        } else {
            conMgr.setDefaultMaxPerRoute(10);
        }
        return conMgr;
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(httpClientProperties.getRequestTimeout()).setConnectTimeout(httpClientProperties.getConnectTimeout()).setSocketTimeout(httpClientProperties.getSocketTimeout()).build();
    }

    /**
     * 创建绕过SSL验证的上下文
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
