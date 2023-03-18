/*
 Navicat Premium Data Transfer

 Source Server         : mapgis-xxx
 Source Server Type    : SQLite
 Source Server Version : 3030001
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3030001
 File Encoding         : 65001

 Date: 28/12/2022 15:31:11
*/

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "gen_table";
CREATE TABLE "gen_table"
(
    "table_id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "table_name"        text(200)  DEFAULT '',
    "table_comment"     text(500)  DEFAULT '',
    "sub_table_name"    text(64)   DEFAULT '/',
    "sub_table_fk_name" text(64)   DEFAULT NULL,
    "class_name"        text(100)  DEFAULT '',
    "tpl_category"      text(200)  DEFAULT 'crud',
    "package_name"      text(100)  DEFAULT NULL,
    "module_name"       text(30)   DEFAULT NULL,
    "business_name"     text(30)   DEFAULT NULL,
    "function_name"     text(50)   DEFAULT NULL,
    "function_author"   text(50)   DEFAULT NULL,
    "gen_type"          text(1)    DEFAULT '0',
    "gen_path"          text(200)  DEFAULT '/',
    "options"           text(1000) DEFAULT NULL,
    "create_by"         text(64)   DEFAULT '',
    "create_time"       text       DEFAULT NULL,
    "update_by"         text(64)   DEFAULT '',
    "update_time"       text       DEFAULT NULL,
    "remark"            text(500)  DEFAULT NULL
);

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS "gen_table_column";
CREATE TABLE "gen_table_column"
(
    "column_id"      integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "table_id"       text(64)    DEFAULT NULL,
    "column_name"    text(200)   DEFAULT NULL,
    "column_comment" text(500)   DEFAULT NULL,
    "column_type"    text(100)   DEFAULT NULL,
    "java_type"      text(500)   DEFAULT NULL,
    "java_field"     text(200)   DEFAULT NULL,
    "is_pk"          text(1)     DEFAULT NULL,
    "is_increment"   text(1)     DEFAULT NULL,
    "is_required"    text(1)     DEFAULT NULL,
    "is_insert"      text(1)     DEFAULT NULL,
    "is_edit"        text(1)     DEFAULT NULL,
    "is_list"        text(1)     DEFAULT NULL,
    "is_query"       text(1)     DEFAULT NULL,
    "query_type"     text(200)   DEFAULT 'EQ',
    "html_type"      text(200)   DEFAULT NULL,
    "dict_type"      text(200)   DEFAULT '',
    "sort"           integer(11) DEFAULT NULL,
    "create_by"      text(64)    DEFAULT '',
    "create_time"    text        DEFAULT NULL,
    "update_by"      text(64)    DEFAULT '',
    "update_time"    text        DEFAULT NULL
);

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_config
-- ----------------------------
DROP TABLE IF EXISTS "sys_auth_config";
CREATE TABLE "sys_auth_config"
(
    "config_id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "type"               text(255) DEFAULT '',
    "name"               text(255) DEFAULT '',
    "icon"               text      DEFAULT '',
    "help"               text(255) DEFAULT '',
    "client_id"          text(255) DEFAULT '',
    "client_secret"      text(255) DEFAULT '',
    "redirect_uri"       text(255) DEFAULT '',
    "auth_request_class" text(500) DEFAULT '',
    "status"             text(1)   DEFAULT '0',
    "create_by"          text(64)  DEFAULT '',
    "create_time"        text      DEFAULT NULL,
    "update_by"          text(64)  DEFAULT '',
    "update_time"        text      DEFAULT NULL,
    "remark"             text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_auth_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_auth_user";
CREATE TABLE "sys_auth_user"
(
    "auth_id"     integer   NOT NULL PRIMARY KEY AUTOINCREMENT,
    "uuid"        text(500) NOT NULL,
    "user_id"     integer(20) DEFAULT NULL,
    "login_name"  text(30)  NOT NULL,
    "user_name"   text(30)    DEFAULT '',
    "avatar"      text(500)   DEFAULT '',
    "email"       text(255)   DEFAULT '',
    "source"      text(255)   DEFAULT '',
    "create_time" text        DEFAULT NULL
);

-- ----------------------------
-- Records of sys_auth_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "sys_config";
CREATE TABLE "sys_config"
(
    "config_id"    integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "config_name"  text(100) DEFAULT '',
    "config_key"   text(100) DEFAULT '',
    "config_value" text      DEFAULT '',
    "config_type"  text(1)   DEFAULT 'N',
    "create_by"    text(64)  DEFAULT '',
    "create_time"  text      DEFAULT NULL,
    "update_by"    text(64)  DEFAULT '',
    "update_time"  text      DEFAULT NULL,
    "remark"       text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "sys_config"
VALUES (1, '安全配置-初始密码配置', 'security.initPassword', 'MapGIS123', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '用户初始密码');
INSERT INTO "sys_config"
VALUES (2, '安全配置-用户注册配置', 'security.register', '{"enabled":true,"defaultRoleIds":[3]}', 'Y', 'admin',
        '2022-03-23 22:12:32', '', NULL, '用户注册管理');
INSERT INTO "sys_config"
VALUES (3, '系统配置-基本配置', 'system.base',
        '{"header":{"defaultLogoAndTitle":true,"logo":"","title":""},"footer":{"copyright":"Copyright © 2023 武汉中地数码科技有限公司"}}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '系统基本配置');
INSERT INTO "sys_config"
VALUES (4, '安全配置-密码安全配置', 'security.passwordProtected',
        '{"enabled":true,"maxRetryCount":5,"lockTime":10,"isLockedByIp":true}', 'Y', 'admin', '2022-03-23 22:12:32', '',
        NULL, '密码安全配置');
INSERT INTO "sys_config"
VALUES (5, '安全配置-用户登录配置', 'security.login',
        '{"soloLoginEnabled":true,"captchaEnabled":true,"tipEnabled":true,"captchaType":"math","maxRetryCount":1,"recordTime":10}', 'Y',
        'admin', '2022-03-23 22:12:32', '', NULL, '用户登录配置');
INSERT INTO "sys_config"
VALUES (6, '安全配置-第三方登录配置', 'security.oauth', '{"defaultUserGroupIds":[1]}', 'Y', 'admin', '2022-03-23 22:12:32', '',
        NULL, '第三方登录配置');
INSERT INTO "sys_config"
VALUES (7, '安全配置-CAS登录配置', 'security.cas', '{"enabled":false,"isReserveDefaultLogin":false,"casServerUrl":""}', 'Y',
        'admin', '2022-03-23 22:12:32', '', NULL, 'CAS登录配置');
INSERT INTO "sys_config"
VALUES (8, '安全配置-LDAP登录配置', 'security.ldap',
        '{"enabled":false,"url":"","base":"","userDn":"","password":"","defaultUserGroupIds":[1],"roleMapping":[]}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, 'LDAP登录配置');
INSERT INTO "sys_config"
VALUES (9, '日志配置', 'log', '{"systemLoglevel":"INFO","httpAccessEnabled":false,"httpAccessMonitorUrls":[]}', 'Y',
        'admin', '2022-03-23 22:12:32', '', NULL, '日志配置');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "sys_dept";
CREATE TABLE "sys_dept"
(
    "dept_id"     integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "parent_id"   integer(20) DEFAULT 0,
    "ancestors"   text(50)    DEFAULT '',
    "dept_name"   text(30)    DEFAULT '',
    "order_num"   integer(4)  DEFAULT 0,
    "del_flag"    text(1)     DEFAULT '0',
    "create_by"   text(64)    DEFAULT '',
    "create_time" text        DEFAULT NULL,
    "update_by"   text(64)    DEFAULT '',
    "update_time" text        DEFAULT NULL
);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "sys_dept"
VALUES (100, 0, 0, '内置部门', 0, 0, 'admin', '2022-03-23 22:12:32', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_data";
CREATE TABLE "sys_dict_data"
(
    "dict_code"   integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "dict_sort"   integer(4) DEFAULT 0,
    "dict_label"  text(100)  DEFAULT '',
    "dict_value"  text(100)  DEFAULT '',
    "dict_type"   text(100)  DEFAULT '',
    "css_class"   text(100)  DEFAULT NULL,
    "list_class"  text(100)  DEFAULT NULL,
    "is_default"  text(1)    DEFAULT 'N',
    "status"      text(1)    DEFAULT '0',
    "create_by"   text(64)   DEFAULT '',
    "create_time" text       DEFAULT NULL,
    "update_by"   text(64)   DEFAULT '',
    "update_time" text       DEFAULT NULL,
    "remark"      text(500)  DEFAULT NULL
);

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "sys_dict_data"
VALUES (1, 1, '男', 0, 'sys_user_sex', '', '', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '性别男');
INSERT INTO "sys_dict_data"
VALUES (2, 2, '女', 1, 'sys_user_sex', '', '', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '性别女');
INSERT INTO "sys_dict_data"
VALUES (3, 1, '显示', 0, 'sys_show_hide', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '显示菜单');
INSERT INTO "sys_dict_data"
VALUES (4, 2, '隐藏', 1, 'sys_show_hide', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '隐藏菜单');
INSERT INTO "sys_dict_data"
VALUES (5, 1, '正常', 0, 'sys_normal_disable', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "sys_dict_data"
VALUES (6, 2, '停用', 1, 'sys_normal_disable', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO "sys_dict_data"
VALUES (7, 1, '正常', 0, 'sys_job_status', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "sys_dict_data"
VALUES (8, 2, '暂停', 1, 'sys_job_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO "sys_dict_data"
VALUES (9, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '默认分组');
INSERT INTO "sys_dict_data"
VALUES (10, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '系统分组');
INSERT INTO "sys_dict_data"
VALUES (11, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认是');
INSERT INTO "sys_dict_data"
VALUES (12, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认否');
INSERT INTO "sys_dict_data"
VALUES (13, 1, '通知', 1, 'sys_notice_type', '', 'warning', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '通知');
INSERT INTO "sys_dict_data"
VALUES (14, 2, '公告', 2, 'sys_notice_type', '', 'success', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '公告');
INSERT INTO "sys_dict_data"
VALUES (15, 1, '正常', 0, 'sys_notice_status', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "sys_dict_data"
VALUES (16, 2, '关闭', 1, 'sys_notice_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '关闭状态');
INSERT INTO "sys_dict_data"
VALUES (17, 99, '其他', 0, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-08-15 22:12:32', '', NULL, '其他操作');
INSERT INTO "sys_dict_data"
VALUES (18, 1, '新增', 1, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '新增操作');
INSERT INTO "sys_dict_data"
VALUES (19, 2, '修改', 2, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '修改操作');
INSERT INTO "sys_dict_data"
VALUES (20, 3, '删除', 3, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '删除操作');
INSERT INTO "sys_dict_data"
VALUES (21, 4, '授权', 4, 'sys_oper_type', '', 'primary', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '授权操作');
INSERT INTO "sys_dict_data"
VALUES (22, 5, '导出', 5, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '导出操作');
INSERT INTO "sys_dict_data"
VALUES (23, 6, '导入', 6, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '导入操作');
INSERT INTO "sys_dict_data"
VALUES (24, 7, '强退', 7, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '强退操作');
INSERT INTO "sys_dict_data"
VALUES (25, 8, '生成代码', 8, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '生成操作');
INSERT INTO "sys_dict_data"
VALUES (26, 9, '清空数据', 9, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '清空操作');
INSERT INTO "sys_dict_data"
VALUES (27, 1, '成功', 0, 'sys_common_status', '', 'primary', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "sys_dict_data"
VALUES (28, 2, '失败', 1, 'sys_common_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "sys_dict_type";
CREATE TABLE "sys_dict_type"
(
    "dict_id"     integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "dict_name"   text(100) DEFAULT '',
    "dict_type"   text(100) DEFAULT '',
    "status"      text(1)   DEFAULT '0',
    "create_by"   text(64)  DEFAULT '',
    "create_time" text      DEFAULT NULL,
    "update_by"   text(64)  DEFAULT '',
    "update_time" text      DEFAULT NULL,
    "remark"      text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "sys_dict_type"
VALUES (1, '用户性别', 'sys_user_sex', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '用户性别列表');
INSERT INTO "sys_dict_type"
VALUES (2, '菜单状态', 'sys_show_hide', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '菜单状态列表');
INSERT INTO "sys_dict_type"
VALUES (3, '系统开关', 'sys_normal_disable', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '系统开关列表');
INSERT INTO "sys_dict_type"
VALUES (4, '任务状态', 'sys_job_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '任务状态列表');
INSERT INTO "sys_dict_type"
VALUES (5, '任务分组', 'sys_job_group', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '任务分组列表');
INSERT INTO "sys_dict_type"
VALUES (6, '系统是否', 'sys_yes_no', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '系统是否列表');
INSERT INTO "sys_dict_type"
VALUES (7, '通知类型', 'sys_notice_type', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '通知类型列表');
INSERT INTO "sys_dict_type"
VALUES (8, '通知状态', 'sys_notice_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '通知状态列表');
INSERT INTO "sys_dict_type"
VALUES (9, '操作类型', 'sys_oper_type', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '操作类型列表');
INSERT INTO "sys_dict_type"
VALUES (10, '系统状态', 'sys_common_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "sys_job";
CREATE TABLE "sys_job"
(
    "job_id"          integer   NOT NULL PRIMARY KEY AUTOINCREMENT,
    "job_name"        text(64)  NOT NULL,
    "job_group"       text(64)  NOT NULL DEFAULT 'DEFAULT',
    "invoke_target"   text(500) NOT NULL,
    "cron_expression" text(255)          DEFAULT '',
    "misfire_policy"  text(20)           DEFAULT '3',
    "concurrent"      text(1)            DEFAULT '1',
    "status"          text(1)            DEFAULT '0',
    "create_by"       text(64)           DEFAULT '',
    "create_time"     text               DEFAULT NULL,
    "update_by"       text(64)           DEFAULT '',
    "update_time"     text               DEFAULT NULL,
    "remark"          text(500)          DEFAULT NULL
);

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "sys_job_log";
CREATE TABLE "sys_job_log"
(
    "job_log_id"     integer   NOT NULL PRIMARY KEY AUTOINCREMENT,
    "job_name"       text(64)  NOT NULL,
    "job_group"      text(64)  NOT NULL,
    "invoke_target"  text(500) NOT NULL,
    "job_message"    text(500)  DEFAULT NULL,
    "status"         text(1)    DEFAULT '0',
    "exception_info" text(2000) DEFAULT '',
    "create_time"    text       DEFAULT NULL
);

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS "sys_logininfor";
CREATE TABLE "sys_logininfor"
(
    "info_id"    integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_name"  text(50)  DEFAULT '',
    "ipaddr"     text(128) DEFAULT '',
    "browser"    text(50)  DEFAULT '',
    "os"         text(50)  DEFAULT '',
    "status"     text(1)   DEFAULT '0',
    "msg"        text(255) DEFAULT '',
    "login_time" text      DEFAULT NULL
);

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_menu";
CREATE TABLE "sys_menu"
(
    "menu_id"      integer  NOT NULL DEFAULT '' PRIMARY KEY AUTOINCREMENT,
    "menu_name"    text(50) NOT NULL,
    "parent_id"    integer(20)       DEFAULT 0,
    "order_num"    integer(4)        DEFAULT 0,
    "path"         text(200)         DEFAULT '',
    "component"    text(255)         DEFAULT NULL,
    "query_string" text(255)         DEFAULT NULL,
    "is_frame"     integer(1)        DEFAULT 1,
    "is_cache"     integer(1)        DEFAULT 0,
    "menu_type"    text(1)           DEFAULT '',
    "visible"      text(1)           DEFAULT '0',
    "status"       text(1)           DEFAULT '0',
    "perms"        text(100)         DEFAULT NULL,
    "icon"         text(100)         DEFAULT '#',
    "create_by"    text(64)          DEFAULT '',
    "create_time"  text              DEFAULT NULL,
    "update_by"    text(64)          DEFAULT '',
    "update_time"  text              DEFAULT NULL,
    "remark"       text(500)         DEFAULT NULL
);

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "sys_menu"
VALUES (1, '监控管理', 0, 1, 'monitor', NULL, '', 1, 0, 'M', 0, 0, '', 'monitor', 'admin', '2022-03-23 22:12:32', '', NULL,
        '监控管理目录');
INSERT INTO "sys_menu"
VALUES (2, '日志管理', 0, 2, 'log', NULL, '', 1, 0, 'M', 0, 0, '', 'log', 'admin', '2022-03-23 22:12:32', '', NULL,
        '日志管理目录');
INSERT INTO "sys_menu"
VALUES (3, '安全管理', 0, 3, 'security', NULL, '', 1, 0, 'M', 0, 0, '', 'safety-certificate', 'admin',
        '2022-03-23 22:12:32', '', NULL, '安全管理目录');
INSERT INTO "sys_menu"
VALUES (4, '计划任务', 0, 4, 'schedule', NULL, '', 1, 0, 'M', 0, 0, '', 'job', 'admin', '2022-10-11 14:20:47', '', NULL,
        '定时任务目录');
INSERT INTO "sys_menu"
VALUES (5, '消息中心', 0, 5, 'message', NULL, '', 1, 0, 'M', 0, 0, '', 'bell', 'admin', '2022-10-12 08:56:41', '', NULL,
        '消息中心目录');
INSERT INTO "sys_menu"
VALUES (6, '开发扩展', 0, 6, 'dev', NULL, '', 1, 0, 'M', 0, 0, '', 'tool', 'admin', '2022-03-23 22:12:32', '', NULL,
        '开发扩展目录');
INSERT INTO "sys_menu"
VALUES (7, '系统配置', 0, 7, 'config', NULL, '', 1, 0, 'M', 0, 0, '', 'setting', 'admin', '2022-10-11 18:33:09', '', NULL,
        '系统设置目录');
INSERT INTO "sys_menu"
VALUES (100, '在线用户', 1, 1, 'online', 'monitor/online/index', '', 1, 1, 'C', 0, 0, 'monitor:online:list', 'online',
        'admin', '2022-03-23 22:12:32', '', NULL, '在线用户菜单');
INSERT INTO "sys_menu"
VALUES (101, '服务器监控', 1, 2, 'server', 'monitor/server/index', '', 1, 1, 'C', 0, 0, 'monitor:server:list', 'server',
        'admin', '2022-03-23 22:12:32', '', NULL, '服务器监控菜单');
INSERT INTO "sys_menu"
VALUES (102, '登录日志', 2, 1, 'logininfor', 'system/logininfor/index', '', 1, 1, 'C', 0, 0, 'system:logininfor:list',
        'logininfor', 'admin', '2022-03-23 22:12:32', '', NULL, '登录日志菜单');
INSERT INTO "sys_menu"
VALUES (103, '操作日志', 2, 2, 'operlog', 'system/operlog/index', '', 1, 1, 'C', 0, 0, 'system:operlog:list', 'form',
        'admin', '2022-03-23 22:12:32', '', NULL, '操作日志菜单');
INSERT INTO "sys_menu"
VALUES (104, '用户管理', 3, 2, 'user', 'system/user/index', '', 1, 0, 'C', 0, 0, 'system:user:list', 'user', 'admin',
        '2022-03-23 22:12:32', '', NULL, '用户管理菜单');
INSERT INTO "sys_menu"
VALUES (105, '角色管理', 3, 4, 'role', 'system/role/index', '', 1, 0, 'C', 0, 0, 'system:role:list', 'peoples', 'admin',
        '2022-03-23 22:12:32', '', NULL, '角色管理菜单');
INSERT INTO "sys_menu"
VALUES (106, '菜单管理', 3, 5, 'menu', 'system/menu/index', '', 1, 0, 'C', 0, 0, 'system:menu:list', 'treeTable', 'admin',
        '2022-03-23 22:12:32', '', NULL, '菜单管理菜单');
INSERT INTO "sys_menu"
VALUES (107, '部门管理', 3, 6, 'dept', 'system/dept/index', '', 1, 0, 'C', 0, 0, 'system:dept:list', 'tree', 'admin',
        '2022-03-23 22:12:32', '', NULL, '部门管理菜单');
INSERT INTO "sys_menu"
VALUES (108, '岗位管理', 3, 7, 'post', 'system/post/index', '', 1, 0, 'C', 0, 0, 'system:post:list', 'post', 'admin',
        '2022-03-23 22:12:32', '', NULL, '岗位管理菜单');
INSERT INTO "sys_menu"
VALUES (109, '定时任务', 4, 1, 'job', 'monitor/job/index', '', 1, 0, 'C', 0, 0, 'monitor:job:list', 'job', 'admin',
        '2022-03-23 22:12:32', '', NULL, '定时任务菜单');
INSERT INTO "sys_menu"
VALUES (110, '通知公告', 5, 1, 'notice', 'system/notice/index', '', 1, 0, 'C', 0, 0, 'system:notice:list', 'message',
        'admin', '2022-03-23 22:12:32', '', NULL, '通知公告菜单');
INSERT INTO "sys_menu"
VALUES (111, '接口文档', 6, 1, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', 0, 0, 'tool:swagger:list', 'swagger',
        'admin', '2022-03-23 22:12:32', '', NULL, '系统接口菜单');
INSERT INTO "sys_menu"
VALUES (112, '数据字典', 6, 2, 'dict', 'system/dict/index', '', 1, 0, 'C', 0, 0, 'system:dict:list', 'dict', 'admin',
        '2022-03-23 22:12:32', '', NULL, '字典管理菜单');
INSERT INTO "sys_menu"
VALUES (113, '代码生成', 6, 3, 'gen', 'tool/gen/index', '', 1, 0, 'C', 0, 0, 'tool:gen:list', 'code', 'admin',
        '2022-03-23 22:12:32', '', NULL, '代码生成菜单');
INSERT INTO "sys_menu"
VALUES (114, '微应用路由配置', 6, 4, 'microApp', 'system/microApp/index', '', 1, 0, 'C', 0, 0, 'system:microApp:list',
        'deployment-unit', 'admin', '2022-09-26 13:54:31', '', NULL, '微应用路由配置菜单');
INSERT INTO "sys_menu"
VALUES (115, '安全配置', 3, 1, 'securityConfig', 'security/config/index', '', 1, 0, 'C', 0, 0, 'system:config:query',
        'validCode', 'admin', '2022-10-14 17:51:53', '', NULL, '安全配置菜单');
INSERT INTO "sys_menu"
VALUES (116, '基本配置', 7, 1, 'base', 'config/base/index', '', 1, 0, 'C', 0, 0, 'system:config:query', 'profile', 'admin',
        '2022-10-15 15:45:11', '', NULL, '基本配置菜单');
INSERT INTO "sys_menu"
VALUES (117, '第三方登录配置', 3, 8, 'oauth', 'security/authConfig/index', NULL, 1, 0, 'C', 0, 0, 'system:authConfig:list',
        'security-scan', 'admin', '2022-10-21 16:11:18', '', NULL, '第三方登录配置菜单');
INSERT INTO "sys_menu"
VALUES (118, 'CAS登录配置', 3, 9, 'cas', 'security/casConfig/index', NULL, 1, 0, 'C', 0, 0, 'system:config:query', 'cas',
        'admin', '2022-10-27 14:55:00', '', NULL, 'CAS登录配置菜单');
INSERT INTO "sys_menu"
VALUES (119, '主题配置', 7, 2, 'theme', 'config/theme/index', NULL, 1, 0, 'C', 0, 0, '', 'skin', 'admin',
        '2022-10-31 11:39:51', '', NULL, '主题配置菜单');
INSERT INTO "sys_menu"
VALUES (120, 'LDAP登录配置', 3, 10, 'ldap', 'security/ldapConfig/index', NULL, 1, 0, 'C', 0, 0, 'system:config:query',
        'ldap', 'admin', '2022-11-03 14:55:44', '', NULL, 'LDAP登录配置菜单');
INSERT INTO "sys_menu"
VALUES (121, '系统日志', 2, 3, 'systemlog', 'log/systemlog/index', NULL, 1, 1, 'C', 0, 0, 'system:systemlog:list', 'edit',
        'admin', '2022-11-23 11:07:53', '', NULL, '系统日志菜单');
INSERT INTO "sys_menu"
VALUES (122, '服务访问日志', 2, 4, 'assesslog', 'log/assesslog/index', NULL, 1, 1, 'C', 0, 0, 'system:assesslog:list',
        'file-text', 'admin', '2022-11-28 17:23:54', '', NULL, '服务访问日志菜单');
INSERT INTO "sys_menu"
VALUES (123, '日志配置', 2, 5, 'logConfig', 'log/config/index', NULL, 1, 0, 'C', 0, 0, 'system:config:query', 'file-done',
        'admin', '2022-11-30 11:45:39', '', NULL, '日志配置菜单');
INSERT INTO "sys_menu"
VALUES (124, '服务器性能监控', 1, 3, 'performance', 'monitor/performance/index', NULL, 1, 1, 'C', 0, 0,
        'monitor:serverPerformance:list', 'fund', 'admin', '2022-12-06 10:54:54', '', NULL, '服务器性能监控菜单');
INSERT INTO "sys_menu"
VALUES (125, '用户组管理', 3, 3, 'usergroup', 'system/usergroup/index', NULL, 1, 0, 'C', 0, 0, 'system:usergroup:list',
        'team', 'admin', '2022-12-23 16:32:49', '', NULL, '用户组管理菜单');
INSERT INTO "sys_menu"
VALUES (1000, '在线查询', 100, 1, '', '', '', 1, 0, 'F', 0, 0, 'monitor:online:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1001, '批量强退', 100, 2, '', '', '', 1, 0, 'F', 0, 0, 'monitor:online:batchLogout', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1002, '单条强退', 100, 3, '', '', '', 1, 0, 'F', 0, 0, 'monitor:online:forceLogout', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1003, '登录查询', 102, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:logininfor:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1004, '登录删除', 102, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:logininfor:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1005, '日志导出', 102, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:logininfor:export', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1006, '账号解锁', 102, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:logininfor:unlock', '#', 'admin',
        '2022-08-15 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1007, '操作查询', 103, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:operlog:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1008, '操作删除', 103, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:operlog:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1009, '日志导出', 103, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:operlog:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1010, '用户查询', 104, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:user:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1011, '用户新增', 104, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:user:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1012, '用户修改', 104, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:user:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1013, '用户删除', 104, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:user:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1014, '用户导出', 104, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:user:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1015, '用户导入', 104, 6, '', '', '', 1, 0, 'F', 0, 0, 'system:user:import', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1016, '重置密码', 104, 7, '', '', '', 1, 0, 'F', 0, 0, 'system:user:resetPwd', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1017, '角色查询', 105, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:role:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1018, '角色新增', 105, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:role:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1019, '角色修改', 105, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:role:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1020, '角色删除', 105, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:role:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1021, '角色导出', 105, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:role:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1022, '菜单查询', 106, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:menu:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1023, '菜单新增', 106, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:menu:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1024, '菜单修改', 106, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:menu:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1025, '菜单删除', 106, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:menu:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1026, '部门查询', 107, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:dept:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1027, '部门新增', 107, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:dept:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1028, '部门修改', 107, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:dept:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1029, '部门删除', 107, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:dept:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1030, '岗位导出', 108, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:post:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1031, '岗位查询', 108, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:post:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1032, '岗位新增', 108, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:post:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1033, '岗位修改', 108, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:post:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1034, '岗位删除', 108, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:post:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1035, '任务查询', 109, 1, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1036, '任务新增', 109, 2, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1037, '任务修改', 109, 3, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1038, '任务删除', 109, 4, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1039, '状态修改', 109, 5, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:changeStatus', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1040, '任务导出', 109, 7, '', '', '', 1, 0, 'F', 0, 0, 'monitor:job:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1041, '公告查询', 110, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:notice:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1042, '公告新增', 110, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:notice:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1043, '公告修改', 110, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:notice:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1044, '公告删除', 110, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:notice:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1045, '字典查询', 112, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:dict:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1046, '字典新增', 112, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:dict:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1047, '字典修改', 112, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:dict:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1048, '字典删除', 112, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:dict:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1049, '字典导出', 112, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:dict:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1050, '生成查询', 113, 1, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:query', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1051, '生成修改', 113, 2, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:edit', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1052, '生成删除', 113, 3, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:remove', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1053, '导入代码', 113, 4, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:import', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1054, '预览代码', 113, 5, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:preview', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1055, '生成代码', 113, 6, '', '', '', 1, 0, 'F', 0, 0, 'tool:gen:code', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1056, '微应用路由查询', 114, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:microApp:query', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1057, '微应用路由新增', 114, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:microApp:add', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1058, '微应用路由修改', 114, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:microApp:edit', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1059, '微应用路由删除', 114, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:microApp:remove', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1060, '微应用路由导出', 114, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:microApp:export', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1061, '配置修改', 115, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin', '2022-10-14 17:54:02',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1062, '配置修改', 116, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin', '2022-10-15 15:45:38',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1063, '第三方登录配置查询', 117, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:authConfig:query', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1064, '第三方登录配置新增', 117, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:authConfig:add', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1065, '第三方登录配置修改', 117, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:authConfig:edit', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1066, '第三方登录配置删除', 117, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:authConfig:remove', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1067, '第三方登录配置导出', 117, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:authConfig:export', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1068, '第三方登录角色配置查询', 117, 6, '', '', '', 1, 0, 'F', 0, 0, 'system:config:query', '#', 'admin',
        '2022-11-03 14:07:26', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1069, '第三方登录角色配置修改', 117, 7, '', '', '', 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin',
        '2022-11-03 14:08:45', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1070, '配置修改', 118, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin', '2022-10-27 14:55:41',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1071, '配置修改', 120, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin', '2022-11-03 14:56:11',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1072, '日志导出', 121, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:systemlog:export', '#', 'admin',
        '2022-11-23 14:52:25', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1073, '日志导出', 122, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:assesslog:export', '#', 'admin',
        '2022-11-28 17:23:54', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1074, '配置修改', 123, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:config.edit', '#', 'admin', '2022-11-30 15:10:47',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1075, '用户组查询', 125, 1, '', '', '', 1, 0, 'F', 0, 0, 'system:usergroup:query', '#', 'admin',
        '2022-12-23 16:32:50', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1076, '用户组新增', 125, 2, '', '', '', 1, 0, 'F', 0, 0, 'system:usergroup:add', '#', 'admin', '2022-12-23 16:32:50',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1077, '用户组修改', 125, 3, '', '', '', 1, 0, 'F', 0, 0, 'system:usergroup:edit', '#', 'admin',
        '2022-12-23 16:32:50', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1078, '用户组删除', 125, 4, '', '', '', 1, 0, 'F', 0, 0, 'system:usergroup:remove', '#', 'admin',
        '2022-12-23 16:32:50', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1079, '用户组导出', 125, 5, '', '', '', 1, 0, 'F', 0, 0, 'system:usergroup:export', '#', 'admin',
        '2022-12-23 16:32:50', '', NULL, '');

-- ----------------------------
-- Table structure for sys_micro_app
-- ----------------------------
DROP TABLE IF EXISTS "sys_micro_app";
CREATE TABLE "sys_micro_app"
(
    "micro_app_id" integer   NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"         text(255) NOT NULL,
    "entry"        text(255) NOT NULL,
    "active_rule"  text(255) NOT NULL,
    "create_by"    text(64) DEFAULT '',
    "create_time"  text     DEFAULT NULL,
    "update_by"    text(64) DEFAULT '',
    "update_time"  text     DEFAULT NULL
);

-- ----------------------------
-- Records of sys_micro_app
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "sys_notice";
CREATE TABLE "sys_notice"
(
    "notice_id"      integer  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "notice_title"   text(50) NOT NULL,
    "notice_type"    text(1)  NOT NULL,
    "notice_content" text(2000) DEFAULT NULL,
    "status"         text(1)    DEFAULT '0',
    "create_by"      text(64)   DEFAULT '',
    "create_time"    text       DEFAULT NULL,
    "update_by"      text(64)   DEFAULT '',
    "update_time"    text       DEFAULT NULL,
    "remark"         text(255)  DEFAULT NULL
);

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "sys_oper_log";
CREATE TABLE "sys_oper_log"
(
    "oper_id"        integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "title"          text(50)   DEFAULT '',
    "business_type"  integer(2) DEFAULT 0,
    "method"         text(100)  DEFAULT '',
    "request_method" text(10)   DEFAULT '',
    "operator_type"  integer(1) DEFAULT 0,
    "oper_name"      text(50)   DEFAULT '',
    "dept_name"      text(50)   DEFAULT '',
    "oper_url"       text(255)  DEFAULT '',
    "oper_ip"        text(128)  DEFAULT '',
    "oper_param"     text(2000) DEFAULT '',
    "json_result"    text(2000) DEFAULT '',
    "status"         integer(1) DEFAULT 0,
    "error_msg"      text(2000) DEFAULT '',
    "oper_time"      text       DEFAULT NULL
);

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "sys_post";
CREATE TABLE "sys_post"
(
    "post_id"     integer    NOT NULL PRIMARY KEY AUTOINCREMENT,
    "post_code"   text(64)   NOT NULL,
    "post_name"   text(50)   NOT NULL,
    "post_sort"   integer(4) NOT NULL,
    "create_by"   text(64)  DEFAULT '',
    "create_time" text      DEFAULT NULL,
    "update_by"   text(64)  DEFAULT '',
    "update_time" text      DEFAULT NULL,
    "remark"      text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_role";
CREATE TABLE "sys_role"
(
    "role_id"             integer    NOT NULL PRIMARY KEY AUTOINCREMENT,
    "role_name"           text(30)   NOT NULL,
    "role_key"            text(100)  NOT NULL,
    "role_sort"           integer(4) NOT NULL,
    "data_scope"          text(1)    DEFAULT '1',
    "menu_check_strictly" integer(1) DEFAULT 1,
    "dept_check_strictly" integer(1) DEFAULT 1,
    "is_sys"              integer(1) DEFAULT 0,
    "del_flag"            text(1)    DEFAULT '0',
    "create_by"           text(64)   DEFAULT '',
    "create_time"         text       DEFAULT NULL,
    "update_by"           text(64)   DEFAULT '',
    "update_time"         text       DEFAULT NULL,
    "remark"              text(500)  DEFAULT NULL
);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "sys_role"
VALUES (1, '系统管理员', 'admin', 1, 1, 1, 1, 1, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '内置的系统管理员角色，拥有整个服务器的管理权限');
INSERT INTO "sys_role"
VALUES (2, '第三方用户', 'third', 2, 5, 1, 1, 1, 0, 'admin', '2022-10-24 11:44:51', '', NULL, '内置的第三方用户角色，表示用户是使用第三方登录');
INSERT INTO "sys_role"
VALUES (3, '普通用户', 'user', 3, 1, 1, 1, 1, 0, 'admin', '2022-10-17 15:18:18', '', NULL, '内置的普通用户角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_dept";
CREATE TABLE "sys_role_dept"
(
    "role_id" integer(20) NOT NULL,
    "dept_id" integer(20) NOT NULL,
    PRIMARY KEY ("role_id", "dept_id")
);

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "sys_role_menu";
CREATE TABLE "sys_role_menu"
(
    "role_id" integer(20) NOT NULL,
    "menu_id" integer(20) NOT NULL,
    PRIMARY KEY ("role_id", "menu_id")
);

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "sys_role_menu"
VALUES (2, 6);
INSERT INTO "sys_role_menu"
VALUES (2, 7);
INSERT INTO "sys_role_menu"
VALUES (2, 111);
INSERT INTO "sys_role_menu"
VALUES (2, 119);
INSERT INTO "sys_role_menu"
VALUES (3, 6);
INSERT INTO "sys_role_menu"
VALUES (3, 7);
INSERT INTO "sys_role_menu"
VALUES (3, 111);
INSERT INTO "sys_role_menu"
VALUES (3, 119);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user";
CREATE TABLE "sys_user"
(
    "user_id"     integer  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "dept_id"     integer(20) DEFAULT NULL,
    "user_name"   text(30) NOT NULL,
    "nick_name"   text(30) NOT NULL,
    "email"       text(50)    DEFAULT '',
    "phonenumber" text(11)    DEFAULT '',
    "sex"         text(1)     DEFAULT '0',
    "avatar"      text(100)   DEFAULT '',
    "password"    text(100)   DEFAULT '',
    "status"      text(1)     DEFAULT '0',
    "del_flag"    text(1)     DEFAULT '0',
    "login_ip"    text(128)   DEFAULT '',
    "login_date"  date        DEFAULT NULL,
    "create_by"   text(64)    DEFAULT '',
    "create_time" text        DEFAULT NULL,
    "update_by"   text(64)    DEFAULT '',
    "update_time" text        DEFAULT NULL,
    "remark"      text(500)   DEFAULT NULL
);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "sys_user"
VALUES (1, 100, 'admin', '系统管理员', '', '', 0, '', '$2a$10$W0oZaiVL8yYLG6PQsM2f4uYFI9kkS424BVArwpSHozx7FxdCijOfq', 0, 0,
        '', NULL, 'admin', '2022-03-23 22:12:32', '', NULL, '系统管理员');

-- ----------------------------
-- Table structure for sys_user_group
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_group";
CREATE TABLE "sys_user_group"
(
    "user_group_id"   integer  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_group_name" text(30) NOT NULL,
    "create_by"       text(64)  DEFAULT '',
    "create_time"     text      DEFAULT NULL,
    "update_by"       text(64)  DEFAULT '',
    "update_time"     text      DEFAULT NULL,
    "remark"          text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_user_group
-- ----------------------------
INSERT INTO "sys_user_group"
VALUES (1, '第三方用户组', 'admin', '2022-12-27 11:30:51', '', NULL, '内置的第三方用户组');

-- ----------------------------
-- Table structure for sys_user_group_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_group_role";
CREATE TABLE "sys_user_group_role"
(
    "user_group_id" integer(20) NOT NULL,
    "role_id"       integer(20) NOT NULL,
    PRIMARY KEY ("user_group_id", "role_id")
);

-- ----------------------------
-- Records of sys_user_group_role
-- ----------------------------
INSERT INTO "sys_user_group_role"
VALUES (1, 2);

-- ----------------------------
-- Table structure for sys_user_group_user
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_group_user";
CREATE TABLE "sys_user_group_user"
(
    "user_group_id" integer(20) NOT NULL,
    "user_id"       integer(20) NOT NULL,
    PRIMARY KEY ("user_group_id", "user_id")
);

-- ----------------------------
-- Records of sys_user_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_post";
CREATE TABLE "sys_user_post"
(
    "user_id" integer(20) NOT NULL,
    "post_id" integer(20) NOT NULL,
    PRIMARY KEY ("user_id", "post_id")
);

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO "sys_user_post"
VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "sys_user_role";
CREATE TABLE "sys_user_role"
(
    "user_id" integer(20) NOT NULL,
    "role_id" integer(20) NOT NULL,
    PRIMARY KEY ("user_id", "role_id")
);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "sys_user_role"
VALUES (1, 1);

-- ----------------------------
-- Indexes structure for table sys_dict_type
-- ----------------------------
CREATE UNIQUE INDEX "dict_type"
    ON "sys_dict_type" (
                        "dict_type" ASC
        );

