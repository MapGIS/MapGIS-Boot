package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.core.config.ProductConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 *
 * @author powanjuanshu
 * @since 2022/6/14 11:31
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Controller
public class SysIndexController {

    private final ProductConfig productConfig;

    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping(value = {"/", "/${mapgis.product.name}/manager/**"})
    public String index() {
        return "forward:/" + productConfig.getName() + "/static/index.html";
    }
}
