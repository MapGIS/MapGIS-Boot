package com.zondy.mapgis.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zondy.mapgis.file.api.domain.FileStorage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件Service接口
 *
 * @author mapgis
 * @date 2023-09-21
 */
public interface IFileStorageService extends IService<FileStorage> {
    /**
     * 查询文件
     *
     * @param fileId 文件主键
     * @return 文件
     */
    public FileStorage selectFileStorageByFileId(Long fileId);

    /**
     * 查询文件列表
     *
     * @param fileStorage 文件
     * @return 文件集合
     */
    public List<FileStorage> selectFileStorageList(FileStorage fileStorage);

    /**
     * 文件上传接口（不需要保存到数据库中）
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception;

    /**
     * 文件存储接口（需要保存到数据库中）
     *
     * @param file 上传的文件
     * @return 结果
     */
    public int storageFile(MultipartFile file);

    /**
     * 新增文件
     *
     * @param fileStorage 文件
     * @return 结果
     */
    public int insertFileStorage(FileStorage fileStorage);

    /**
     * 修改文件
     *
     * @param fileStorage 文件
     * @return 结果
     */
    public int updateFileStorage(FileStorage fileStorage);

    /**
     * 批量删除文件
     *
     * @param fileIds 需要删除的文件主键集合
     * @return 结果
     */
    public int deleteFileStorageByFileIds(Long[] fileIds);

    /**
     * 删除文件信息
     *
     * @param fileId 文件主键
     * @return 结果
     */
    public int deleteFileStorageByFileId(Long fileId);
}
