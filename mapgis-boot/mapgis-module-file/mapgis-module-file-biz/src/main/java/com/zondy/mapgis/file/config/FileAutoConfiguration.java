package com.zondy.mapgis.file.config;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zondy.mapgis.file.service.impl.FastDfsSysFileServiceImpl;
import com.zondy.mapgis.file.service.impl.LocalSysFileServiceImpl;
import com.zondy.mapgis.file.service.impl.MinioSysFileServiceImpl;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author powanjuanshu
 * @since 2022/7/21 18:37
 */
@Configuration
@ComponentScan({"com.zondy.mapgis.file.controller", "com.zondy.mapgis.file.service.impl"})
public class FileAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "mapgis.file.store", havingValue = "local", matchIfMissing = true)
    @Import({LocalSysFileServiceImpl.class})
    static class LocalFileStoreConfig {
    }

    @Configuration
    @ConditionalOnProperty(name = "mapgis.file.store", havingValue = "fastdfs")
    @ConditionalOnClass({FastFileStorageClient.class})
    @Import({FastDfsSysFileServiceImpl.class})
    static class FastdfsFileStoreConfig {
    }

    @Configuration
    @ConditionalOnProperty(name = "mapgis.file.store", havingValue = "minio")
    @ConditionalOnClass({MinioClient.class})
    @Import({MinioSysFileServiceImpl.class, MinioConfig.class})
    static class MinioFileStoreConfig {
    }
}