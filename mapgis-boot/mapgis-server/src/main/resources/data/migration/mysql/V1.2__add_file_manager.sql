-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
CREATE TABLE `file_storage`
(
    `file_id`     bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `engine`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储引擎',
    `bucket`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储桶',
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件名称',
    `suffix`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
    `size_kb`     bigint(20) NULL DEFAULT NULL COMMENT '文件大小kb',
    `size`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小（格式化后）',
    `url`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件地址',
    `thumbnail`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片缩略图',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '文件存储表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (126, '文件管理', 0, 3, 'file', 'file/storage/index', '', 1, 0, 'C', '0', '0', 'file:storage:list', 'copy', 'admin',
        '2023-09-21 10:15:33', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu`
VALUES (1080, '文件存储查询', 126, 1, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:query', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1081, '文件存储新增', 126, 2, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:add', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1082, '文件存储修改', 126, 3, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:edit', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1083, '文件存储删除', 126, 4, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:remove', '#', 'admin', '2023-09-21 10:15:33', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1084, '文件存储导出', 126, 5, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:export', '#', 'admin', '2023-09-21 10:15:33', '',
        NULL, '');
