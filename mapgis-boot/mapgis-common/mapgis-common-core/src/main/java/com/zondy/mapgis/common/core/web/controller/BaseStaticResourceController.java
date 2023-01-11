package com.zondy.mapgis.common.core.web.controller;

import cn.hutool.core.io.IoUtil;
import com.zondy.mapgis.common.core.constant.ResourceConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础静态资源控制器
 *
 * @author xiongbo
 * @since 2023/1/11 18:09
 */
public class BaseStaticResourceController {
    private static final Map<String, String> DEFAULT_MEDIA_TYPES = new HashMap<>();

    static {
        // 摘自swagger插件代码
        DEFAULT_MEDIA_TYPES.put("html", "text/html");
        DEFAULT_MEDIA_TYPES.put("png", "image/png");
        DEFAULT_MEDIA_TYPES.put("gif", "image/gif");
        DEFAULT_MEDIA_TYPES.put("css", "text/css");
        DEFAULT_MEDIA_TYPES.put("js", "application/javascript");
        DEFAULT_MEDIA_TYPES.put("eot", "application/vnd.ms-fontobject");
        DEFAULT_MEDIA_TYPES.put("ttf", "application/font-sfnt");
        DEFAULT_MEDIA_TYPES.put("svg", "image/svg+xml");
        DEFAULT_MEDIA_TYPES.put("woff", "application/font-woff");
        DEFAULT_MEDIA_TYPES.put("woff2", "application/font-woff2");
        DEFAULT_MEDIA_TYPES.put("ico", "image/x-icon");
    }

    /**
     * 获取静态资源
     *
     * @param staticPath 静态资源位置
     * @return
     */
    public ResponseEntity<?> getFrontResource(String staticPath) {
        InputStream is = BaseController.class.getClassLoader().getResourceAsStream(staticPath);
        if (is == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            ResponseEntity.BodyBuilder rb = ResponseEntity.ok();
            int i = staticPath.lastIndexOf(".");
            if (i > -1) {
                String ext = staticPath.substring(i + 1);
                String type = DEFAULT_MEDIA_TYPES.get(ext);
                rb.contentType(MediaType.parseMediaType(type));
            }
            return rb.body(IoUtil.readBytes(is));
        }
    }

    public ResponseEntity<?> dispatchRestHtml() {
        return getFrontResource(ResourceConstants.SERVICES_UI_LOCATION + "/index.html");
    }

    public ResponseEntity<?> dispatchManagerHtml() {
        return getFrontResource(ResourceConstants.MANAGER_UI_LOCATION + "/index.html");
    }
}
