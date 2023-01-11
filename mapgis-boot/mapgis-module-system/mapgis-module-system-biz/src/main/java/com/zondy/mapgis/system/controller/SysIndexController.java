package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.core.config.ProductConfig;
import com.zondy.mapgis.common.core.web.controller.BaseStaticResourceController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * 首页控制器
 *
 * @author xiongbo
 * @since 2022/6/14 11:31
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
public class SysIndexController extends BaseStaticResourceController {

    private final ProductConfig productConfig;

    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getIndex() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(URI.create("/" + productConfig.getName() + "/manager")).build();
    }

    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping("/${mapgis.product.name}/manager/**")
    public ResponseEntity<?> getManagerFrontend() {
        return dispatchManagerHtml();
    }
}
