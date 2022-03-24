package com.zondy.mapgis.file.api;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.file.api.domain.FileStorage;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务API，提供其他独立模块调用
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public interface IFileServiceApi {
    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    public R<FileStorage> upload(MultipartFile file);
}
