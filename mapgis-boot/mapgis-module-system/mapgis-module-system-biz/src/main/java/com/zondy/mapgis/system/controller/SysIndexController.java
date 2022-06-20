package com.zondy.mapgis.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 *
 * @author powanjuanshu
 * @since 2022/6/14 11:31
 */
@Controller
public class SysIndexController {
    /**
     * 系统首页
     *
     * @return
     */
    @GetMapping(value = {"/", "/xxx/manager/**"})
    public String index() {
        return "forward:/xxx/static/index.html";
    }
}
