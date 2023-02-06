package com.zondy.mapgis.common.log.enums;

/**
 * 业务操作类型
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
public enum BusinessType {
    /**
     * 其它
     */
    OTHER,

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 授权
     */
    GRANT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 生成代码
     */
    GENCODE,

    /**
     * 清空数据
     */
    CLEAN,

    /**
     * 保存数据，包括新增或更新
     */
    SAVE,

    /**
     * 发布
     */
    PUBLISH,

    /**
     * 开启
     */
    START,

    /**
     * 停止
     */
    STOP
}
