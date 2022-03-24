package com.zondy.mapgis.file.controller;

import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.file.FileUtils;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.file.api.domain.FileStorage;
import com.zondy.mapgis.file.service.IFileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件请求处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Api(value = "文件存储控制器", tags = {"文件存储管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/file")
public class FileStorageController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FileStorageController.class);

    private final IFileStorageService fileStorageService;

    /**
     * 上传文件
     */
    @ApiOperation("上传文件")
    @PostMapping("upload")
    public AjaxResult upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = fileStorageService.uploadFile(file);
            FileStorage fileStorage = new FileStorage();
            fileStorage.setName(FileUtils.getName(url));
            fileStorage.setUrl(url);
            return AjaxResult.success(fileStorage);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败！");
        }
    }
}