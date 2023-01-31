package com.zondy.mapgis.system.controller;

import com.zondy.mapgis.common.controllerprefix.annotation.ManagerRestController;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.poi.ExcelUtil;
import com.zondy.mapgis.common.core.web.controller.BaseController;
import com.zondy.mapgis.common.core.web.domain.AjaxResult;
import com.zondy.mapgis.common.core.web.page.TableDataInfo;
import com.zondy.mapgis.common.log.annotation.Log;
import com.zondy.mapgis.common.log.enums.BusinessType;
import com.zondy.mapgis.common.security.annotation.RequiresPermissions;
import com.zondy.mapgis.common.security.utils.SecurityUtils;
import com.zondy.mapgis.system.api.domain.SysDictData;
import com.zondy.mapgis.system.service.ISysDictDataService;
import com.zondy.mapgis.system.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Tag(name = "数据字典信息管理", description = "数据字典信息控制器")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ManagerRestController("/system/dict/data")
public class SysDictDataController extends BaseController {

    private final ISysDictDataService dictDataService;

    private final ISysDictTypeService dictTypeService;

    @Operation(summary = "查询字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Operation(summary = "导出字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @RequiresPermissions("system:dict:export")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }

    /**
     * 查询字典数据详细
     */
    @Operation(summary = "查询字典数据详细")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return success(data);
    }

    /**
     * 新增字典类型
     */
    @Operation(summary = "新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @Operation(summary = "修改保存字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @Operation(summary = "删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
