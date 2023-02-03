package com.zondy.mapgis.common.service.factory;

import cn.hutool.core.util.StrUtil;
import com.zondy.mapgis.common.core.utils.xml.XmlSerialUtils;
import com.zondy.mapgis.common.service.exception.web.InternalServerErrorException;
import com.zondy.mapgis.common.service.exception.web.NotFoundException;
import com.zondy.mapgis.common.service.format.ResponseFormat;
import com.zondy.mapgis.common.json.JacksonUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 响应工厂
 *
 * @author Chelsea
 * @since 2023/1/10 15:42
 */
public class ResponseFactory {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
    public static final MediaType APPLICATION_XML_UTF8 = new MediaType(MediaType.APPLICATION_XML, StandardCharsets.UTF_8);
    public static final MediaType TEXT_PLAIN_UTF8 = new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);
    public static final MediaType TEXT_HTML_UTF8 = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
    public static final MediaType APPLICATION_PROTOBUF = new MediaType("application", "x-protobuf");

    public MediaType convertContentType(String contentType, MediaType defaultValue) {
        if (StrUtil.isNotBlank(contentType)) {
            String[] arr = contentType.split("/");
            if (arr.length > 1) {
                return new MediaType(arr[0], arr[1]);
            } else {
                return new MediaType(arr[0]);
            }
        }
        return defaultValue;
    }

    /**
     * 创建响应对象
     * ResponseFormat.GEOJSON未处理，由上层进行扩展
     *
     * @param t           响应内容
     * @param format      响应格式
     * @param contentType 内容格式
     * @param status      状态
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<?> createResponse(T t, ResponseFormat format, String contentType, HttpStatus status) {
        if (t == null) {
            return createServerErrorTextResponse("response is empty");
        }
        if (ResponseFormat.JSON.equals(format)) {
            String json = t instanceof String ? (String) t : JacksonUtils.toJson(t);
            if (StringUtils.hasText(json)) {
                return ResponseEntity.status(status).contentType(APPLICATION_JSON_UTF8).body(json);
            } else {
                return createServerErrorTextResponse("json serialize failed");
            }
        } else if (ResponseFormat.IMAGE.equals(format)) {
            if (t instanceof byte[]) {
                return ResponseEntity.status(status).contentType(convertContentType(contentType, MediaType.APPLICATION_OCTET_STREAM)).body(t);
            } else {
                return createServerErrorTextResponse("image format unsupported");
            }
        } else if (ResponseFormat.XML.equals(format)) {
            String xml = t instanceof String ? (String) t : XmlSerialUtils.toXML(t);
            if (StringUtils.hasText(xml)) {
                return ResponseEntity.status(status).contentType(APPLICATION_XML_UTF8).body(xml);
            } else {
                return createServerErrorTextResponse("xml format failed");
            }
        } else if (ResponseFormat.TEXT.equals(format)) {
            String text = t instanceof String ? (String) t : t.toString();
            if (StringUtils.hasText(text)) {
                return ResponseEntity.status(status).contentType(convertContentType(contentType, TEXT_PLAIN_UTF8)).body(text);
            } else {
                return createServerErrorTextResponse("text is empty");
            }
        } else if (ResponseFormat.PBF.equals(format)) {
            if (t instanceof byte[]) {
                return ResponseEntity.status(status).contentType(convertContentType(contentType, APPLICATION_PROTOBUF)).body(t);
            } else {
                return createServerErrorTextResponse("pbf format unsupported");
            }
        } else if (ResponseFormat.HTML.equals(format)) {
            String text = t instanceof String ? (String) t : t.toString();
            if (StringUtils.hasText(text)) {
                return ResponseEntity.status(status).contentType(convertContentType(contentType, TEXT_HTML_UTF8)).body(text);
            } else {
                return createServerErrorTextResponse("html format failed");
            }
        }

        return createServerErrorTextResponse("response format unsupported");
    }

    /**
     * 创建文件流响应体
     *
     * @param file    文件
     * @param headers 返回头
     * @return 响应体
     */
    public ResponseEntity<?> createFileResponse(File file, HttpHeaders headers) {
        if (!file.exists()) {
            throw new NotFoundException("请求的文件'" + file + "'不存在");
        }
        if (file.length() == 0) {
            throw new NotFoundException("请求的文件'" + file + "'内容为空");
        }
        FileSystemResource fsr = new FileSystemResource(file);
        if (!fsr.isReadable()) {
            throw new InternalServerErrorException("请求的文件'" + file + "'没有读取权限");
        }
        return ResponseEntity.ok().headers(headers).body(fsr);
    }

    public <T> ResponseEntity<?> createResponse(T t, ResponseFormat format) {
        return createResponse(t, format, null, HttpStatus.OK);
    }

    public ResponseEntity<?> createArcCallbackResponse(String json, String callback) {
        return createResponse(callback + "(" + json + ")", ResponseFormat.TEXT, null, HttpStatus.OK);
    }

    public <T> ResponseEntity<?> createResponse(T t, ResponseFormat format, String contentType) {
        return createResponse(t, format, contentType, HttpStatus.OK);
    }

    public <T> ResponseEntity<?> createResponse(T t, ResponseFormat format, HttpStatus status) {
        return createResponse(t, format, null, status);
    }

    public ResponseEntity<String> createServerErrorTextResponse(String msg) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(TEXT_PLAIN_UTF8).body(msg);
    }

    public ResponseEntity<?> createClientErrorTextResponse(String msg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(TEXT_PLAIN_UTF8).body(msg);
    }

    public ResponseEntity<?> create404Response(String msg) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(TEXT_PLAIN_UTF8).body(msg);
    }

    public ResponseEntity<?> createOGCServiceErrorResponse(HttpStatus status, String msg) {
        return ResponseEntity.status(status).contentType(APPLICATION_XML_UTF8).body(msg);
    }
}
