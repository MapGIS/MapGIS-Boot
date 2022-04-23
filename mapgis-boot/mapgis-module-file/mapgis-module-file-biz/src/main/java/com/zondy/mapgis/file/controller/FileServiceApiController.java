package com.zondy.mapgis.file.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.file.api.IFileServiceApi;
import com.zondy.mapgis.file.api.domain.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@ManagerRestController("/file/api")
public class FileServiceApiController extends BaseController {

    @Autowired
    @Qualifier("FileServiceApiImpl")
    private IFileServiceApi fileServiceApi;

    /**
     * 上传文件
     */
    @PostMapping("upload")
    public R<FileStorage> upload(@RequestPart(value = "file") MultipartFile file) {
        return fileServiceApi.upload(file);
    }
}
