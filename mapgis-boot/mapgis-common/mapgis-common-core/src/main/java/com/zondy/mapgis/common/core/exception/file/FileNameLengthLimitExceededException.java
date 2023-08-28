package com.zondy.mapgis.common.core.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength}, "the filename is too long");
    }
}
