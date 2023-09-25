package com.zondy.mapgis.file.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.common.core.utils.file.FileUtils;
import com.zondy.mapgis.common.core.utils.poi.ExcelUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.file.api.domain.FileStorage;
import com.zondy.mapgis.file.service.IFileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件Controller
 *
 * @author mapgis
 * @date 2023-09-21
 */
@Tag(name = "文件管理", description = "文件控制器")
@ManagerRestController("/file")
@Slf4j
public class FileStorageController extends BaseController {
    @Autowired
    private IFileStorageService fileStorageService;

    /**
     * 查询文件列表
     */
    @Operation(summary = "查询文件列表")
    @PreAuthorize("@ss.hasPermi('file:storage:list')")
    @RequiresPermissions("file:storage:list")
    @GetMapping("/list")
    public TableDataInfo list(FileStorage fileStorage) {
        startPage();
        List<FileStorage> list = fileStorageService.selectFileStorageList(fileStorage);
        return getDataTable(list);
    }

    /**
     * 导出文件列表
     */
    @Operation(summary = "导出文件列表")
    @PreAuthorize("@ss.hasPermi('file:storage:export')")
    @RequiresPermissions("file:storage:export")
    @Log(title = "文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FileStorage fileStorage) {
        List<FileStorage> list = fileStorageService.selectFileStorageList(fileStorage);
        ExcelUtil<FileStorage> util = new ExcelUtil<FileStorage>(FileStorage.class);
        util.exportExcel(response, list, "文件数据");
    }

    /**
     * 获取文件详细信息
     */
    @Operation(summary = "获取文件详细信息")
    @PreAuthorize("@ss.hasPermi('file:storage:query')")
    @RequiresPermissions("file:storage:query")
    @GetMapping(value = "/{fileId}")
    public AjaxResult getInfo(@PathVariable("fileId") Long fileId) {
        return success(fileStorageService.selectFileStorageByFileId(fileId));
    }

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件")
    @PostMapping("upload")
    public AjaxResult upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = fileStorageService.uploadFile(file);
            FileStorage fileStorage = new FileStorage();
            fileStorage.setName(FileUtils.getName(url));
            fileStorage.setUrl(url);
            return success(fileStorage);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new ServiceException("上传文件失败！");
        }
    }

    /**
     * 新增文件
     */
    @Operation(summary = "新增文件")
    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(MultipartFile file) {
        return toAjax(fileStorageService.storageFile(file));
    }

    /**
     * 修改文件
     */
    @Operation(summary = "修改文件")
    @PreAuthorize("@ss.hasPermi('file:storage:edit')")
    @RequiresPermissions("file:storage:edit")
    @Log(title = "文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FileStorage fileStorage) {
        fileStorage.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(fileStorageService.updateFileStorage(fileStorage));
    }

    /**
     * 删除文件
     */
    @Operation(summary = "删除文件")
    @PreAuthorize("@ss.hasPermi('file:storage:remove')")
    @RequiresPermissions("file:storage:remove")
    @Log(title = "文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public AjaxResult remove(@PathVariable Long[] fileIds) {
        return toAjax(fileStorageService.deleteFileStorageByFileIds(fileIds));
    }
}
