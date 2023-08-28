package com.zondy.mapgis.file.service.impl;

import cn.hutool.core.io.IoUtil;
import com.zondy.mapgis.common.core.utils.file.FileUploadUtils;
import com.zondy.mapgis.file.config.MinioConfig;
import com.zondy.mapgis.file.service.IFileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Minio 文件存储
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class MinioSysFileServiceImpl implements IFileStorageService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        IoUtil.close(inputStream);
        return minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName;
    }
}
