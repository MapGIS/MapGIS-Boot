package com.zondy.mapgis.file.service.impl;

import com.zondy.mapgis.common.core.utils.file.FileUploadUtils;
import com.zondy.mapgis.file.api.config.properties.FileProperties;
import com.zondy.mapgis.file.enums.FileEngineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class LocalSysFileServiceImpl extends FileStorageServiceImpl {
    @Autowired
    private FileProperties fileProperties;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception 异常
     */
    public String uploadFile(MultipartFile file) throws Exception {
        String name = FileUploadUtils.upload(fileProperties.getFullPath(), file, null);
        return fileProperties.getDomain() + fileProperties.getPrefix() + name;
    }

    @Override
    public int storageFile(MultipartFile file) {
        return storageFile(FileEngineType.LOCAL.getValue(), "", file);
    }
}
