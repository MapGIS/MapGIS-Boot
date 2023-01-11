package com.zondy.mapgis.common.resttemplate.http;

import com.zondy.mapgis.common.resttemplate.properties.HttpClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;


/**
 * 提供同步httpclient工厂类
 * 同时支持http、https发送请求，https免验证
 *
 * @author Chelsea
 * @since 2023/1/11 12:00
 */
@Slf4j
public class HttpClientFactory {
    /**
     * 连接空闲的时间单位
     */
    private final static TimeUnit DEFAULT_IDLE_TIME_UNIT = TimeUnit.MINUTES;
    private final HttpClientProperties httpClientProperties;

    public HttpClientFactory(HttpClientProperties httpClientProperties) {
        this.httpClientProperties = httpClientProperties;
    }

    public CloseableHttpClient createHttpClient() {
        HttpClientConnectionManager cm = createHttpClientConnectionManager();
        // 请求超时配置 请不要设置socketTimeout，因为可能存在一个路由多个请求会共享一个底层socket通道，导致其他请求莫名其妙的socketTimeout
        // socketTimeout：数据传输过程中数据包之间间隔的最大时间
        RequestConfig requestConfig = createRequestConfig();
        return createHttpClient(cm, requestConfig);
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(httpClientProperties.getRequestTimeout()).setConnectTimeout(httpClientProperties.getConnectTimeout()).setSocketTimeout(httpClientProperties.getSocketTimeout()).build();
    }

    private CloseableHttpClient createHttpClient(HttpClientConnectionManager cm, RequestConfig requestConfig) {
        HttpClientBuilder builder = HttpClients.custom();
        // 读取环境变量
        String httpProxy = System.getenv("http_proxy");
        if (StringUtils.hasText(httpProxy)) {
            try {
                URI uri = URI.create(httpProxy);
                HttpHost proxy = new HttpHost(uri.getHost(), uri.getPort(), "http");
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
                builder.setRoutePlanner(routePlanner);
            } catch (Exception e) {
                log.error("配置http代理出现了异常", e);
            }
        }
        String httpsProxy = System.getenv("https_proxy");
        if (StringUtils.hasText(httpsProxy)) {
            try {
                URI uri = URI.create(httpsProxy);
                HttpHost proxy = new HttpHost(uri.getHost(), uri.getPort(), "https");
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
                builder.setRoutePlanner(routePlanner);
            } catch (Exception e) {
                log.error("配置https代理出现了异常", e);
            }
        }
        // http client的重试次数，默认是3次；当前是禁用掉
        return builder.setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).evictIdleConnections(httpClientProperties.getIdleTimeDuration(), DEFAULT_IDLE_TIME_UNIT).build();
    }

    public PoolingHttpClientConnectionManager createHttpClientConnectionManager() {
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            // 一定要带上第二个参数
            // noinspection deprecation
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(createIgnoreVerifySSL(),
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception e) {
            sslConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build();
        // 这里需要设置连接对象的生存时长，在清理空闲或失效时才有效
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry, null, null, null,
                httpClientProperties.getIdleTimeDuration(), DEFAULT_IDLE_TIME_UNIT);
        cm.setMaxTotal(httpClientProperties.getMaxConnectCount());
        cm.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoutCount());
        return cm;
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
