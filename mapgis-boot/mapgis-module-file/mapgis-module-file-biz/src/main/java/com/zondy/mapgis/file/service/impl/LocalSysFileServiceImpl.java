package com.zondy.mapgis.file.service.impl;

import com.zondy.mapgis.common.core.utils.file.FileUploadUtils;
import com.zondy.mapgis.file.api.config.properties.FileProperties;
import com.zondy.mapgis.file.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class LocalSysFileServiceImpl implements IFileStorageService {
    @Autowired
    private FileProperties fileProperties;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String name = FileUploadUtils.upload(fileProperties.getFullPath(), file);
        String url = fileProperties.getDomain() + fileProperties.getPrefix() + name;
        return url;
    }
}
