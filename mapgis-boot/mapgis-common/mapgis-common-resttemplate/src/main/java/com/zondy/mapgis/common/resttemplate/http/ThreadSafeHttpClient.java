package com.zondy.mapgis.common.resttemplate.http;

import cn.hutool.core.io.IoUtil;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import com.zondy.mapgis.common.resttemplate.config.properties.HttpClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 基于httpclient4.5
 * 支持多线程的共享httpClient，httpClient的连接对象使用连接池管理，很大的节省httpClient频繁开启关闭时损耗性能
 *
 * @author Chelsea
 * @since 2023/1/11 12:00
 */
@Slf4j
public class ThreadSafeHttpClient {
    private static class Holder {
        private final static ThreadSafeHttpClient INSTANCE = new ThreadSafeHttpClient();
    }

    private final CloseableHttpClient httpclient;

    private ThreadSafeHttpClient() {
        HttpClientProperties httpClientProperties = null;
        try {
            httpClientProperties = SpringUtils.getBean(HttpClientProperties.class);
        } catch (Exception e) {
            httpClientProperties = new HttpClientProperties();
        }
        HttpClientFactory factory = new HttpClientFactory(httpClientProperties);
        httpclient = factory.createHttpClient();
    }

    public static ThreadSafeHttpClient getInstance() {
        return Holder.INSTANCE;
    }

    public static int requestHttpStatus(final HttpUriRequest request) {
        try {
            try (CloseableHttpResponse response = getInstance().httpclient.execute(request)) {
                return response.getStatusLine().getStatusCode();
            }
        } catch (IOException ignored) {
        }
        return 0;
    }

    public static String getForString(String url) {
        return getForString(url, true);
    }

    public static String getForString(String url, boolean quietly) {
        HttpGet httpGet = new HttpGet(url);
        try {
            return getInstance().requestForStringInternal(httpGet);
        } catch (IOException e) {
            if (!quietly) {
                log.warn("HTTP ERROR -- GET -- URL：" + httpGet.getURI().toString(), e);
            }
        }

        return null;
    }

    private String requestForStringInternal(final HttpUriRequest request) throws IOException {
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String ret = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            EntityUtils.consumeQuietly(entity);
            if (ret != null && ret.indexOf(65279) == 0) {
                return ret.substring(1);
            }
            return ret;
        }
    }

    public static byte[] getForBytes(String url) {
        return getForBytes(url, true);
    }

    public static byte[] getForBytes(String url, boolean quietly) {
        HttpGet httpGet = new HttpGet(url);
        //模拟浏览器的请求头,解决天地图的华为云防火墙拦截问题
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        try {
            return getInstance().requestForBytesInternal(httpGet);
        } catch (IOException e) {
            if (!quietly) {
                log.warn("HTTP ERROR -- GET -- URL：" + httpGet.getURI().toString(), e);
            }
        }
        return null;
    }

    private byte[] requestForBytesInternal(final HttpUriRequest request) throws IOException {
        try (CloseableHttpResponse response = httpclient.execute(request)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                EntityUtils.consumeQuietly(entity);
                return bytes;
            }
        }
        return null;
    }

    public static boolean download(String sourceURL, String savePath) throws IOException {
        HttpGet httpGet = new HttpGet(sourceURL);
        CloseableHttpResponse response = null;
        try {
            response = getInstance().httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                FileOutputStream stream = new FileOutputStream(savePath);
                entity.writeTo(stream);
                stream.close();
                EntityUtils.consume(entity);
                return true;
            }
        } finally {
            IoUtil.close(response);
        }
        return false;
    }

}
