package com.zondy.mapgis.common.core.exception.file;

import com.zondy.mapgis.common.core.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
