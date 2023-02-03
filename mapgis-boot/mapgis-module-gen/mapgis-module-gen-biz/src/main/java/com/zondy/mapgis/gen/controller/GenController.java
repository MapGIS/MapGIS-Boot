package com.zondy.mapgis.gen.controller;

import cn.hutool.core.io.IoUtil;
import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.text.Convert;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.gen.domain.GenTable;
import com.zondy.mapgis.gen.domain.GenTableColumn;
import com.zondy.mapgis.gen.service.IGenTableColumnService;
import com.zondy.mapgis.gen.service.IGenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiongbo
 * @since 2022/8/21 15:22
 */
@Tag(name = "代码生成管理", description = "代码生成控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/gen")
public class GenController extends BaseController {
    private final IGenTableService genTableService;

    private final IGenTableColumnService genTableColumnService;

    /**
     * 查询代码生成列表
     */
    @Operation(summary = "查询代码生成列表")
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @RequiresPermissions("tool:gen:list")
    @GetMapping("/list")
    public TableDataInfo genList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 修改代码生成业务
     */
    @Operation(summary = "修改代码生成业务")
    @PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @RequiresPermissions("tool:gen:query")
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("info", table);
        map.put("rows", list);
        map.put("tables", tables);
        return success(map);
    }

    /**
     * 查询数据库列表
     */
    @Operation(summary = "查询数据库列表")
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @RequiresPermissions("tool:gen:list")
    @GetMapping("/db/list")
    public TableDataInfo dataList(GenTable genTable) {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return getDataTable(list);
    }

    /**
     * 查询数据表字段列表
     */
    @Operation(summary = "查询数据表字段列表")
    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @RequiresPermissions("tool:gen:list")
    @GetMapping(value = "/column/{tableId}")
    public TableDataInfo columnList(Long tableId) {
        TableDataInfo dataInfo = new TableDataInfo();
        List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    /**
     * 导入表结构（保存）
     */
    @Operation(summary = "导入表结构（保存）")
    @PreAuthorize("@ss.hasPermi('tool:gen:import')")
    @RequiresPermissions("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(String tables) {
        String[] tableNames = Convert.toStrArray(tables);
        // 查询表信息
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList);
        return success();
    }

    /**
     * 修改保存代码生成业务
     */
    @Operation(summary = "修改保存代码生成业务")
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @RequiresPermissions("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTable genTable) {
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        return success();
    }

    /**
     * 删除代码生成
     */
    @Operation(summary = "删除代码生成")
    @PreAuthorize("@ss.hasPermi('tool:gen:remove')")
    @RequiresPermissions("tool:gen:remove")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds) {
        genTableService.deleteGenTableByIds(tableIds);
        return success();
    }

    /**
     * 预览代码
     */
    @Operation(summary = "预览代码")
    @PreAuthorize("@ss.hasPermi('tool:gen:preview')")
    @RequiresPermissions("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return success(dataMap);
    }

    /**
     * 生成代码（下载方式）
     */
    @Operation(summary = "生成代码（下载方式）")
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成代码（自定义路径）
     */
    @Operation(summary = "生成代码（自定义路径）")
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName) {
        genTableService.generatorCode(tableName);
        return success();
    }

    /**
     * 同步数据库
     */
    @Operation(summary = "同步数据库")
    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @RequiresPermissions("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public AjaxResult synchDb(@PathVariable("tableName") String tableName) {
        genTableService.synchDb(tableName);
        return success();
    }

    /**
     * 批量生成代码
     */
    @Operation(summary = "批量生成代码")
    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @RequiresPermissions("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"mapgis.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), false, data);
    }
}
