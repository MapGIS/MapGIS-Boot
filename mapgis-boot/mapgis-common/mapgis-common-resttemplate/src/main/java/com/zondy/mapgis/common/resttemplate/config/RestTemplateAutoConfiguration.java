package com.zondy.mapgis.common.resttemplate.config;

import com.zondy.mapgis.common.resttemplate.http.HttpClientFactory;
import com.zondy.mapgis.common.resttemplate.properties.HttpClientProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 自动配置
 *
 * @author xiongbo
 * @since 2023/1/11 11:56
 */
@Configuration
public class RestTemplateAutoConfiguration {
    /**
     * RestTemplate HttpClient自动配置
     */
    @Configuration
    static class HttpClientAutoConfiguration {
        @Bean
        public RestTemplate restTemplate(HttpMessageConverters httpMessageConverters, HttpClientProperties httpClientProperties) {
            return createRestTemplate(httpMessageConverters, httpClientProperties);
        }

        private RestTemplate createRestTemplate(HttpMessageConverters httpMessageConverters, HttpClientProperties httpClientProperties) {
            HttpClientFactory factory = new HttpClientFactory(httpClientProperties);
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(factory.createHttpClient());
            clientHttpRequestFactory.setBufferRequestBody(false);
            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            restTemplate.setMessageConverters(httpMessageConverters.getConverters());
            return restTemplate;
        }
    }
}
