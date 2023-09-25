package com.zondy.mapgis.file.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.file.MimeTypeUtils;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.file.api.domain.FileStorage;
import com.zondy.mapgis.file.mapper.FileStorageMapper;
import com.zondy.mapgis.file.service.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * 文件Service业务层处理
 *
 * @author mapgis
 * @date 2023-09-21
 */
public abstract class FileStorageServiceImpl extends ServiceImpl<FileStorageMapper, FileStorage> implements IFileStorageService {
    @Autowired
    private FileStorageMapper fileStorageMapper;

    /**
     * 查询文件
     *
     * @param fileId 文件主键
     * @return 文件
     */
    @Override
    public FileStorage selectFileStorageByFileId(Long fileId) {
        return fileStorageMapper.selectFileStorageByFileId(fileId);
    }

    /**
     * 查询文件列表
     *
     * @param fileStorage 文件
     * @return 文件
     */
    @Override
    public List<FileStorage> selectFileStorageList(FileStorage fileStorage) {
        return fileStorageMapper.selectFileStorageList(fileStorage);
    }

    /**
     * 新增文件
     *
     * @param fileStorage 文件
     * @return 结果
     */
    @Override
    public int insertFileStorage(FileStorage fileStorage) {
        return fileStorageMapper.insertFileStorage(fileStorage);
    }

    /**
     * 修改文件
     *
     * @param fileStorage 文件
     * @return 结果
     */
    @Override
    public int updateFileStorage(FileStorage fileStorage) {
        return fileStorageMapper.updateFileStorage(fileStorage);
    }

    /**
     * 批量删除文件
     *
     * @param fileIds 需要删除的文件主键
     * @return 结果
     */
    @Override
    public int deleteFileStorageByFileIds(Long[] fileIds) {
        return fileStorageMapper.deleteFileStorageByFileIds(fileIds);
    }

    /**
     * 删除文件信息
     *
     * @param fileId 文件主键
     * @return 结果
     */
    @Override
    public int deleteFileStorageByFileId(Long fileId) {
        return fileStorageMapper.deleteFileStorageByFileId(fileId);
    }

    /**
     * 新增文件
     *
     * @param engine     引擎
     * @param bucketName 存储桶
     * @param file       文件
     * @return 结果
     */
    protected int storageFile(String engine, String bucketName, MultipartFile file) {
        String url;

        try {
            // 上传并返回访问地址
            url = uploadFile(file);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败！");
        }

        // 将文件信息保存到数据库
        FileStorage fileStorage = new FileStorage();

        // 设置存储引擎类型
        fileStorage.setEngine(engine);
        // 设置存储桶
        fileStorage.setBucket(bucketName);
        // 设置文件名
        fileStorage.setName(file.getOriginalFilename());
        String suffix = ObjectUtil.isNotEmpty(file.getOriginalFilename()) ? StrUtil.subAfter(file.getOriginalFilename(),
                StrUtil.DOT, true) : null;
        // 设置文件后缀
        fileStorage.setSuffix(suffix);
        // 设置文件大小kb
        fileStorage.setSizeKb(Convert.toStr(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024))
                .setScale(0, BigDecimal.ROUND_HALF_UP)));
        // 设置文件大小（格式化后）
        fileStorage.setSize(FileUtil.readableFileSize(file.getSize()));
        // 如果是图片，则压缩生成缩略图
        if (ObjectUtil.isNotEmpty(suffix)) {
            if (StringUtils.equalsAnyIgnoreCase(suffix, MimeTypeUtils.IMAGE_EXTENSION)) {
                try {
                    fileStorage.setThumbnail(ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(file.getBytes()),
                            100, 100, null), suffix));
                } catch (Exception ignored) {
                }
            }
        }
        // 设置存储路径
        fileStorage.setUrl(url);

        // 设置创建者
        fileStorage.setCreateBy(SecurityUtils.getUsername());

        // 插入到数据库
        return fileStorageMapper.insertFileStorage(fileStorage);
    }
}
