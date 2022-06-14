package com.zondy.mapgis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 *
 * @author powanjuanshu
 * @since 2022/6/14 11:31
 */
@Controller
public class IndexController {
    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping(value = {"/", "/xxx/**"})
    public String index() {
        return "forward:/static/index.html";
    }
}
