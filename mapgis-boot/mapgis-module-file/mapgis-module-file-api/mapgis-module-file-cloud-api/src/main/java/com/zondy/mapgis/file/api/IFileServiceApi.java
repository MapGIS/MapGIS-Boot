package com.zondy.mapgis.file.api;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.file.api.domain.FileStorage;
import com.zondy.mapgis.file.api.factory.RemoteFileServiceApiFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务API，提供其他独立模块调用
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@FeignClient(contextId = "remoteFileServiceApi", value = "${mapgis.product.full-name}-file-server", path = "${api.path.manager-prefix}", fallbackFactory = RemoteFileServiceApiFallbackFactory.class)
public interface IFileServiceApi {
    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/file/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<FileStorage> upload(@RequestPart(value = "file") MultipartFile file);
}
