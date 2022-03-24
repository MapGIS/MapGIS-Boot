package com.zondy.mapgis.common.core.config.properties;

import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Data
@Configuration
public class FileProperties {
    /**
     * 资源映射路径前缀
     */
    @Value("${file.prefix}")
    public String prefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain:}")
    public String domain;

    /**
     * 上传文件存储在本地的路径
     */
    @Value("${file.path}")
    private String path;

    /**
     * 获取完整路径
     * @return 完整路径
     */
    public String getFullPath() {
        String currentPath = EnvironmentUtil.getCurrentProjectPath();

        return currentPath + path;
    }
}
