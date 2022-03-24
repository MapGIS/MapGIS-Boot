package com.zondy.mapgis.file.api.factory;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.file.api.IFileServiceApi;
import com.zondy.mapgis.file.api.domain.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务降级处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class RemoteFileServiceApiFallbackFactory implements FallbackFactory<IFileServiceApi> {
    private static final Logger log = LoggerFactory.getLogger(RemoteFileServiceApiFallbackFactory.class);

    @Override
    public IFileServiceApi create(Throwable throwable) {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new IFileServiceApi() {
            @Override
            public R<FileStorage> upload(MultipartFile file) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }
        };
    }
}
