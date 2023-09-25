-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
CREATE TABLE "file_storage"
(
    "file_id"     integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "engine"      text(255) DEFAULT NULL,
    "bucket"      text(255) DEFAULT NULL,
    "name"        text DEFAULT NULL,
    "suffix"      text(255) DEFAULT NULL,
    "size_kb"     integer(20) DEFAULT NULL,
    "size"        text(255) DEFAULT NULL,
    "url"         text DEFAULT NULL,
    "thumbnail"   text DEFAULT NULL,
    "create_by"   text(64) DEFAULT '',
    "create_time" text DEFAULT NULL,
    "update_by"   text(64) DEFAULT '',
    "update_time" text DEFAULT NULL,
    "remark"      text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "sys_menu"
VALUES (126, '文件管理', 0, 3, 'file', 'file/storage/index', '', 1, 0, 'C', 0, 0, 'file:storage:list', 'copy', 'admin',
        '2023-09-21 10:15:33', '', NULL, '文件管理菜单');
INSERT INTO "sys_menu"
VALUES (1080, '文件存储查询', 126, 1, '', '', '', 1, 0, 'F', 0, 0, 'file:storage:query', '#', 'admin', '2023-09-21 10:15:33', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1081, '文件存储新增', 126, 2, '', '', '', 1, 0, 'F', 0, 0, 'file:storage:add', '#', 'admin', '2023-09-21 10:15:33', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1082, '文件存储修改', 126, 3, '', '', '', 1, 0, 'F', 0, 0, 'file:storage:edit', '#', 'admin', '2023-09-21 10:15:33', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1083, '文件存储删除', 126, 4, '', '', '', 1, 0, 'F', 0, 0, 'file:storage:remove', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO "sys_menu"
VALUES (1084, '文件存储导出', 126, 5, '', '', '', 1, 0, 'F', 0, 0, 'file:storage:export', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
