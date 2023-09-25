package com.zondy.mapgis.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zondy.mapgis.file.api.domain.FileStorage;

import java.util.List;

/**
 * 文件Mapper接口
 *
 * @author mapgis
 * @date 2023-09-21
 */
public interface FileStorageMapper extends BaseMapper<FileStorage> {
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
     * 删除文件
     *
     * @param fileId 文件主键
     * @return 结果
     */
    public int deleteFileStorageByFileId(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFileStorageByFileIds(Long[] fileIds);
}
