package com.zondy.mapgis.file.service.impl;

import cn.hutool.core.io.IoUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zondy.mapgis.common.core.utils.file.FileTypeUtils;
import com.zondy.mapgis.file.enums.FileEngineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * FastDFS 文件存储
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class FastDfsSysFileServiceImpl extends FileStorageServiceImpl {
    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = storageClient.uploadFile(inputStream, file.getSize(),
                FileTypeUtils.getExtension(file), null);
        IoUtil.close(inputStream);
        return domain + "/" + storePath.getFullPath();
    }

    /**
     * 文件存储接口（需要保存到数据库中）
     *
     * @param file 上传的文件
     * @return 结果
     */
    @Override
    public int storageFile(MultipartFile file) {
        return storageFile(FileEngineType.FASTDFS.getValue(), "", file);
    }
}
