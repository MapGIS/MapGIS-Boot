package com.zondy.mapgis.modules.tool.controller;

import com.zondy.mapgis.common.core.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * swagger 接口
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController {
    @GetMapping()
    public String index() {
        return redirect("/swagger-ui.html");
    }
}
