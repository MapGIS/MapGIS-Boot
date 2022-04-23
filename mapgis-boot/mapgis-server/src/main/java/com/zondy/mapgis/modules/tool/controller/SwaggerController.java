package com.zondy.mapgis.modules.tool.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * swagger 接口
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@ManagerRestController("/tool/swagger")
public class SwaggerController extends BaseController {
    @GetMapping()
    public String index() {
        return redirect("/swagger-ui.html");
    }
}
