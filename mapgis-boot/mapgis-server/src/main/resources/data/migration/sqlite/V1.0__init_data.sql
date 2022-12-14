/*
 Navicat Premium Data Transfer

 Source Server         : mapgis-boot
 Source Server Type    : SQLite
 Source Server Version : 3030001
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3030001
 File Encoding         : 65001

 Date: 26/09/2022 14:40:59
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
    "config_value" text(500) DEFAULT '',
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
VALUES (1, '????????????-??????????????????', 'sys.user.initPassword', 123456, 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '??????????????? 123456');
INSERT INTO "sys_config"
VALUES (2, '????????????-??????????????????????????????', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '?????????????????????????????????true?????????false?????????');

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
    "leader"      text(20)    DEFAULT NULL,
    "phone"       text(11)    DEFAULT NULL,
    "email"       text(50)    DEFAULT NULL,
    "status"      text(1)     DEFAULT '0',
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
VALUES (100, 0, 0, '??????????????????', 0, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin', '2022-03-23 22:12:32', '',
        NULL);
INSERT INTO "sys_dept"
VALUES (101, 100, '0,100', '????????????', 1, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin', '2022-03-23 22:12:32',
        '', NULL);
INSERT INTO "sys_dept"
VALUES (102, 100, '0,100', '????????????', 2, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin', '2022-03-23 22:12:32',
        '', NULL);
INSERT INTO "sys_dept"
VALUES (103, 101, '0,100,101', '????????????', 1, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (104, 101, '0,100,101', '????????????', 2, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (105, 101, '0,100,101', '????????????', 3, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (106, 101, '0,100,101', '????????????', 4, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (107, 101, '0,100,101', '????????????', 5, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (108, 102, '0,100,102', '????????????', 1, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);
INSERT INTO "sys_dept"
VALUES (109, 102, '0,100,102', '????????????', 2, 'MapGIS', 13888888888, 'mapgis@mapgis.com', 0, 0, 'admin',
        '2022-03-23 22:12:32', '', NULL);

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
VALUES (1, 1, '???', 0, 'sys_user_sex', '', '', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '?????????');
INSERT INTO "sys_dict_data"
VALUES (2, 2, '???', 1, 'sys_user_sex', '', '', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '?????????');
INSERT INTO "sys_dict_data"
VALUES (3, 3, '??????', 2, 'sys_user_sex', '', '', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (4, 1, '??????', 0, 'sys_show_hide', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (5, 2, '??????', 1, 'sys_show_hide', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (6, 1, '??????', 0, 'sys_normal_disable', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (7, 2, '??????', 1, 'sys_normal_disable', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (8, 1, '??????', 0, 'sys_job_status', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (9, 2, '??????', 1, 'sys_job_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (10, 1, '??????', 'DEFAULT', 'sys_job_group', '', '', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (11, 2, '??????', 'SYSTEM', 'sys_job_group', '', '', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (12, 1, '???', 'Y', 'sys_yes_no', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '???????????????');
INSERT INTO "sys_dict_data"
VALUES (13, 2, '???', 'N', 'sys_yes_no', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '???????????????');
INSERT INTO "sys_dict_data"
VALUES (14, 1, '??????', 1, 'sys_notice_type', '', 'warning', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????');
INSERT INTO "sys_dict_data"
VALUES (15, 2, '??????', 2, 'sys_notice_type', '', 'success', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????');
INSERT INTO "sys_dict_data"
VALUES (16, 1, '??????', 0, 'sys_notice_status', '', 'primary', 'Y', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (17, 2, '??????', 1, 'sys_notice_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (18, 99, '??????', 0, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-08-15 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (19, 1, '??????', 1, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (20, 2, '??????', 2, 'sys_oper_type', '', 'info', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (21, 3, '??????', 3, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (22, 4, '??????', 4, 'sys_oper_type', '', 'primary', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (23, 5, '??????', 5, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (24, 6, '??????', 6, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (25, 7, '??????', 7, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (26, 8, '????????????', 8, 'sys_oper_type', '', 'warning', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (27, 9, '????????????', 9, 'sys_oper_type', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (28, 1, '??????', 0, 'sys_common_status', '', 'primary', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');
INSERT INTO "sys_dict_data"
VALUES (29, 2, '??????', 1, 'sys_common_status', '', 'danger', 'N', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');

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
VALUES (1, '????????????', 'sys_user_sex', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (2, '????????????', 'sys_show_hide', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (3, '????????????', 'sys_normal_disable', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (4, '????????????', 'sys_job_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (5, '????????????', 'sys_job_group', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (6, '????????????', 'sys_yes_no', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (7, '????????????', 'sys_notice_type', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (8, '????????????', 'sys_notice_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (9, '????????????', 'sys_oper_type', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_dict_type"
VALUES (10, '????????????', 'sys_common_status', 0, 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');

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
VALUES (1, '????????????', 0, 1, 'system', NULL, NULL, 1, 0, 'M', 0, 0, '', 'system', 'admin', '2022-03-23 22:12:32', '', NULL,
        '??????????????????');
INSERT INTO "sys_menu"
VALUES (2, '????????????', 0, 2, 'monitor', NULL, NULL, 1, 0, 'M', 0, 0, '', 'monitor', 'admin', '2022-03-23 22:12:32', '',
        NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (3, '????????????', 0, 3, 'tool', NULL, NULL, 1, 0, 'M', 0, 0, '', 'tool', 'admin', '2022-03-23 22:12:32', '', NULL,
        '??????????????????');
INSERT INTO "sys_menu"
VALUES (100, '????????????', 1, 1, 'user', 'system/user/index', NULL, 1, 0, 'C', 0, 0, 'system:user:list', 'user', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (101, '????????????', 1, 2, 'role', 'system/role/index', NULL, 1, 0, 'C', 0, 0, 'system:role:list', 'peoples', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (102, '????????????', 1, 3, 'menu', 'system/menu/index', NULL, 1, 0, 'C', 0, 0, 'system:menu:list', 'treeTable', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (103, '????????????', 1, 4, 'dept', 'system/dept/index', NULL, 1, 0, 'C', 0, 0, 'system:dept:list', 'tree', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (104, '????????????', 1, 5, 'post', 'system/post/index', NULL, 1, 0, 'C', 0, 0, 'system:post:list', 'post', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (105, '????????????', 1, 6, 'dict', 'system/dict/index', NULL, 1, 0, 'C', 0, 0, 'system:dict:list', 'dict', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (106, '????????????', 1, 7, 'config', 'system/config/index', NULL, 1, 0, 'C', 0, 0, 'system:config:list', 'edit',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (107, '????????????', 1, 8, 'notice', 'system/notice/index', NULL, 1, 0, 'C', 0, 0, 'system:notice:list', 'message',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (108, '????????????', 1, 9, 'log', '', NULL, 1, 0, 'M', 0, 0, '', 'log', 'admin', '2022-03-23 22:12:32', '', NULL,
        '??????????????????');
INSERT INTO "sys_menu"
VALUES (109, '????????????', 2, 1, 'online', 'monitor/online/index', NULL, 1, 0, 'C', 0, 0, 'monitor:online:list', 'online',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (110, '????????????', 2, 2, 'job', 'monitor/job/index', NULL, 1, 0, 'C', 0, 0, 'monitor:job:list', 'job', 'admin',
        '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (111, '????????????', 2, 3, 'druid', 'monitor/druid/index', NULL, 1, 0, 'C', 0, 0, 'monitor:druid:list', 'druid',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (112, '????????????', 2, 4, 'server', 'monitor/server/index', NULL, 1, 1, 'C', 0, 0, 'monitor:server:list', 'server',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (113, '???????????????', 2, 5, 'microApp', 'system/microApp/index', NULL, 1, 0, 'C', 0, 0, 'system:microApp:list',
        'deployment-unit', 'admin', '2022-09-26 13:54:31', '', NULL, '?????????????????????');
INSERT INTO "sys_menu"
VALUES (114, '????????????', 3, 1, 'build', 'tool/build/index', NULL, 1, 0, 'C', 0, 0, 'tool:build:list', 'build',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (115, '????????????', 3, 2, 'gen', 'tool/gen/index', NULL, 1, 0, 'C', 0, 0, 'tool:gen:list', 'code',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (116, '????????????', 3, 3, 'swagger', 'tool/swagger/index', NULL, 1, 0, 'C', 0, 0, 'tool:swagger:list', 'swagger',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (500, '????????????', 108, 1, 'operlog', 'system/operlog/index', NULL, 1, 1, 'C', 0, 0, 'monitor:operlog:list', 'form',
        'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (501, '????????????', 108, 2, 'logininfor', 'system/logininfor/index', NULL, 1, 0, 'C', 0, 0, 'monitor:logininfor:list',
        'logininfor', 'admin', '2022-03-23 22:12:32', '', NULL, '??????????????????');
INSERT INTO "sys_menu"
VALUES (1000, '????????????', 100, 1, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1001, '????????????', 100, 2, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1002, '????????????', 100, 3, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1003, '????????????', 100, 4, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1004, '????????????', 100, 5, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1005, '????????????', 100, 6, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:import', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1006, '????????????', 100, 7, '', '', NULL, 1, 0, 'F', 0, 0, 'system:user:resetPwd', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1007, '????????????', 101, 1, '', '', NULL, 1, 0, 'F', 0, 0, 'system:role:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1008, '????????????', 101, 2, '', '', NULL, 1, 0, 'F', 0, 0, 'system:role:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1009, '????????????', 101, 3, '', '', NULL, 1, 0, 'F', 0, 0, 'system:role:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1010, '????????????', 101, 4, '', '', NULL, 1, 0, 'F', 0, 0, 'system:role:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1011, '????????????', 101, 5, '', '', NULL, 1, 0, 'F', 0, 0, 'system:role:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1012, '????????????', 102, 1, '', '', NULL, 1, 0, 'F', 0, 0, 'system:menu:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1013, '????????????', 102, 2, '', '', NULL, 1, 0, 'F', 0, 0, 'system:menu:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1014, '????????????', 102, 3, '', '', NULL, 1, 0, 'F', 0, 0, 'system:menu:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1015, '????????????', 102, 4, '', '', NULL, 1, 0, 'F', 0, 0, 'system:menu:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1016, '????????????', 103, 1, '', '', NULL, 1, 0, 'F', 0, 0, 'system:dept:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1017, '????????????', 103, 2, '', '', NULL, 1, 0, 'F', 0, 0, 'system:dept:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1018, '????????????', 103, 3, '', '', NULL, 1, 0, 'F', 0, 0, 'system:dept:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1019, '????????????', 103, 4, '', '', NULL, 1, 0, 'F', 0, 0, 'system:dept:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1020, '????????????', 104, 1, '', '', NULL, 1, 0, 'F', 0, 0, 'system:post:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1021, '????????????', 104, 2, '', '', NULL, 1, 0, 'F', 0, 0, 'system:post:add', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "sys_menu"
VALUES (1022, '????????????', 104, 3, '', '', NULL, 1, 0, 'F', 0, 0, 'system:post:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1023, '????????????', 104, 4, '', '', NULL, 1, 0, 'F', 0, 0, 'system:post:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1024, '????????????', 104, 5, '', '', NULL, 1, 0, 'F', 0, 0, 'system:post:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1025, '????????????', 105, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:dict:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1026, '????????????', 105, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:dict:add', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1027, '????????????', 105, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:dict:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1028, '????????????', 105, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:dict:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1029, '????????????', 105, 5, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:dict:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1030, '????????????', 106, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:config:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1031, '????????????', 106, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:config:add', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1032, '????????????', 106, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:config:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1033, '????????????', 106, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:config:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1034, '????????????', 106, 5, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:config:export', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1035, '????????????', 107, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:notice:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1036, '????????????', 107, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:notice:add', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1037, '????????????', 107, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:notice:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1038, '????????????', 107, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:notice:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1039, '????????????', 500, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:operlog:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1040, '????????????', 500, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:operlog:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1041, '????????????', 500, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:operlog:export', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1042, '????????????', 501, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:logininfor:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1043, '????????????', 501, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:logininfor:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1044, '????????????', 501, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:logininfor:export', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1045, '????????????', 501, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:logininfor:unlock', '#', 'admin',
        '2022-08-15 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1046, '????????????', 109, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:online:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1047, '????????????', 109, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:online:batchLogout', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1048, '????????????', 109, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:online:forceLogout', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1049, '????????????', 110, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:query', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1050, '????????????', 110, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:add', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1051, '????????????', 110, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:edit', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1052, '????????????', 110, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:remove', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1053, '????????????', 110, 5, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:changeStatus', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1054, '????????????', 110, 7, '#', '', NULL, 1, 0, 'F', 0, 0, 'monitor:job:export', '#', 'admin', '2022-03-23 22:12:32',
        '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1055, '????????????', 115, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:query', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1056, '????????????', 115, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:edit', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1057, '????????????', 115, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:remove', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1058, '????????????', 115, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:import', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1059, '????????????', 115, 5, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:preview', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1060, '????????????', 115, 6, '#', '', NULL, 1, 0, 'F', 0, 0, 'tool:gen:code', '#', 'admin',
        '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1061, '?????????????????????', 113, 1, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:microApp:query', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1062, '?????????????????????', 113, 2, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:microApp:add', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1063, '?????????????????????', 113, 3, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:microApp:edit', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1064, '?????????????????????', 113, 4, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:microApp:remove', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');
INSERT INTO "sys_menu"
VALUES (1065, '?????????????????????', 113, 5, '#', '', NULL, 1, 0, 'F', 0, 0, 'system:microApp:export', '#', 'admin',
        '2022-09-26 13:54:31', '', NULL, '');

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
    "status"      text(1)    NOT NULL,
    "create_by"   text(64)  DEFAULT '',
    "create_time" text      DEFAULT NULL,
    "update_by"   text(64)  DEFAULT '',
    "update_time" text      DEFAULT NULL,
    "remark"      text(500) DEFAULT NULL
);

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO "sys_post"
VALUES (1, 'ceo', '?????????', 1, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_post"
VALUES (2, 'se', '????????????', 2, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_post"
VALUES (3, 'hr', '????????????', 3, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "sys_post"
VALUES (4, 'user', '????????????', 4, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '');

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
    "status"              text(1)    NOT NULL,
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
VALUES (1, '???????????????', 'admin', 1, 1, 1, 1, 0, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '?????????');
INSERT INTO "sys_role"
VALUES (2, '????????????', 'common', 2, 2, 1, 1, 0, 0, 'admin', '2022-03-23 22:12:32', '', NULL, '????????????');

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
INSERT INTO "sys_role_dept"
VALUES (2, 100);
INSERT INTO "sys_role_dept"
VALUES (2, 101);
INSERT INTO "sys_role_dept"
VALUES (2, 105);

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
VALUES (2, 1);
INSERT INTO "sys_role_menu"
VALUES (2, 2);
INSERT INTO "sys_role_menu"
VALUES (2, 3);
INSERT INTO "sys_role_menu"
VALUES (2, 100);
INSERT INTO "sys_role_menu"
VALUES (2, 101);
INSERT INTO "sys_role_menu"
VALUES (2, 102);
INSERT INTO "sys_role_menu"
VALUES (2, 103);
INSERT INTO "sys_role_menu"
VALUES (2, 104);
INSERT INTO "sys_role_menu"
VALUES (2, 105);
INSERT INTO "sys_role_menu"
VALUES (2, 106);
INSERT INTO "sys_role_menu"
VALUES (2, 107);
INSERT INTO "sys_role_menu"
VALUES (2, 108);
INSERT INTO "sys_role_menu"
VALUES (2, 109);
INSERT INTO "sys_role_menu"
VALUES (2, 110);
INSERT INTO "sys_role_menu"
VALUES (2, 111);
INSERT INTO "sys_role_menu"
VALUES (2, 112);
INSERT INTO "sys_role_menu"
VALUES (2, 113);
INSERT INTO "sys_role_menu"
VALUES (2, 114);
INSERT INTO "sys_role_menu"
VALUES (2, 115);
INSERT INTO "sys_role_menu"
VALUES (2, 116);
INSERT INTO "sys_role_menu"
VALUES (2, 500);
INSERT INTO "sys_role_menu"
VALUES (2, 501);
INSERT INTO "sys_role_menu"
VALUES (2, 1000);
INSERT INTO "sys_role_menu"
VALUES (2, 1001);
INSERT INTO "sys_role_menu"
VALUES (2, 1002);
INSERT INTO "sys_role_menu"
VALUES (2, 1003);
INSERT INTO "sys_role_menu"
VALUES (2, 1004);
INSERT INTO "sys_role_menu"
VALUES (2, 1005);
INSERT INTO "sys_role_menu"
VALUES (2, 1006);
INSERT INTO "sys_role_menu"
VALUES (2, 1007);
INSERT INTO "sys_role_menu"
VALUES (2, 1008);
INSERT INTO "sys_role_menu"
VALUES (2, 1009);
INSERT INTO "sys_role_menu"
VALUES (2, 1010);
INSERT INTO "sys_role_menu"
VALUES (2, 1011);
INSERT INTO "sys_role_menu"
VALUES (2, 1012);
INSERT INTO "sys_role_menu"
VALUES (2, 1013);
INSERT INTO "sys_role_menu"
VALUES (2, 1014);
INSERT INTO "sys_role_menu"
VALUES (2, 1015);
INSERT INTO "sys_role_menu"
VALUES (2, 1016);
INSERT INTO "sys_role_menu"
VALUES (2, 1017);
INSERT INTO "sys_role_menu"
VALUES (2, 1018);
INSERT INTO "sys_role_menu"
VALUES (2, 1019);
INSERT INTO "sys_role_menu"
VALUES (2, 1020);
INSERT INTO "sys_role_menu"
VALUES (2, 1021);
INSERT INTO "sys_role_menu"
VALUES (2, 1022);
INSERT INTO "sys_role_menu"
VALUES (2, 1023);
INSERT INTO "sys_role_menu"
VALUES (2, 1024);
INSERT INTO "sys_role_menu"
VALUES (2, 1025);
INSERT INTO "sys_role_menu"
VALUES (2, 1026);
INSERT INTO "sys_role_menu"
VALUES (2, 1027);
INSERT INTO "sys_role_menu"
VALUES (2, 1028);
INSERT INTO "sys_role_menu"
VALUES (2, 1029);
INSERT INTO "sys_role_menu"
VALUES (2, 1030);
INSERT INTO "sys_role_menu"
VALUES (2, 1031);
INSERT INTO "sys_role_menu"
VALUES (2, 1032);
INSERT INTO "sys_role_menu"
VALUES (2, 1033);
INSERT INTO "sys_role_menu"
VALUES (2, 1034);
INSERT INTO "sys_role_menu"
VALUES (2, 1035);
INSERT INTO "sys_role_menu"
VALUES (2, 1036);
INSERT INTO "sys_role_menu"
VALUES (2, 1037);
INSERT INTO "sys_role_menu"
VALUES (2, 1038);
INSERT INTO "sys_role_menu"
VALUES (2, 1039);
INSERT INTO "sys_role_menu"
VALUES (2, 1040);
INSERT INTO "sys_role_menu"
VALUES (2, 1041);
INSERT INTO "sys_role_menu"
VALUES (2, 1042);
INSERT INTO "sys_role_menu"
VALUES (2, 1043);
INSERT INTO "sys_role_menu"
VALUES (2, 1044);
INSERT INTO "sys_role_menu"
VALUES (2, 1045);
INSERT INTO "sys_role_menu"
VALUES (2, 1046);
INSERT INTO "sys_role_menu"
VALUES (2, 1047);
INSERT INTO "sys_role_menu"
VALUES (2, 1048);
INSERT INTO "sys_role_menu"
VALUES (2, 1049);
INSERT INTO "sys_role_menu"
VALUES (2, 1050);
INSERT INTO "sys_role_menu"
VALUES (2, 1051);
INSERT INTO "sys_role_menu"
VALUES (2, 1052);
INSERT INTO "sys_role_menu"
VALUES (2, 1053);
INSERT INTO "sys_role_menu"
VALUES (2, 1054);
INSERT INTO "sys_role_menu"
VALUES (2, 1055);
INSERT INTO "sys_role_menu"
VALUES (2, 1056);
INSERT INTO "sys_role_menu"
VALUES (2, 1057);
INSERT INTO "sys_role_menu"
VALUES (2, 1058);
INSERT INTO "sys_role_menu"
VALUES (2, 1059);
INSERT INTO "sys_role_menu"
VALUES (2, 1060);
INSERT INTO "sys_role_menu"
VALUES (2, 1061);
INSERT INTO "sys_role_menu"
VALUES (2, 1062);
INSERT INTO "sys_role_menu"
VALUES (2, 1063);
INSERT INTO "sys_role_menu"
VALUES (2, 1064);
INSERT INTO "sys_role_menu"
VALUES (2, 1065);

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
    "user_type"   text(2)     DEFAULT '00',
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
VALUES (1, 100, 'admin', '???????????????', '00', 'mapgis@mapgis.com', 13888888888, 0, '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 0, 0, '', NULL, 'admin', '2022-03-23 22:12:32',
        '', NULL, '???????????????');
INSERT INTO "sys_user"
VALUES (2, 103, 'giser', 'GISer', '00', 'giser@mapgis.com', 13666666666, 0, '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 0, 0, '', NULL, 'admin', '2022-03-23 22:12:32',
        '', NULL, '?????????');

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
INSERT INTO "sys_user_post"
VALUES (2, 4);

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
INSERT INTO "sys_user_role"
VALUES (2, 2);

-- ----------------------------
-- Indexes structure for table sys_dict_type
-- ----------------------------
CREATE UNIQUE INDEX "dict_type"
    ON "sys_dict_type" (
                        "dict_type" ASC
        );

