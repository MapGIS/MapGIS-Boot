/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : mapgis-cloud-xxx

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 25/09/2023 12:48:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
DROP TABLE IF EXISTS `file_storage`;
CREATE TABLE `file_storage`
(
    `file_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `engine`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储引擎',
    `bucket`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储桶',
    `name`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '文件名称',
    `suffix`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件后缀',
    `size_kb`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '文件大小kb',
    `size`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小（格式化后）',
    `url`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '文件地址',
    `thumbnail`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '图片缩略图',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '文件存储表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_storage
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`
(
    `table_id`          bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '编号',
    `table_name`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '表名称',
    `table_comment`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '表描述',
    `sub_table_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '关联子表的表名',
    `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '子表关联的外键名',
    `class_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '实体类名称',
    `tpl_category`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
    `package_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '生成包路径',
    `module_name`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '生成模块名',
    `business_name`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '生成业务名',
    `function_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '生成功能名',
    `function_author`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT NULL COMMENT '生成功能作者',
    `gen_type`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
    `gen_path`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
    `options`           varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '创建者',
    `create_time`       datetime                                                       NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '更新者',
    `update_time`       datetime                                                       NULL DEFAULT NULL COMMENT '更新时间',
    `remark`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`
(
    `column_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '编号',
    `table_id`       bigint(20)                                                    NULL DEFAULT NULL COMMENT '归属表编号',
    `column_name`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
    `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
    `column_type`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
    `java_type`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
    `java_field`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
    `is_pk`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否主键（1是）',
    `is_increment`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否自增（1是）',
    `is_required`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否必填（1是）',
    `is_insert`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
    `is_edit`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
    `is_list`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
    `is_query`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
    `query_type`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
    `html_type`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
    `dict_type`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `sort`           int(11)                                                       NULL DEFAULT NULL COMMENT '排序',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_config`;
CREATE TABLE `sys_auth_config`
(
    `config_id`          int(5)                                                        NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `type`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录平台',
    `name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录名称',
    `icon`               longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '登录图标',
    `help`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录帮助',
    `client_id`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '客户端Id',
    `client_secret`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '客户端秘钥',
    `redirect_uri`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '回调地址',
    `auth_request_class` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '授权请求类',
    `status`             char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`        datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`        datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`             varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '第三方登录配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_user`;
CREATE TABLE `sys_auth_user`
(
    `auth_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '授权ID',
    `uuid`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方平台用户唯一ID',
    `user_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '系统用户ID',
    `login_name`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '登录账号',
    `user_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '用户昵称',
    `avatar`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
    `email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
    `source`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户来源',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`auth_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '第三方授权表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_auth_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_id`    int(5)                                                        NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `config_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
    `config_key`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
    `config_value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '参数键值',
    `config_type`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '参数配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, '安全配置-初始密码配置', 'security.initPassword', 'MapGIS123', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '用户初始密码');
INSERT INTO `sys_config`
VALUES (2, '安全配置-用户注册配置', 'security.register', '{\"enabled\":true,\"defaultRoleIds\":[3]}', 'Y', 'admin', '2022-03-23 22:12:32',
        '', NULL, '用户注册管理');
INSERT INTO `sys_config`
VALUES (3, '系统配置-基本配置', 'system.base',
        '{\"header\":{\"defaultLogoAndTitle\":true,\"logo\":\"\",\"title\":\"\"},\"footer\":{\"copyright\":\"Copyright © 2023 武汉中地数码科技有限公司\"}}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '系统基本配置');
INSERT INTO `sys_config`
VALUES (4, '安全配置-密码安全配置', 'security.passwordProtected',
        '{\"enabled\":true,\"maxRetryCount\":5,\"lockTime\":10,\"isLockedByIp\":true}', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '密码安全配置');
INSERT INTO `sys_config`
VALUES (5, '安全配置-登录配置', 'security.login',
        '{\"soloLoginEnabled\":true,\"captchaEnabled\":true,\"tipEnabled\":true,\"captchaType\":\"math\",\"maxRetryCount\":1,\"recordTime\":10}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '用户登录配置');
INSERT INTO `sys_config`
VALUES (6, '安全配置-第三方登录配置', 'security.oauth', '{\"defaultUserGroupIds\":[1]}', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '第三方登录配置');
INSERT INTO `sys_config`
VALUES (7, '安全配置-CAS登录配置', 'security.cas', '{\"enabled\":false,\"isReserveDefaultLogin\":false,\"casServerUrl\":\"\"}', 'Y',
        'admin', '2022-03-23 22:12:32', '', NULL, 'CAS登录配置');
INSERT INTO `sys_config`
VALUES (8, '安全配置-LDAP登录配置', 'security.ldap',
        '{\"enabled\":false,\"url\":\"\",\"base\":\"\",\"userDn\":\"\",\"password\":\"\",\"defaultUserGroupIds\":[1],\"roleMapping\":[]}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, 'LDAP登录配置');
INSERT INTO `sys_config`
VALUES (9, '日志配置', 'log', '{\"systemLoglevel\":\"INFO\",\"httpAccessEnabled\":false,\"httpAccessMonitorUrls\":[]}', 'Y', 'admin',
        '2022-03-23 22:12:32', '', NULL, '日志配置');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `dept_id`     bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `parent_id`   bigint(20)                                                   NULL DEFAULT 0 COMMENT '父部门id',
    `ancestors`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
    `dept_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
    `order_num`   int(4)                                                       NULL DEFAULT 0 COMMENT '显示顺序',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 200
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '部门表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept`
VALUES (100, 0, '0', '内置部门', 0, '0', 'admin', '2022-03-23 22:12:32', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `dict_code`   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `dict_sort`   int(4)                                                        NULL DEFAULT 0 COMMENT '字典排序',
    `dict_label`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
    `dict_value`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `css_class`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    `list_class`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
    `is_default`  char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典数据表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data`
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '性别男');
INSERT INTO `sys_dict_data`
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '性别女');
INSERT INTO `sys_dict_data`
VALUES (3, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data`
VALUES (4, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data`
VALUES (5, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (6, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO `sys_dict_data`
VALUES (7, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (8, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO `sys_dict_data`
VALUES (9, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '默认分组');
INSERT INTO `sys_dict_data`
VALUES (10, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统分组');
INSERT INTO `sys_dict_data`
VALUES (11, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data`
VALUES (12, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data`
VALUES (13, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知');
INSERT INTO `sys_dict_data`
VALUES (14, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '公告');
INSERT INTO `sys_dict_data`
VALUES (15, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (16, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data`
VALUES (17, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-08-15 22:12:32', '', NULL, '其他操作');
INSERT INTO `sys_dict_data`
VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '新增操作');
INSERT INTO `sys_dict_data`
VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '修改操作');
INSERT INTO `sys_dict_data`
VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '删除操作');
INSERT INTO `sys_dict_data`
VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '授权操作');
INSERT INTO `sys_dict_data`
VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '导出操作');
INSERT INTO `sys_dict_data`
VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '导入操作');
INSERT INTO `sys_dict_data`
VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '强退操作');
INSERT INTO `sys_dict_data`
VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '生成操作');
INSERT INTO `sys_dict_data`
VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '清空操作');
INSERT INTO `sys_dict_data`
VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO `sys_dict_data`
VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `dict_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    `dict_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
    `dict_type`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`dict_id`) USING BTREE,
    UNIQUE INDEX `dict_type` (`dict_type`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '字典类型表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type`
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type`
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type`
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type`
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type`
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type`
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type`
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type`
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type`
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type`
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_gateway_route
-- ----------------------------
DROP TABLE IF EXISTS `sys_gateway_route`;
CREATE TABLE `sys_gateway_route`
(
    `gateway_route_id` bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '路由编号',
    `route_id`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由ID',
    `uri`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '服务地址',
    `predicates`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '断言',
    `filters`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '过滤器',
    `order_num`        int(4)                                                        NULL DEFAULT 0 COMMENT '顺序',
    `status`           char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '路由状态（0正常 1停用）',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`      datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`      datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`gateway_route_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '网关路由表'
  ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_gateway_route
-- ----------------------------
INSERT INTO `sys_gateway_route`
VALUES (1, '${mapgis.product.full-name}-auth-server', 'lb://${mapgis.product.full-name}-auth-server',
        '[{\"name\":\"Path\",\"args\":[\"${api.path.manager-prefix}/auth/**\"]}]', '[{\"name\":\"CacheRequestFilter\",\"args\":[]}]', 0,
        '0', 'admin', '2022-09-20 14:12:32', '', NULL);
INSERT INTO `sys_gateway_route`
VALUES (2, '${mapgis.product.full-name}-job-server', 'lb://${mapgis.product.full-name}-job-server',
        '[{\"name\":\"Path\",\"args\":[\"${api.path.manager-prefix}/schedule/**\"]}]', '[]', 0, '0', 'admin', '2022-09-20 14:12:32', '',
        NULL);
INSERT INTO `sys_gateway_route`
VALUES (3, '${mapgis.product.full-name}-system-server', 'lb://${mapgis.product.full-name}-system-server',
        '[{\"name\":\"Path\",\"args\":[\"${api.path.manager-prefix}/system/**\",\"/${mapgis.product.name}/manager/**\",\"/${mapgis.product.name}/static/**\",\"/\"]}]',
        '[]', 0, '0', 'admin', '2022-09-20 14:12:32', '', NULL);
INSERT INTO `sys_gateway_route`
VALUES (4, '${mapgis.product.full-name}-file-server', 'lb://${mapgis.product.full-name}-file-server',
        '[{\"name\":\"Path\",\"args\":[\"${api.path.manager-prefix}/file/**\"]}]', '[]', 0, '0', 'admin', '2022-09-20 14:12:32', '', NULL);
INSERT INTO `sys_gateway_route`
VALUES (5, '${mapgis.product.full-name}-gen-server', 'lb://${mapgis.product.full-name}-gen-server',
        '[{\"name\":\"Path\",\"args\":[\"${api.path.manager-prefix}/gen/**\"]}]', '[]', 0, '0', 'admin', '2022-09-20 14:12:32', '', NULL);

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`
(
    `job_id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `job_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '任务名称',
    `job_group`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
    `invoke_target`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
    `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT 'cron执行表达式',
    `misfire_policy`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    `concurrent`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL     DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
    `status`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL     DEFAULT '0' COMMENT '状态（0正常 1暂停）',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                                      NULL     DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                                      NULL     DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT '' COMMENT '备注信息',
    PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`
(
    `job_log_id`     bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
    `job_name`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '任务名称',
    `job_group`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '任务组名',
    `invoke_target`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '调用目标字符串',
    `job_message`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '日志信息',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
    `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
    `create_time`    datetime                                                       NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`
(
    `info_id`    bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `user_name`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '用户账号',
    `ipaddr`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
    `browser`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '浏览器类型',
    `os`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '操作系统',
    `status`     char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    `msg`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
    `login_time` datetime                                                      NULL DEFAULT NULL COMMENT '访问时间',
    PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `menu_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `menu_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '菜单名称',
    `parent_id`    bigint(20)                                                    NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`    int(4)                                                        NULL DEFAULT 0 COMMENT '显示顺序',
    `path`         varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
    `component`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
    `query_string` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
    `is_frame`     int(1)                                                        NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
    `is_cache`     int(1)                                                        NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
    `menu_type`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `visible`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `status`       char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    `perms`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
    `icon`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2000
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, '监控管理', 0, 1, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-03-23 22:12:32', '', NULL,
        '监控管理目录');
INSERT INTO `sys_menu`
VALUES (2, '日志管理', 0, 2, 'log', NULL, '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-03-23 22:12:32', '', NULL, '日志管理目录');
INSERT INTO `sys_menu`
VALUES (3, '安全管理', 0, 3, 'security', NULL, '', 1, 0, 'M', '0', '0', '', 'safety-certificate', 'admin', '2022-03-23 22:12:32', '', NULL,
        '安全管理目录');
INSERT INTO `sys_menu`
VALUES (4, '计划任务', 0, 4, 'schedule', NULL, '', 1, 0, 'M', '0', '0', '', 'job', 'admin', '2022-10-11 14:20:47', '', NULL,
        '定时任务目录');
INSERT INTO `sys_menu`
VALUES (5, '消息中心', 0, 5, 'message', NULL, '', 1, 0, 'M', '0', '0', '', 'bell', 'admin', '2022-10-12 08:56:41', '', NULL,
        '消息中心目录');
INSERT INTO `sys_menu`
VALUES (6, '开发扩展', 0, 6, 'dev', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-03-23 22:12:32', '', NULL, '开发扩展目录');
INSERT INTO `sys_menu`
VALUES (7, '系统配置', 0, 7, 'config', NULL, '', 1, 0, 'M', '0', '0', '', 'setting', 'admin', '2022-10-11 18:33:09', '', NULL,
        '系统设置目录');
INSERT INTO `sys_menu`
VALUES (100, '在线用户', 1, 1, 'online', 'monitor/online/index', '', 1, 1, 'C', '0', '0', 'monitor:online:list', 'online', 'admin',
        '2022-03-23 22:12:32', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu`
VALUES (101, '服务器监控', 1, 2, 'server', 'monitor/server/index', '', 1, 1, 'C', '0', '0', 'monitor:server:list', 'server', 'admin',
        '2022-03-23 22:12:32', '', NULL, '服务器监控菜单');
INSERT INTO `sys_menu`
VALUES (102, '登录日志', 2, 1, 'logininfor', 'system/logininfor/index', '', 1, 1, 'C', '0', '0', 'system:logininfor:list', 'logininfor',
        'admin', '2022-03-23 22:12:32', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu`
VALUES (103, '操作日志', 2, 2, 'operlog', 'system/operlog/index', '', 1, 1, 'C', '0', '0', 'system:operlog:list', 'form', 'admin',
        '2022-03-23 22:12:32', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu`
VALUES (104, '用户管理', 3, 2, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin',
        '2022-03-23 22:12:32', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu`
VALUES (105, '角色管理', 3, 4, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin',
        '2022-03-23 22:12:32', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu`
VALUES (106, '菜单管理', 3, 5, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'treeTable', 'admin',
        '2022-03-23 22:12:32', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu`
VALUES (107, '部门管理', 3, 6, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin',
        '2022-03-23 22:12:32', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu`
VALUES (108, '岗位管理', 3, 7, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin',
        '2022-03-23 22:12:32', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu`
VALUES (109, '定时任务', 4, 1, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin',
        '2022-03-23 22:12:32', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu`
VALUES (110, '通知公告', 5, 1, 'notice', 'system/notice/index', '', 1, 1, 'C', '0', '0', 'system:notice:list', 'message', 'admin',
        '2022-03-23 22:12:32', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu`
VALUES (111, '接口文档', 6, 1, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin',
        '2022-03-23 22:12:32', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu`
VALUES (112, '数据字典', 6, 2, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin',
        '2022-03-23 22:12:32', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu`
VALUES (113, '代码生成', 6, 3, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-03-23 22:12:32',
        '', NULL, '代码生成菜单');
INSERT INTO `sys_menu`
VALUES (114, '微应用路由配置', 6, 4, 'microApp', 'system/microApp/index', '', 1, 0, 'C', '0', '0', 'system:microApp:list',
        'deployment-unit', 'admin', '2022-09-26 13:54:31', '', NULL, '微应用路由配置菜单');
INSERT INTO `sys_menu`
VALUES (115, '安全配置', 3, 1, 'securityConfig', 'security/config/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'validCode',
        'admin', '2022-10-14 17:51:53', '', NULL, '安全配置菜单');
INSERT INTO `sys_menu`
VALUES (116, '基本配置', 7, 1, 'base', 'config/base/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'profile', 'admin',
        '2022-10-15 15:45:11', '', NULL, '基本配置菜单');
INSERT INTO `sys_menu`
VALUES (117, '第三方登录配置', 3, 8, 'oauth', 'security/authConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:authConfig:list',
        'security-scan', 'admin', '2022-10-21 16:11:18', '', NULL, '第三方登录配置菜单');
INSERT INTO `sys_menu`
VALUES (118, 'CAS登录配置', 3, 9, 'cas', 'security/casConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'cas', 'admin',
        '2022-10-27 14:55:00', '', NULL, 'CAS登录配置菜单');
INSERT INTO `sys_menu`
VALUES (119, '主题配置', 7, 2, 'theme', 'config/theme/index', NULL, 1, 0, 'C', '0', '0', '', 'skin', 'admin', '2022-10-31 11:39:51', '',
        NULL, '主题配置菜单');
INSERT INTO `sys_menu`
VALUES (120, 'LDAP登录配置', 3, 10, 'ldap', 'security/ldapConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'ldap', 'admin',
        '2022-11-03 14:55:44', '', NULL, 'LDAP登录配置菜单');
INSERT INTO `sys_menu`
VALUES (121, '系统日志', 2, 3, 'systemlog', 'log/systemlog/index', NULL, 1, 1, 'C', '0', '0', 'system:systemlog:list', 'edit', 'admin',
        '2022-11-23 11:07:53', '', NULL, '系统日志菜单');
INSERT INTO `sys_menu`
VALUES (122, '服务访问日志', 2, 4, 'assesslog', 'log/assesslog/index', NULL, 1, 1, 'C', '0', '0', 'system:assesslog:list', 'file-text',
        'admin', '2022-11-28 17:23:54', '', NULL, '服务访问日志菜单');
INSERT INTO `sys_menu`
VALUES (123, '日志配置', 2, 5, 'logConfig', 'log/config/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'file-done', 'admin',
        '2022-11-30 11:45:39', '', NULL, '日志配置菜单');
INSERT INTO `sys_menu`
VALUES (124, '服务器性能监控', 1, 3, 'performance', 'monitor/performance/index', NULL, 1, 1, 'C', '0', '0',
        'monitor:serverPerformance:list', 'fund', 'admin', '2022-12-06 10:54:54', '', NULL, '服务器性能监控菜单');
INSERT INTO `sys_menu`
VALUES (125, '用户组管理', 3, 3, 'usergroup', 'system/usergroup/index', NULL, 1, 0, 'C', '0', '0', 'system:usergroup:list', 'team', 'admin',
        '2022-12-23 16:32:49', '', NULL, '用户组管理菜单');
INSERT INTO `sys_menu`
VALUES (126, '文件管理', 0, 3, 'file', 'file/storage/index', '', 1, 0, 'C', '0', '0', 'file:storage:list', 'copy', 'admin',
        '2023-09-21 10:15:33', '', NULL, '文件管理菜单');
INSERT INTO `sys_menu`
VALUES (500, '微服务路由配置', 6, 5, 'route', 'system/route/index', '', 1, 0, 'C', '0', '0', 'system:route:list', 'route', 'admin',
        '2022-09-21 22:08:36', '', NULL, '微服务路由配置菜单');
INSERT INTO `sys_menu`
VALUES (1000, '在线查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1001, '批量强退', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1002, '单条强退', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1003, '登录查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1004, '登录删除', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1005, '日志导出', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1006, '账号解锁', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', '2022-08-15 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1007, '操作查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1008, '操作删除', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1009, '日志导出', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1010, '用户查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1011, '用户新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1012, '用户修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1013, '用户删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1014, '用户导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1015, '用户导入', 104, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1016, '重置密码', 104, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1017, '角色查询', 105, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1018, '角色新增', 105, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1019, '角色修改', 105, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1020, '角色删除', 105, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1021, '角色导出', 105, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1022, '菜单查询', 106, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1023, '菜单新增', 106, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1024, '菜单修改', 106, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1025, '菜单删除', 106, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1026, '部门查询', 107, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1027, '部门新增', 107, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1028, '部门修改', 107, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1029, '部门删除', 107, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1030, '岗位导出', 108, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1031, '岗位查询', 108, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1032, '岗位新增', 108, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1033, '岗位修改', 108, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1034, '岗位删除', 108, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1035, '任务查询', 109, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1036, '任务新增', 109, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1037, '任务修改', 109, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1038, '任务删除', 109, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1039, '状态修改', 109, 5, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1040, '任务导出', 109, 7, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1041, '公告查询', 110, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1042, '公告新增', 110, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1043, '公告修改', 110, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1044, '公告删除', 110, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1045, '字典查询', 112, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1046, '字典新增', 112, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1047, '字典修改', 112, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1048, '字典删除', 112, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1049, '字典导出', 112, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1050, '生成查询', 113, 1, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1051, '生成修改', 113, 2, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1052, '生成删除', 113, 3, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1053, '导入代码', 113, 4, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1054, '预览代码', 113, 5, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1055, '生成代码', 113, 6, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1056, '微应用路由查询', 114, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:query', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1057, '微应用路由新增', 114, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:add', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1058, '微应用路由修改', 114, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:edit', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1059, '微应用路由删除', 114, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:remove', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1060, '微应用路由导出', 114, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:export', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1061, '配置修改', 115, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-14 17:54:02', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1062, '配置修改', 116, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-15 15:45:38', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1063, '第三方登录配置查询', 117, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:query', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1064, '第三方登录配置新增', 117, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:add', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1065, '第三方登录配置修改', 117, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:edit', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1066, '第三方登录配置删除', 117, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:remove', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1067, '第三方登录配置导出', 117, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:export', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1068, '第三方登录角色配置查询', 117, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-11-03 14:07:26',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1069, '第三方登录角色配置修改', 117, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-11-03 14:08:45',
        '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1070, '配置修改', 118, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-27 14:55:41', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1071, '配置修改', 120, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-11-03 14:56:11', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1072, '日志导出', 121, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:systemlog:export', '#', 'admin', '2022-11-23 14:52:25', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1073, '日志导出', 122, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:assesslog:export', '#', 'admin', '2022-11-28 17:23:54', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1074, '配置修改', 123, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config.edit', '#', 'admin', '2022-11-30 15:10:47', '', NULL, '');
INSERT INTO `sys_menu`
VALUES (1075, '用户组查询', 125, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:query', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1076, '用户组新增', 125, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:add', '#', 'admin', '2022-12-23 16:32:50', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1077, '用户组修改', 125, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:edit', '#', 'admin', '2022-12-23 16:32:50', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1078, '用户组删除', 125, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:remove', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
INSERT INTO `sys_menu`
VALUES (1079, '用户组导出', 125, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:export', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
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
INSERT INTO `sys_menu`
VALUES (1500, '网关路由查询', 500, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:route:query', '#', 'admin', '2022-09-21 22:08:36', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1501, '网关路由新增', 500, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:route:add', '#', 'admin', '2022-09-21 22:08:36', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1502, '网关路由修改', 500, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:route:edit', '#', 'admin', '2022-09-21 22:08:36', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1503, '网关路由删除', 500, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:route:remove', '#', 'admin', '2022-09-21 22:08:36', '', NULL,
        '');
INSERT INTO `sys_menu`
VALUES (1504, '网关路由导出', 500, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:route:export', '#', 'admin', '2022-09-21 22:08:36', '', NULL,
        '');

-- ----------------------------
-- Table structure for sys_micro_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_micro_app`;
CREATE TABLE `sys_micro_app`
(
    `micro_app_id` bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '微应用',
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `entry`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `active_rule`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`  datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`micro_app_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '微应用路由表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_micro_app
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`
(
    `notice_id`      int(4)                                                        NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `notice_title`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '公告标题',
    `notice_type`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NOT NULL COMMENT '公告类型（1通知 2公告）',
    `notice_content` longblob                                                      NULL COMMENT '公告内容',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`    datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知公告表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`
(
    `oper_id`        bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `title`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '模块标题',
    `business_type`  int(2)                                                         NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    `method`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '方法名称',
    `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '请求方式',
    `operator_type`  int(1)                                                         NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    `oper_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '操作人员',
    `dept_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL DEFAULT '' COMMENT '部门名称',
    `oper_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '请求URL',
    `oper_ip`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '主机地址',
    `oper_param`     varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求参数',
    `json_result`    varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回参数',
    `status`         int(1)                                                         NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
    `error_msg`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
    `oper_time`      datetime                                                       NULL DEFAULT NULL COMMENT '操作时间',
    `cost_time`      bigint(20)                                                     NULL DEFAULT 0 COMMENT '消耗时间',
    PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`
(
    `post_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `post_code`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '岗位编码',
    `post_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '岗位名称',
    `post_sort`   int(4)                                                        NOT NULL COMMENT '显示顺序',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `role_id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '角色名称',
    `role_key`            varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
    `role_sort`           int(4)                                                        NOT NULL COMMENT '显示顺序',
    `data_scope`          char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `menu_check_strictly` tinyint(1)                                                    NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
    `dept_check_strictly` tinyint(1)                                                    NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
    `is_sys`              tinyint(1)                                                    NULL DEFAULT 0 COMMENT '默认角色（0否 1是）',
    `del_flag`            char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`         datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`         datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`              varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '系统管理员', 'admin', 1, '1', 1, 1, 1, '0', 'admin', '2022-03-23 22:12:32', '', NULL,
        '内置的系统管理员角色，拥有整个服务器的管理权限');
INSERT INTO `sys_role`
VALUES (2, '第三方用户', 'third', 2, '5', 1, 1, 1, '0', 'admin', '2022-10-24 11:44:51', '', NULL,
        '内置的第三方用户角色，表示用户是使用第三方登录');
INSERT INTO `sys_role`
VALUES (3, '普通用户', 'user', 3, '1', 1, 1, 1, '0', 'admin', '2022-10-17 15:18:18', '', NULL, '内置的普通用户角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`
VALUES (2, 6);
INSERT INTO `sys_role_menu`
VALUES (2, 7);
INSERT INTO `sys_role_menu`
VALUES (2, 111);
INSERT INTO `sys_role_menu`
VALUES (2, 119);
INSERT INTO `sys_role_menu`
VALUES (3, 6);
INSERT INTO `sys_role_menu`
VALUES (3, 7);
INSERT INTO `sys_role_menu`
VALUES (3, 111);
INSERT INTO `sys_role_menu`
VALUES (3, 119);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `user_id`     bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `dept_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '部门ID',
    `user_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户账号',
    `nick_name`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户昵称',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '用户邮箱',
    `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '手机号码',
    `sex`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    `avatar`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
    `status`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
    `login_ip`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
    `login_date`  datetime                                                      NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 100, 'admin', '系统管理员', '', '', '0', '', '$2a$10$W0oZaiVL8yYLG6PQsM2f4uYFI9kkS424BVArwpSHozx7FxdCijOfq', '0', '0', '', NULL,
        'admin', '2022-03-23 22:12:32', '', NULL, '系统管理员');

-- ----------------------------
-- Table structure for sys_user_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_group`;
CREATE TABLE `sys_user_group`
(
    `user_group_id`   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
    `user_group_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户组名称',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '创建者',
    `create_time`     datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT '' COMMENT '更新者',
    `update_time`     datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `remark`          varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_group_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户组表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_group
-- ----------------------------
INSERT INTO `sys_user_group`
VALUES (1, '第三方用户组', 'admin', '2022-12-27 11:30:51', '', NULL, '内置的第三方用户组');

-- ----------------------------
-- Table structure for sys_user_group_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_group_role`;
CREATE TABLE `sys_user_group_role`
(
    `user_group_id` bigint(20) NOT NULL COMMENT '用户组ID',
    `role_id`       bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_group_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户组和角色关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_group_role
-- ----------------------------
INSERT INTO `sys_user_group_role`
VALUES (1, 2);

-- ----------------------------
-- Table structure for sys_user_group_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_group_user`;
CREATE TABLE `sys_user_group_user`
(
    `user_group_id` bigint(20) NOT NULL COMMENT '用户组ID',
    `user_id`       bigint(20) NOT NULL COMMENT '用户ID',
    PRIMARY KEY (`user_group_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户组和用户关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1);

SET FOREIGN_KEY_CHECKS = 1;
