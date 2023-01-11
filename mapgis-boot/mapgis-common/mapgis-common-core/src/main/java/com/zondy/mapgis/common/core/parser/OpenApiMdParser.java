package com.zondy.mapgis.common.core.parser;

import cn.hutool.core.io.IoUtil;
import com.zondy.mapgis.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * open api解析器
 *
 * @author xiongbo
 * @since 2023/1/10 18:05
 */
@Slf4j
public class OpenApiMdParser {
    /**
     * open api中md标签格式为 {{api-docs/map-server.md}}，注意文件夹名称只能为字母数字-_,不能以-开头
     */
    public static final Pattern MD_REGEX = Pattern.compile("\\{\\{ *((?!-)[A-z0-9-_]+/)*((?!-)[A-z0-9-_]+)\\.md *}}");

    /**
     * open api中md解析
     *
     * @param description
     * @return
     */
    public String parse(String description) {
        if (StringUtils.isBlank(description)) {
            return description;
        }
        Matcher matcher = MD_REGEX.matcher(description);
        while (matcher.find()) {
            String content = matcher.group();
            String mdPath = content.substring(2);
            mdPath = mdPath.substring(0, mdPath.length() - 2);
            mdPath = mdPath.trim();

            log.info("解析到md代码块:" + content);
            String mdContent = getMdContent(mdPath);
            if (mdContent != null) {
                // 修正换行符为 LF
                mdContent = mdContent.replace("\r\n", "\n");
                description = description.replace(content, mdContent);
            }
        }
        return description;
    }

    public String getMdContent(String mdClassPath) {
        if (StringUtils.isNotBlank(mdClassPath)) {
            if (mdClassPath.startsWith("/")) {
                mdClassPath = mdClassPath.substring(1);
            }
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(mdClassPath);
            if (is != null) {
                return IoUtil.read(is, StandardCharsets.UTF_8);
            }
        }
        return null;
    }
}
