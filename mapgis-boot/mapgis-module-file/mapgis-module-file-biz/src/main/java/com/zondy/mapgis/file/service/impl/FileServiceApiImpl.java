package com.zondy.mapgis.file.service.impl;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.common.core.utils.file.FileUtils;
import com.zondy.mapgis.file.api.IFileServiceApi;
import com.zondy.mapgis.file.api.domain.FileStorage;
import com.zondy.mapgis.file.service.IFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class FileServiceApiImpl implements IFileServiceApi {
    private static final Logger log = LoggerFactory.getLogger(FileServiceApiImpl.class);

    @Autowired
    private IFileStorageService fileStorageService;

    @Override
    public R<FileStorage> upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = fileStorageService.uploadFile(file);
            FileStorage fileStorage = new FileStorage();
            fileStorage.setName(FileUtils.getName(url));
            fileStorage.setUrl(url);
            return R.ok(fileStorage);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }
}
