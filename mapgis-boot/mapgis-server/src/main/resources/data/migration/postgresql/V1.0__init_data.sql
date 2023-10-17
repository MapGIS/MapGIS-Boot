/*
 Navicat Premium Data Transfer

 Source Server         : localhost-pg
 Source Server Type    : PostgreSQL
 Source Server Version : 100023
 Source Host           : localhost:5432
 Source Catalog        : mapgis-xxx
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100023
 File Encoding         : 65001

 Date: 16/10/2023 15:12:45
*/


-- ----------------------------
-- Sequence structure for file_storage_file_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."file_storage_file_id_seq";
CREATE SEQUENCE "public"."file_storage_file_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for gen_table_column_column_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_column_column_id_seq";
CREATE SEQUENCE "public"."gen_table_column_column_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for gen_table_table_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."gen_table_table_id_seq";
CREATE SEQUENCE "public"."gen_table_table_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_auth_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_auth_config_config_id_seq";
CREATE SEQUENCE "public"."sys_auth_config_config_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_auth_user_auth_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_auth_user_auth_id_seq";
CREATE SEQUENCE "public"."sys_auth_user_auth_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_config_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_config_config_id_seq";
CREATE SEQUENCE "public"."sys_config_config_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dept_dept_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dept_dept_id_seq";
CREATE SEQUENCE "public"."sys_dept_dept_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_data_dict_code_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_data_dict_code_seq";
CREATE SEQUENCE "public"."sys_dict_data_dict_code_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_dict_type_dict_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_dict_type_dict_id_seq";
CREATE SEQUENCE "public"."sys_dict_type_dict_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_job_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_job_id_seq";
CREATE SEQUENCE "public"."sys_job_job_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_job_log_job_log_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_job_log_job_log_id_seq";
CREATE SEQUENCE "public"."sys_job_log_job_log_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_logininfor_info_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_logininfor_info_id_seq";
CREATE SEQUENCE "public"."sys_logininfor_info_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_menu_menu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_menu_menu_id_seq";
CREATE SEQUENCE "public"."sys_menu_menu_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_micro_app_micro_app_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_micro_app_micro_app_id_seq";
CREATE SEQUENCE "public"."sys_micro_app_micro_app_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_notice_notice_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_notice_notice_id_seq";
CREATE SEQUENCE "public"."sys_notice_notice_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_oper_log_oper_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_oper_log_oper_id_seq";
CREATE SEQUENCE "public"."sys_oper_log_oper_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_post_post_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_post_post_id_seq";
CREATE SEQUENCE "public"."sys_post_post_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_role_role_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_role_role_id_seq";
CREATE SEQUENCE "public"."sys_role_role_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_group_user_group_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_group_user_group_id_seq";
CREATE SEQUENCE "public"."sys_user_group_user_group_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_user_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_user_user_id_seq";
CREATE SEQUENCE "public"."sys_user_user_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Table structure for file_storage
-- ----------------------------
DROP TABLE IF EXISTS "public"."file_storage";
CREATE TABLE "public"."file_storage"
(
    "file_id"     int8 NOT NULL                               DEFAULT nextval('file_storage_file_id_seq'::regclass),
    "engine"      varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "bucket"      varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "name"        text COLLATE "pg_catalog"."default",
    "suffix"      varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "size_kb"     varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "size"        varchar(255) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "url"         text COLLATE "pg_catalog"."default",
    "thumbnail"   text COLLATE "pg_catalog"."default",
    "create_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."file_storage"."file_id" IS '文件ID';
COMMENT ON COLUMN "public"."file_storage"."engine" IS '存储引擎';
COMMENT ON COLUMN "public"."file_storage"."bucket" IS '存储桶';
COMMENT ON COLUMN "public"."file_storage"."name" IS '文件名称';
COMMENT ON COLUMN "public"."file_storage"."suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."file_storage"."size_kb" IS '文件大小kb';
COMMENT ON COLUMN "public"."file_storage"."size" IS '文件大小（格式化后）';
COMMENT ON COLUMN "public"."file_storage"."url" IS '文件地址';
COMMENT ON COLUMN "public"."file_storage"."thumbnail" IS '图片缩略图';
COMMENT ON COLUMN "public"."file_storage"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."file_storage"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."file_storage"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."file_storage"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."file_storage"."remark" IS '备注';
COMMENT ON TABLE "public"."file_storage" IS '文件存储表';

-- ----------------------------
-- Records of file_storage
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table";
CREATE TABLE "public"."gen_table"
(
    "table_id"          int8 NOT NULL                                DEFAULT nextval('gen_table_table_id_seq'::regclass),
    "table_name"        varchar(200) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "table_comment"     varchar(500) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "sub_table_name"    varchar(64) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "sub_table_fk_name" varchar(64) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "class_name"        varchar(100) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "tpl_category"      varchar(200) COLLATE "pg_catalog"."default"  DEFAULT 'crud'::character varying,
    "package_name"      varchar(100) COLLATE "pg_catalog"."default"  DEFAULT NULL::character varying,
    "module_name"       varchar(30) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "business_name"     varchar(30) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "function_name"     varchar(50) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "function_author"   varchar(50) COLLATE "pg_catalog"."default"   DEFAULT NULL::character varying,
    "gen_type"          char(1) COLLATE "pg_catalog"."default"       DEFAULT '0'::bpchar,
    "gen_path"          varchar(200) COLLATE "pg_catalog"."default"  DEFAULT '/'::character varying,
    "options"           varchar(1000) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "create_by"         varchar(64) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "create_time"       timestamp(6),
    "update_by"         varchar(64) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "update_time"       timestamp(6),
    "remark"            varchar(500) COLLATE "pg_catalog"."default"  DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."gen_table"."table_id" IS '编号';
COMMENT ON COLUMN "public"."gen_table"."table_name" IS '表名称';
COMMENT ON COLUMN "public"."gen_table"."table_comment" IS '表描述';
COMMENT ON COLUMN "public"."gen_table"."sub_table_name" IS '关联子表的表名';
COMMENT ON COLUMN "public"."gen_table"."sub_table_fk_name" IS '子表关联的外键名';
COMMENT ON COLUMN "public"."gen_table"."class_name" IS '实体类名称';
COMMENT ON COLUMN "public"."gen_table"."tpl_category" IS '使用的模板（crud单表操作 tree树表操作）';
COMMENT ON COLUMN "public"."gen_table"."package_name" IS '生成包路径';
COMMENT ON COLUMN "public"."gen_table"."module_name" IS '生成模块名';
COMMENT ON COLUMN "public"."gen_table"."business_name" IS '生成业务名';
COMMENT ON COLUMN "public"."gen_table"."function_name" IS '生成功能名';
COMMENT ON COLUMN "public"."gen_table"."function_author" IS '生成功能作者';
COMMENT ON COLUMN "public"."gen_table"."gen_type" IS '生成代码方式（0zip压缩包 1自定义路径）';
COMMENT ON COLUMN "public"."gen_table"."gen_path" IS '生成路径（不填默认项目路径）';
COMMENT ON COLUMN "public"."gen_table"."options" IS '其它生成选项';
COMMENT ON COLUMN "public"."gen_table"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."gen_table"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_table"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."gen_table"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."gen_table"."remark" IS '备注';
COMMENT ON TABLE "public"."gen_table" IS '代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS "public"."gen_table_column";
CREATE TABLE "public"."gen_table_column"
(
    "column_id"      int8 NOT NULL                               DEFAULT nextval('gen_table_column_column_id_seq'::regclass),
    "table_id"       int8,
    "column_name"    varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "column_comment" varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "column_type"    varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "java_type"      varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "java_field"     varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "is_pk"          char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_increment"   char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_required"    char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_insert"      char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_edit"        char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_list"        char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "is_query"       char(1) COLLATE "pg_catalog"."default"      DEFAULT NULL::bpchar,
    "query_type"     varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'EQ'::character varying,
    "html_type"      varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "dict_type"      varchar(200) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "sort"           int4,
    "create_by"      varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time"    timestamp(6),
    "update_by"      varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time"    timestamp(6)
)
;
COMMENT ON COLUMN "public"."gen_table_column"."column_id" IS '编号';
COMMENT ON COLUMN "public"."gen_table_column"."table_id" IS '归属表编号';
COMMENT ON COLUMN "public"."gen_table_column"."column_name" IS '列名称';
COMMENT ON COLUMN "public"."gen_table_column"."column_comment" IS '列描述';
COMMENT ON COLUMN "public"."gen_table_column"."column_type" IS '列类型';
COMMENT ON COLUMN "public"."gen_table_column"."java_type" IS 'JAVA类型';
COMMENT ON COLUMN "public"."gen_table_column"."java_field" IS 'JAVA字段名';
COMMENT ON COLUMN "public"."gen_table_column"."is_pk" IS '是否主键（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_increment" IS '是否自增（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_required" IS '是否必填（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_insert" IS '是否为插入字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_edit" IS '是否编辑字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_list" IS '是否列表字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."is_query" IS '是否查询字段（1是）';
COMMENT ON COLUMN "public"."gen_table_column"."query_type" IS '查询方式（等于、不等于、大于、小于、范围）';
COMMENT ON COLUMN "public"."gen_table_column"."html_type" IS '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）';
COMMENT ON COLUMN "public"."gen_table_column"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."gen_table_column"."sort" IS '排序';
COMMENT ON COLUMN "public"."gen_table_column"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."gen_table_column"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."gen_table_column"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."gen_table_column"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."gen_table_column" IS '代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_auth_config";
CREATE TABLE "public"."sys_auth_config"
(
    "config_id"          int8 NOT NULL                               DEFAULT nextval('sys_auth_config_config_id_seq'::regclass),
    "type"               varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "name"               varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "icon"               text COLLATE "pg_catalog"."default",
    "help"               varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "client_id"          varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "client_secret"      varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "redirect_uri"       varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "auth_request_class" varchar(500) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "status"             char(1) COLLATE "pg_catalog"."default"      DEFAULT '0'::bpchar,
    "create_by"          varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time"        timestamp(6),
    "update_by"          varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time"        timestamp(6),
    "remark"             varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_auth_config"."config_id" IS '配置ID';
COMMENT ON COLUMN "public"."sys_auth_config"."type" IS '登录平台';
COMMENT ON COLUMN "public"."sys_auth_config"."name" IS '登录名称';
COMMENT ON COLUMN "public"."sys_auth_config"."icon" IS '登录图标';
COMMENT ON COLUMN "public"."sys_auth_config"."help" IS '登录帮助';
COMMENT ON COLUMN "public"."sys_auth_config"."client_id" IS '客户端Id';
COMMENT ON COLUMN "public"."sys_auth_config"."client_secret" IS '客户端秘钥';
COMMENT ON COLUMN "public"."sys_auth_config"."redirect_uri" IS '回调地址';
COMMENT ON COLUMN "public"."sys_auth_config"."auth_request_class" IS '授权请求类';
COMMENT ON COLUMN "public"."sys_auth_config"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_auth_config"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_auth_config"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_auth_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_auth_config"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_auth_config" IS '第三方登录配置表';

-- ----------------------------
-- Records of sys_auth_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_auth_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_auth_user";
CREATE TABLE "public"."sys_auth_user"
(
    "auth_id"     int8                                        NOT NULL DEFAULT nextval('sys_auth_user_auth_id_seq'::regclass),
    "uuid"        varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "user_id"     int8,
    "login_name"  varchar(30) COLLATE "pg_catalog"."default"  NOT NULL,
    "user_name"   varchar(30) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "avatar"      varchar(500) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "email"       varchar(255) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "source"      varchar(255) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_auth_user"."auth_id" IS '授权ID';
COMMENT ON COLUMN "public"."sys_auth_user"."uuid" IS '第三方平台用户唯一ID';
COMMENT ON COLUMN "public"."sys_auth_user"."user_id" IS '系统用户ID';
COMMENT ON COLUMN "public"."sys_auth_user"."login_name" IS '登录账号';
COMMENT ON COLUMN "public"."sys_auth_user"."user_name" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_auth_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "public"."sys_auth_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_auth_user"."source" IS '用户来源';
COMMENT ON COLUMN "public"."sys_auth_user"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_auth_user" IS '第三方授权表';

-- ----------------------------
-- Records of sys_auth_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_config";
CREATE TABLE "public"."sys_config"
(
    "config_id"    int8 NOT NULL                               DEFAULT nextval('sys_config_config_id_seq'::regclass),
    "config_name"  varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "config_key"   varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "config_value" text COLLATE "pg_catalog"."default",
    "config_type"  char(1) COLLATE "pg_catalog"."default"      DEFAULT 'N'::bpchar,
    "create_by"    varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time"  timestamp(6),
    "update_by"    varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time"  timestamp(6),
    "remark"       varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_config"."config_id" IS '参数主键';
COMMENT ON COLUMN "public"."sys_config"."config_name" IS '参数名称';
COMMENT ON COLUMN "public"."sys_config"."config_key" IS '参数键名';
COMMENT ON COLUMN "public"."sys_config"."config_value" IS '参数键值';
COMMENT ON COLUMN "public"."sys_config"."config_type" IS '系统内置（Y是 N否）';
COMMENT ON COLUMN "public"."sys_config"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_config"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_config"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_config"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_config" IS '参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO "public"."sys_config"
VALUES (1, '安全配置-初始密码配置', 'security.initPassword', 'MapGIS123', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '用户初始密码');
INSERT INTO "public"."sys_config"
VALUES (2, '安全配置-用户注册配置', 'security.register', '{"enabled":true,"defaultRoleIds":[3]}', 'Y', 'admin', '2022-03-23 22:12:32', '',
        NULL, '用户注册管理');
INSERT INTO "public"."sys_config"
VALUES (3, '系统配置-基本配置', 'system.base',
        '{"header":{"defaultLogoAndTitle":true,"logo":"","title":""},"footer":{"copyright":"Copyright © 2023 武汉中地数码科技有限公司"}}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '系统基本配置');
INSERT INTO "public"."sys_config"
VALUES (4, '安全配置-密码安全配置', 'security.passwordProtected', '{"enabled":true,"maxRetryCount":5,"lockTime":10,"isLockedByIp":true}',
        'Y', 'admin', '2022-03-23 22:12:32', '', NULL, '密码安全配置');
INSERT INTO "public"."sys_config"
VALUES (5, '安全配置-登录配置', 'security.login',
        '{"soloLoginEnabled":true,"captchaEnabled":true,"tipEnabled":true,"captchaType":"math","maxRetryCount":1,"recordTime":10}', 'Y',
        'admin', '2022-03-23 22:12:32', '', NULL, '用户登录配置');
INSERT INTO "public"."sys_config"
VALUES (6, '安全配置-第三方登录配置', 'security.oauth', '{"defaultUserGroupIds":[1]}', 'Y', 'admin', '2022-03-23 22:12:32', '', NULL,
        '第三方登录配置');
INSERT INTO "public"."sys_config"
VALUES (7, '安全配置-CAS登录配置', 'security.cas', '{"enabled":false,"isReserveDefaultLogin":false,"casServerUrl":""}', 'Y', 'admin',
        '2022-03-23 22:12:32', '', NULL, 'CAS登录配置');
INSERT INTO "public"."sys_config"
VALUES (8, '安全配置-LDAP登录配置', 'security.ldap',
        '{"enabled":false,"url":"","base":"","userDn":"","password":"","defaultUserGroupIds":[1],"roleMapping":[]}', 'Y', 'admin',
        '2022-03-23 22:12:32', '', NULL, 'LDAP登录配置');
INSERT INTO "public"."sys_config"
VALUES (9, '日志配置', 'log', '{"systemLoglevel":"INFO","httpAccessEnabled":false,"httpAccessMonitorUrls":[]}', 'Y', 'admin',
        '2022-03-23 22:12:32', '', NULL, '日志配置');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dept";
CREATE TABLE "public"."sys_dept"
(
    "dept_id"     int8 NOT NULL                              DEFAULT nextval('sys_dept_dept_id_seq'::regclass),
    "parent_id"   int8                                       DEFAULT 0,
    "ancestors"   varchar(50) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "dept_name"   varchar(30) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "order_num"   int4                                       DEFAULT 0,
    "del_flag"    char(1) COLLATE "pg_catalog"."default"     DEFAULT '0'::bpchar,
    "create_by"   varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "update_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_dept"."dept_id" IS '部门id';
COMMENT ON COLUMN "public"."sys_dept"."parent_id" IS '父部门id';
COMMENT ON COLUMN "public"."sys_dept"."ancestors" IS '祖级列表';
COMMENT ON COLUMN "public"."sys_dept"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_dept"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_dept"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_dept"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dept"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dept"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dept"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_dept" IS '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO "public"."sys_dept"
VALUES (100, 0, '0', '内置部门', 0, '0', 'admin', '2022-03-23 22:12:32', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_data";
CREATE TABLE "public"."sys_dict_data"
(
    "dict_code"   int8 NOT NULL                               DEFAULT nextval('sys_dict_data_dict_code_seq'::regclass),
    "dict_sort"   int4                                        DEFAULT 0,
    "dict_label"  varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "dict_value"  varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "dict_type"   varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "css_class"   varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "list_class"  varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "is_default"  char(1) COLLATE "pg_catalog"."default"      DEFAULT 'N'::bpchar,
    "status"      char(1) COLLATE "pg_catalog"."default"      DEFAULT '0'::bpchar,
    "create_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_dict_data"."dict_code" IS '字典编码';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_sort" IS '字典排序';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_label" IS '字典标签';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_value" IS '字典键值';
COMMENT ON COLUMN "public"."sys_dict_data"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."sys_dict_data"."css_class" IS '样式属性（其他样式扩展）';
COMMENT ON COLUMN "public"."sys_dict_data"."list_class" IS '表格回显样式';
COMMENT ON COLUMN "public"."sys_dict_data"."is_default" IS '是否默认（Y是 N否）';
COMMENT ON COLUMN "public"."sys_dict_data"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_dict_data"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_data"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict_data"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_data"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_data"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_dict_data" IS '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "public"."sys_dict_data"
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '性别男');
INSERT INTO "public"."sys_dict_data"
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '性别女');
INSERT INTO "public"."sys_dict_data"
VALUES (3, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '显示菜单');
INSERT INTO "public"."sys_dict_data"
VALUES (4, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '隐藏菜单');
INSERT INTO "public"."sys_dict_data"
VALUES (5, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (6, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data"
VALUES (7, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (8, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');
INSERT INTO "public"."sys_dict_data"
VALUES (9, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '默认分组');
INSERT INTO "public"."sys_dict_data"
VALUES (10, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统分组');
INSERT INTO "public"."sys_dict_data"
VALUES (11, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认是');
INSERT INTO "public"."sys_dict_data"
VALUES (12, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统默认否');
INSERT INTO "public"."sys_dict_data"
VALUES (13, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知');
INSERT INTO "public"."sys_dict_data"
VALUES (14, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '公告');
INSERT INTO "public"."sys_dict_data"
VALUES (15, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (16, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '关闭状态');
INSERT INTO "public"."sys_dict_data"
VALUES (17, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-08-15 22:12:32', '', NULL, '其他操作');
INSERT INTO "public"."sys_dict_data"
VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '新增操作');
INSERT INTO "public"."sys_dict_data"
VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '修改操作');
INSERT INTO "public"."sys_dict_data"
VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '删除操作');
INSERT INTO "public"."sys_dict_data"
VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '授权操作');
INSERT INTO "public"."sys_dict_data"
VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '导出操作');
INSERT INTO "public"."sys_dict_data"
VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '导入操作');
INSERT INTO "public"."sys_dict_data"
VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '强退操作');
INSERT INTO "public"."sys_dict_data"
VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '生成操作');
INSERT INTO "public"."sys_dict_data"
VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '清空操作');
INSERT INTO "public"."sys_dict_data"
VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '正常状态');
INSERT INTO "public"."sys_dict_data"
VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_dict_type";
CREATE TABLE "public"."sys_dict_type"
(
    "dict_id"     int8 NOT NULL                               DEFAULT nextval('sys_dict_type_dict_id_seq'::regclass),
    "dict_name"   varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "dict_type"   varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "status"      char(1) COLLATE "pg_catalog"."default"      DEFAULT '0'::bpchar,
    "create_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_dict_type"."dict_id" IS '字典主键';
COMMENT ON COLUMN "public"."sys_dict_type"."dict_name" IS '字典名称';
COMMENT ON COLUMN "public"."sys_dict_type"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."sys_dict_type"."status" IS '状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_dict_type"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_dict_type"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_dict_type"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_dict_type"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_dict_type"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_dict_type" IS '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "public"."sys_dict_type"
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '用户性别列表');
INSERT INTO "public"."sys_dict_type"
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '菜单状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统开关列表');
INSERT INTO "public"."sys_dict_type"
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '任务状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '任务分组列表');
INSERT INTO "public"."sys_dict_type"
VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '系统是否列表');
INSERT INTO "public"."sys_dict_type"
VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知类型列表');
INSERT INTO "public"."sys_dict_type"
VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '通知状态列表');
INSERT INTO "public"."sys_dict_type"
VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '操作类型列表');
INSERT INTO "public"."sys_dict_type"
VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-03-23 22:12:32', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job";
CREATE TABLE "public"."sys_job"
(
    "job_id"          int8                                        NOT NULL DEFAULT nextval('sys_job_job_id_seq'::regclass),
    "job_name"        varchar(64) COLLATE "pg_catalog"."default"  NOT NULL DEFAULT ''::character varying,
    "job_group"       varchar(64) COLLATE "pg_catalog"."default"  NOT NULL DEFAULT 'DEFAULT'::character varying,
    "invoke_target"   varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "cron_expression" varchar(255) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "misfire_policy"  varchar(20) COLLATE "pg_catalog"."default"           DEFAULT '3'::character varying,
    "concurrent"      char(1) COLLATE "pg_catalog"."default"               DEFAULT '1'::bpchar,
    "status"          char(1) COLLATE "pg_catalog"."default"               DEFAULT '0'::bpchar,
    "create_by"       varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "create_time"     timestamp(6),
    "update_by"       varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "update_time"     timestamp(6),
    "remark"          varchar(500) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_job"."job_id" IS '任务ID';
COMMENT ON COLUMN "public"."sys_job"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "public"."sys_job"."cron_expression" IS 'cron执行表达式';
COMMENT ON COLUMN "public"."sys_job"."misfire_policy" IS '计划执行错误策略（1立即执行 2执行一次 3放弃执行）';
COMMENT ON COLUMN "public"."sys_job"."concurrent" IS '是否并发执行（0允许 1禁止）';
COMMENT ON COLUMN "public"."sys_job"."status" IS '状态（0正常 1暂停）';
COMMENT ON COLUMN "public"."sys_job"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_job"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_job"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_job"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_job"."remark" IS '备注信息';
COMMENT ON TABLE "public"."sys_job" IS '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_job_log";
CREATE TABLE "public"."sys_job_log"
(
    "job_log_id"     int8                                        NOT NULL DEFAULT nextval('sys_job_log_job_log_id_seq'::regclass),
    "job_name"       varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "job_group"      varchar(64) COLLATE "pg_catalog"."default"  NOT NULL,
    "invoke_target"  varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
    "job_message"    varchar(500) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "status"         char(1) COLLATE "pg_catalog"."default"               DEFAULT '0'::bpchar,
    "exception_info" varchar(2000) COLLATE "pg_catalog"."default"         DEFAULT ''::character varying,
    "create_time"    timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_job_log"."job_log_id" IS '任务日志ID';
COMMENT ON COLUMN "public"."sys_job_log"."job_name" IS '任务名称';
COMMENT ON COLUMN "public"."sys_job_log"."job_group" IS '任务组名';
COMMENT ON COLUMN "public"."sys_job_log"."invoke_target" IS '调用目标字符串';
COMMENT ON COLUMN "public"."sys_job_log"."job_message" IS '日志信息';
COMMENT ON COLUMN "public"."sys_job_log"."status" IS '执行状态（0正常 1失败）';
COMMENT ON COLUMN "public"."sys_job_log"."exception_info" IS '异常信息';
COMMENT ON COLUMN "public"."sys_job_log"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."sys_job_log" IS '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_logininfor";
CREATE TABLE "public"."sys_logininfor"
(
    "info_id"    int8 NOT NULL                               DEFAULT nextval('sys_logininfor_info_id_seq'::regclass),
    "user_name"  varchar(50) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "ipaddr"     varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "browser"    varchar(50) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "os"         varchar(50) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "status"     char(1) COLLATE "pg_catalog"."default"      DEFAULT '0'::bpchar,
    "msg"        varchar(255) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "login_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_logininfor"."info_id" IS '访问ID';
COMMENT ON COLUMN "public"."sys_logininfor"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_logininfor"."ipaddr" IS '登录IP地址';
COMMENT ON COLUMN "public"."sys_logininfor"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."sys_logininfor"."os" IS '操作系统';
COMMENT ON COLUMN "public"."sys_logininfor"."status" IS '登录状态（0成功 1失败）';
COMMENT ON COLUMN "public"."sys_logininfor"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."sys_logininfor"."login_time" IS '访问时间';
COMMENT ON TABLE "public"."sys_logininfor" IS '系统访问记录';

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_menu";
CREATE TABLE "public"."sys_menu"
(
    "menu_id"      int8                                       NOT NULL DEFAULT nextval('sys_menu_menu_id_seq'::regclass),
    "menu_name"    varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "parent_id"    int8                                                DEFAULT 0,
    "order_num"    int4                                                DEFAULT 0,
    "path"         varchar(200) COLLATE "pg_catalog"."default"         DEFAULT ''::character varying,
    "component"    varchar(255) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "query_string" varchar(255) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "is_frame"     int4                                                DEFAULT 1,
    "is_cache"     int4                                                DEFAULT 0,
    "menu_type"    char(1) COLLATE "pg_catalog"."default"              DEFAULT ''::bpchar,
    "visible"      char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "status"       char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "perms"        varchar(100) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "icon"         varchar(100) COLLATE "pg_catalog"."default"         DEFAULT '#'::character varying,
    "create_by"    varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time"  timestamp(6),
    "update_by"    varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "update_time"  timestamp(6),
    "remark"       varchar(500) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_menu"."menu_id" IS '菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."menu_name" IS '菜单名称';
COMMENT ON COLUMN "public"."sys_menu"."parent_id" IS '父菜单ID';
COMMENT ON COLUMN "public"."sys_menu"."order_num" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_menu"."path" IS '路由地址';
COMMENT ON COLUMN "public"."sys_menu"."component" IS '组件路径';
COMMENT ON COLUMN "public"."sys_menu"."query_string" IS '路由参数';
COMMENT ON COLUMN "public"."sys_menu"."is_frame" IS '是否为外链（0是 1否）';
COMMENT ON COLUMN "public"."sys_menu"."is_cache" IS '是否缓存（0缓存 1不缓存）';
COMMENT ON COLUMN "public"."sys_menu"."menu_type" IS '菜单类型（M目录 C菜单 F按钮）';
COMMENT ON COLUMN "public"."sys_menu"."visible" IS '菜单状态（0显示 1隐藏）';
COMMENT ON COLUMN "public"."sys_menu"."status" IS '菜单状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_menu"."perms" IS '权限标识';
COMMENT ON COLUMN "public"."sys_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."sys_menu"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_menu"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_menu"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_menu"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_menu" IS '菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "public"."sys_menu"
VALUES (1, '监控管理', 0, 1, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-03-23 22:12:32', '', NULL,
        '监控管理目录');
INSERT INTO "public"."sys_menu"
VALUES (2, '日志管理', 0, 2, 'log', NULL, '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-03-23 22:12:32', '', NULL, '日志管理目录');
INSERT INTO "public"."sys_menu"
VALUES (3, '安全管理', 0, 3, 'security', NULL, '', 1, 0, 'M', '0', '0', '', 'safety-certificate', 'admin', '2022-03-23 22:12:32', '', NULL,
        '安全管理目录');
INSERT INTO "public"."sys_menu"
VALUES (4, '计划任务', 0, 4, 'schedule', NULL, '', 1, 0, 'M', '0', '0', '', 'job', 'admin', '2022-10-11 14:20:47', '', NULL,
        '定时任务目录');
INSERT INTO "public"."sys_menu"
VALUES (5, '消息中心', 0, 5, 'message', NULL, '', 1, 0, 'M', '0', '0', '', 'bell', 'admin', '2022-10-12 08:56:41', '', NULL,
        '消息中心目录');
INSERT INTO "public"."sys_menu"
VALUES (6, '开发扩展', 0, 6, 'dev', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-03-23 22:12:32', '', NULL, '开发扩展目录');
INSERT INTO "public"."sys_menu"
VALUES (7, '系统配置', 0, 7, 'config', NULL, '', 1, 0, 'M', '0', '0', '', 'setting', 'admin', '2022-10-11 18:33:09', '', NULL,
        '系统设置目录');
INSERT INTO "public"."sys_menu"
VALUES (100, '在线用户', 1, 1, 'online', 'monitor/online/index', '', 1, 1, 'C', '0', '0', 'monitor:online:list', 'online', 'admin',
        '2022-03-23 22:12:32', '', NULL, '在线用户菜单');
INSERT INTO "public"."sys_menu"
VALUES (101, '服务器监控', 1, 2, 'server', 'monitor/server/index', '', 1, 1, 'C', '0', '0', 'monitor:server:list', 'server', 'admin',
        '2022-03-23 22:12:32', '', NULL, '服务器监控菜单');
INSERT INTO "public"."sys_menu"
VALUES (102, '登录日志', 2, 1, 'logininfor', 'system/logininfor/index', '', 1, 1, 'C', '0', '0', 'system:logininfor:list', 'logininfor',
        'admin', '2022-03-23 22:12:32', '', NULL, '登录日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (103, '操作日志', 2, 2, 'operlog', 'system/operlog/index', '', 1, 1, 'C', '0', '0', 'system:operlog:list', 'form', 'admin',
        '2022-03-23 22:12:32', '', NULL, '操作日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (104, '用户管理', 3, 2, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin',
        '2022-03-23 22:12:32', '', NULL, '用户管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (105, '角色管理', 3, 4, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin',
        '2022-03-23 22:12:32', '', NULL, '角色管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (106, '菜单管理', 3, 5, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'treeTable', 'admin',
        '2022-03-23 22:12:32', '', NULL, '菜单管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (107, '部门管理', 3, 6, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin',
        '2022-03-23 22:12:32', '', NULL, '部门管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (108, '岗位管理', 3, 7, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin',
        '2022-03-23 22:12:32', '', NULL, '岗位管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (109, '定时任务', 4, 1, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin',
        '2022-03-23 22:12:32', '', NULL, '定时任务菜单');
INSERT INTO "public"."sys_menu"
VALUES (110, '通知公告', 5, 1, 'notice', 'system/notice/index', '', 1, 1, 'C', '0', '0', 'system:notice:list', 'message', 'admin',
        '2022-03-23 22:12:32', '', NULL, '通知公告菜单');
INSERT INTO "public"."sys_menu"
VALUES (111, '接口文档', 6, 1, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin',
        '2022-03-23 22:12:32', '', NULL, '系统接口菜单');
INSERT INTO "public"."sys_menu"
VALUES (112, '数据字典', 6, 2, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin',
        '2022-03-23 22:12:32', '', NULL, '字典管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (113, '代码生成', 6, 3, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-03-23 22:12:32',
        '', NULL, '代码生成菜单');
INSERT INTO "public"."sys_menu"
VALUES (114, '微应用路由配置', 6, 4, 'microApp', 'system/microApp/index', '', 1, 0, 'C', '0', '0', 'system:microApp:list',
        'deployment-unit', 'admin', '2022-09-26 13:54:31', '', NULL, '微应用路由配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (115, '安全配置', 3, 1, 'securityConfig', 'security/config/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'validCode',
        'admin', '2022-10-14 17:51:53', '', NULL, '安全配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (116, '基本配置', 7, 1, 'base', 'config/base/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'profile', 'admin',
        '2022-10-15 15:45:11', '', NULL, '基本配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (117, '第三方登录配置', 3, 8, 'oauth', 'security/authConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:authConfig:list',
        'security-scan', 'admin', '2022-10-21 16:11:18', '', NULL, '第三方登录配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (118, 'CAS登录配置', 3, 9, 'cas', 'security/casConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'cas', 'admin',
        '2022-10-27 14:55:00', '', NULL, 'CAS登录配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (119, '主题配置', 7, 2, 'theme', 'config/theme/index', NULL, 1, 0, 'C', '0', '0', '', 'skin', 'admin', '2022-10-31 11:39:51', '',
        NULL, '主题配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (120, 'LDAP登录配置', 3, 10, 'ldap', 'security/ldapConfig/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'ldap', 'admin',
        '2022-11-03 14:55:44', '', NULL, 'LDAP登录配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (121, '系统日志', 2, 3, 'systemlog', 'log/systemlog/index', NULL, 1, 1, 'C', '0', '0', 'system:systemlog:list', 'edit', 'admin',
        '2022-11-23 11:07:53', '', NULL, '系统日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (122, '服务访问日志', 2, 4, 'assesslog', 'log/assesslog/index', NULL, 1, 1, 'C', '0', '0', 'system:assesslog:list', 'file-text',
        'admin', '2022-11-28 17:23:54', '', NULL, '服务访问日志菜单');
INSERT INTO "public"."sys_menu"
VALUES (123, '日志配置', 2, 5, 'logConfig', 'log/config/index', NULL, 1, 0, 'C', '0', '0', 'system:config:query', 'file-done', 'admin',
        '2022-11-30 11:45:39', '', NULL, '日志配置菜单');
INSERT INTO "public"."sys_menu"
VALUES (124, '服务器性能监控', 1, 3, 'performance', 'monitor/performance/index', NULL, 1, 1, 'C', '0', '0',
        'monitor:serverPerformance:list', 'fund', 'admin', '2022-12-06 10:54:54', '', NULL, '服务器性能监控菜单');
INSERT INTO "public"."sys_menu"
VALUES (125, '用户组管理', 3, 3, 'usergroup', 'system/usergroup/index', NULL, 1, 0, 'C', '0', '0', 'system:usergroup:list', 'team', 'admin',
        '2022-12-23 16:32:49', '', NULL, '用户组管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (126, '文件管理', 0, 3, 'file', 'file/storage/index', '', 1, 0, 'C', '0', '0', 'file:storage:list', 'copy', 'admin',
        '2023-09-21 10:15:33', '', NULL, '文件管理菜单');
INSERT INTO "public"."sys_menu"
VALUES (1000, '在线查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1001, '批量强退', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1002, '单条强退', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1003, '登录查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1004, '登录删除', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1005, '日志导出', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1006, '账号解锁', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', '2022-08-15 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1007, '操作查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1008, '操作删除', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1009, '日志导出', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1010, '用户查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1011, '用户新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1012, '用户修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1013, '用户删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1014, '用户导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1015, '用户导入', 104, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1016, '重置密码', 104, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1017, '角色查询', 105, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1018, '角色新增', 105, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1019, '角色修改', 105, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1020, '角色删除', 105, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1021, '角色导出', 105, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1022, '菜单查询', 106, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1023, '菜单新增', 106, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1024, '菜单修改', 106, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1025, '菜单删除', 106, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1026, '部门查询', 107, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1027, '部门新增', 107, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1028, '部门修改', 107, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1029, '部门删除', 107, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1030, '岗位导出', 108, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1031, '岗位查询', 108, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1032, '岗位新增', 108, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1033, '岗位修改', 108, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1034, '岗位删除', 108, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1035, '任务查询', 109, 1, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1036, '任务新增', 109, 2, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1037, '任务修改', 109, 3, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1038, '任务删除', 109, 4, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1039, '状态修改', 109, 5, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2022-03-23 22:12:32', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1040, '任务导出', 109, 7, '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1041, '公告查询', 110, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1042, '公告新增', 110, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1043, '公告修改', 110, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1044, '公告删除', 110, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1045, '字典查询', 112, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1046, '字典新增', 112, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1047, '字典修改', 112, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1048, '字典删除', 112, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1049, '字典导出', 112, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1050, '生成查询', 113, 1, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1051, '生成修改', 113, 2, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1052, '生成删除', 113, 3, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1053, '导入代码', 113, 4, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1054, '预览代码', 113, 5, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1055, '生成代码', 113, 6, '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-03-23 22:12:32', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1056, '微应用路由查询', 114, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:query', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1057, '微应用路由新增', 114, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:add', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1058, '微应用路由修改', 114, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:edit', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1059, '微应用路由删除', 114, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:remove', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1060, '微应用路由导出', 114, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:microApp:export', '#', 'admin', '2022-09-26 13:54:31', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1061, '配置修改', 115, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-14 17:54:02', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1062, '配置修改', 116, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-15 15:45:38', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1063, '第三方登录配置查询', 117, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:query', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1064, '第三方登录配置新增', 117, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:add', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1065, '第三方登录配置修改', 117, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:edit', '#', 'admin', '2022-10-21 16:11:18',
        '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1066, '第三方登录配置删除', 117, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:remove', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1067, '第三方登录配置导出', 117, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:authConfig:export', '#', 'admin',
        '2022-10-21 16:11:18', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1068, '第三方登录角色配置查询', 117, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-11-03 14:07:26',
        '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1069, '第三方登录角色配置修改', 117, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-11-03 14:08:45',
        '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1070, '配置修改', 118, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-10-27 14:55:41', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1071, '配置修改', 120, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-11-03 14:56:11', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1072, '日志导出', 121, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:systemlog:export', '#', 'admin', '2022-11-23 14:52:25', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1073, '日志导出', 122, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:assesslog:export', '#', 'admin', '2022-11-28 17:23:54', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1074, '配置修改', 123, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:config.edit', '#', 'admin', '2022-11-30 15:10:47', '', NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1075, '用户组查询', 125, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:query', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1076, '用户组新增', 125, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:add', '#', 'admin', '2022-12-23 16:32:50', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1077, '用户组修改', 125, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:edit', '#', 'admin', '2022-12-23 16:32:50', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1078, '用户组删除', 125, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:remove', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1079, '用户组导出', 125, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:usergroup:export', '#', 'admin', '2022-12-23 16:32:50', '',
        NULL, '');
INSERT INTO "public"."sys_menu"
VALUES (1080, '文件存储查询', 126, 1, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:query', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1081, '文件存储新增', 126, 2, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:add', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1082, '文件存储修改', 126, 3, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:edit', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1083, '文件存储删除', 126, 4, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:remove', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');
INSERT INTO "public"."sys_menu"
VALUES (1084, '文件存储导出', 126, 5, '', '', '', 1, 0, 'F', '0', '0', 'file:storage:export', '#', 'admin', '2023-09-21 10:15:33', '', NULL,
        '');

-- ----------------------------
-- Table structure for sys_micro_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_micro_app";
CREATE TABLE "public"."sys_micro_app"
(
    "micro_app_id" int8                                        NOT NULL DEFAULT nextval('sys_micro_app_micro_app_id_seq'::regclass),
    "name"         varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "entry"        varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "active_rule"  varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by"    varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "create_time"  timestamp(6),
    "update_by"    varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "update_time"  timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_micro_app"."micro_app_id" IS '微应用';
COMMENT ON COLUMN "public"."sys_micro_app"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_micro_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_micro_app"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_micro_app"."update_time" IS '更新时间';
COMMENT ON TABLE "public"."sys_micro_app" IS '微应用路由表';

-- ----------------------------
-- Records of sys_micro_app
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_notice";
CREATE TABLE "public"."sys_notice"
(
    "notice_id"      int8                                       NOT NULL DEFAULT nextval('sys_notice_notice_id_seq'::regclass),
    "notice_title"   varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "notice_type"    char(1) COLLATE "pg_catalog"."default"     NOT NULL,
    "notice_content" text COLLATE "pg_catalog"."default",
    "status"         char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "create_by"      varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time"    timestamp(6),
    "update_by"      varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "update_time"    timestamp(6),
    "remark"         varchar(255) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_notice"."notice_id" IS '公告ID';
COMMENT ON COLUMN "public"."sys_notice"."notice_title" IS '公告标题';
COMMENT ON COLUMN "public"."sys_notice"."notice_type" IS '公告类型（1通知 2公告）';
COMMENT ON COLUMN "public"."sys_notice"."notice_content" IS '公告内容';
COMMENT ON COLUMN "public"."sys_notice"."status" IS '公告状态（0正常 1关闭）';
COMMENT ON COLUMN "public"."sys_notice"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_notice"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_notice"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_notice"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_notice" IS '通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_oper_log";
CREATE TABLE "public"."sys_oper_log"
(
    "oper_id"        int8 NOT NULL                                DEFAULT nextval('sys_oper_log_oper_id_seq'::regclass),
    "title"          varchar(50) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "business_type"  int4                                         DEFAULT 0,
    "method"         varchar(100) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "request_method" varchar(10) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "operator_type"  int4                                         DEFAULT 0,
    "oper_name"      varchar(50) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "dept_name"      varchar(50) COLLATE "pg_catalog"."default"   DEFAULT ''::character varying,
    "oper_url"       varchar(255) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "oper_ip"        varchar(128) COLLATE "pg_catalog"."default"  DEFAULT ''::character varying,
    "oper_param"     varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "json_result"    varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "status"         int4                                         DEFAULT 0,
    "error_msg"      varchar(2000) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "oper_time"      timestamp(6),
    "cost_time"      int8                                         DEFAULT 0
)
;
COMMENT ON COLUMN "public"."sys_oper_log"."oper_id" IS '日志主键';
COMMENT ON COLUMN "public"."sys_oper_log"."title" IS '模块标题';
COMMENT ON COLUMN "public"."sys_oper_log"."business_type" IS '业务类型（0其它 1新增 2修改 3删除）';
COMMENT ON COLUMN "public"."sys_oper_log"."method" IS '方法名称';
COMMENT ON COLUMN "public"."sys_oper_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."sys_oper_log"."operator_type" IS '操作类别（0其它 1后台用户 2手机端用户）';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_name" IS '操作人员';
COMMENT ON COLUMN "public"."sys_oper_log"."dept_name" IS '部门名称';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_url" IS '请求URL';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_ip" IS '主机地址';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_param" IS '请求参数';
COMMENT ON COLUMN "public"."sys_oper_log"."json_result" IS '返回参数';
COMMENT ON COLUMN "public"."sys_oper_log"."status" IS '操作状态（0正常 1异常）';
COMMENT ON COLUMN "public"."sys_oper_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."sys_oper_log"."oper_time" IS '操作时间';
COMMENT ON COLUMN "public"."sys_oper_log"."cost_time" IS '消耗时间';
COMMENT ON TABLE "public"."sys_oper_log" IS '操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_post";
CREATE TABLE "public"."sys_post"
(
    "post_id"     int8                                       NOT NULL DEFAULT nextval('sys_post_post_id_seq'::regclass),
    "post_code"   varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
    "post_name"   varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
    "post_sort"   int4                                       NOT NULL,
    "create_by"   varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_post"."post_id" IS '岗位ID';
COMMENT ON COLUMN "public"."sys_post"."post_code" IS '岗位编码';
COMMENT ON COLUMN "public"."sys_post"."post_name" IS '岗位名称';
COMMENT ON COLUMN "public"."sys_post"."post_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_post"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_post"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_post"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_post"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_post"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_post" IS '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role";
CREATE TABLE "public"."sys_role"
(
    "role_id"             int8                                        NOT NULL DEFAULT nextval('sys_role_role_id_seq'::regclass),
    "role_name"           varchar(30) COLLATE "pg_catalog"."default"  NOT NULL,
    "role_key"            varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
    "role_sort"           int4                                        NOT NULL,
    "data_scope"          char(1) COLLATE "pg_catalog"."default"               DEFAULT '1'::bpchar,
    "menu_check_strictly" bool                                                 DEFAULT true,
    "dept_check_strictly" bool                                                 DEFAULT true,
    "is_sys"              int2                                                 DEFAULT 0,
    "del_flag"            char(1) COLLATE "pg_catalog"."default"               DEFAULT '0'::bpchar,
    "create_by"           varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "create_time"         timestamp(6),
    "update_by"           varchar(64) COLLATE "pg_catalog"."default"           DEFAULT ''::character varying,
    "update_time"         timestamp(6),
    "remark"              varchar(500) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_role"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "public"."sys_role"."role_key" IS '角色权限字符串';
COMMENT ON COLUMN "public"."sys_role"."role_sort" IS '显示顺序';
COMMENT ON COLUMN "public"."sys_role"."data_scope" IS '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）';
COMMENT ON COLUMN "public"."sys_role"."menu_check_strictly" IS '菜单树选择项是否关联显示';
COMMENT ON COLUMN "public"."sys_role"."dept_check_strictly" IS '部门树选择项是否关联显示';
COMMENT ON COLUMN "public"."sys_role"."is_sys" IS '默认角色（0否 1是）';
COMMENT ON COLUMN "public"."sys_role"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_role"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_role"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_role"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_role"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_role" IS '角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "public"."sys_role"
VALUES (1, '系统管理员', 'admin', 1, '1', true, true, 1, '0', 'admin', '2022-03-23 22:12:32', '', NULL,
        '内置的系统管理员角色，拥有整个服务器的管理权限');
INSERT INTO "public"."sys_role"
VALUES (2, '第三方用户', 'third', 2, '5', true, true, 1, '0', 'admin', '2022-10-24 11:44:51', '', NULL,
        '内置的第三方用户角色，表示用户是使用第三方登录');
INSERT INTO "public"."sys_role"
VALUES (3, '普通用户', 'user', 3, '1', true, true, 1, '0', 'admin', '2022-10-17 15:18:18', '', NULL, '内置的普通用户角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_dept";
CREATE TABLE "public"."sys_role_dept"
(
    "role_id" int8 NOT NULL,
    "dept_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_dept"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_dept"."dept_id" IS '部门ID';
COMMENT ON TABLE "public"."sys_role_dept" IS '角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_role_menu";
CREATE TABLE "public"."sys_role_menu"
(
    "role_id" int8 NOT NULL,
    "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."sys_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "public"."sys_role_menu" IS '角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "public"."sys_role_menu"
VALUES (2, 6);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 7);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 111);
INSERT INTO "public"."sys_role_menu"
VALUES (2, 119);
INSERT INTO "public"."sys_role_menu"
VALUES (3, 6);
INSERT INTO "public"."sys_role_menu"
VALUES (3, 7);
INSERT INTO "public"."sys_role_menu"
VALUES (3, 111);
INSERT INTO "public"."sys_role_menu"
VALUES (3, 119);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user";
CREATE TABLE "public"."sys_user"
(
    "user_id"     int8                                       NOT NULL DEFAULT nextval('sys_user_user_id_seq'::regclass),
    "dept_id"     int8,
    "user_name"   varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "nick_name"   varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "email"       varchar(50) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "phonenumber" varchar(11) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "sex"         char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "avatar"      varchar(100) COLLATE "pg_catalog"."default"         DEFAULT ''::character varying,
    "password"    varchar(100) COLLATE "pg_catalog"."default"         DEFAULT ''::character varying,
    "status"      char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "del_flag"    char(1) COLLATE "pg_catalog"."default"              DEFAULT '0'::bpchar,
    "login_ip"    varchar(128) COLLATE "pg_catalog"."default"         DEFAULT ''::character varying,
    "login_date"  timestamp(6),
    "create_by"   varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time" timestamp(6),
    "update_by"   varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "update_time" timestamp(6),
    "remark"      varchar(500) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user"."dept_id" IS '部门ID';
COMMENT ON COLUMN "public"."sys_user"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user"."nick_name" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_user"."phonenumber" IS '手机号码';
COMMENT ON COLUMN "public"."sys_user"."sex" IS '用户性别（0男 1女 2未知）';
COMMENT ON COLUMN "public"."sys_user"."avatar" IS '头像地址';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."status" IS '帐号状态（0正常 1停用）';
COMMENT ON COLUMN "public"."sys_user"."del_flag" IS '删除标志（0代表存在 2代表删除）';
COMMENT ON COLUMN "public"."sys_user"."login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."sys_user"."login_date" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_user" IS '用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "public"."sys_user"
VALUES (1, 100, 'admin', '系统管理员', '', '', '0', '', '$2a$10$W0oZaiVL8yYLG6PQsM2f4uYFI9kkS424BVArwpSHozx7FxdCijOfq', '0', '0', '', NULL,
        'admin', '2022-03-23 22:12:32', '', NULL, '系统管理员');

-- ----------------------------
-- Table structure for sys_user_group
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_group";
CREATE TABLE "public"."sys_user_group"
(
    "user_group_id"   int8                                       NOT NULL DEFAULT nextval('sys_user_group_user_group_id_seq'::regclass),
    "user_group_name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
    "create_by"       varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "create_time"     timestamp(6),
    "update_by"       varchar(64) COLLATE "pg_catalog"."default"          DEFAULT ''::character varying,
    "update_time"     timestamp(6),
    "remark"          varchar(500) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying
)
;
COMMENT ON COLUMN "public"."sys_user_group"."user_group_id" IS '用户组ID';
COMMENT ON COLUMN "public"."sys_user_group"."user_group_name" IS '用户组名称';
COMMENT ON COLUMN "public"."sys_user_group"."create_by" IS '创建者';
COMMENT ON COLUMN "public"."sys_user_group"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_group"."update_by" IS '更新者';
COMMENT ON COLUMN "public"."sys_user_group"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."sys_user_group"."remark" IS '备注';
COMMENT ON TABLE "public"."sys_user_group" IS '用户组表';

-- ----------------------------
-- Records of sys_user_group
-- ----------------------------
INSERT INTO "public"."sys_user_group"
VALUES (1, '第三方用户组', 'admin', '2022-12-27 11:30:51', '', NULL, '内置的第三方用户组');

-- ----------------------------
-- Table structure for sys_user_group_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_group_role";
CREATE TABLE "public"."sys_user_group_role"
(
    "user_group_id" int8 NOT NULL,
    "role_id"       int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_group_role"."user_group_id" IS '用户组ID';
COMMENT ON COLUMN "public"."sys_user_group_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_user_group_role" IS '用户组和角色关联表';

-- ----------------------------
-- Records of sys_user_group_role
-- ----------------------------
INSERT INTO "public"."sys_user_group_role"
VALUES (1, 2);

-- ----------------------------
-- Table structure for sys_user_group_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_group_user";
CREATE TABLE "public"."sys_user_group_user"
(
    "user_group_id" int8 NOT NULL,
    "user_id"       int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_group_user"."user_group_id" IS '用户组ID';
COMMENT ON COLUMN "public"."sys_user_group_user"."user_id" IS '用户ID';
COMMENT ON TABLE "public"."sys_user_group_user" IS '用户组和用户关联表';

-- ----------------------------
-- Records of sys_user_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_post";
CREATE TABLE "public"."sys_user_post"
(
    "user_id" int8 NOT NULL,
    "post_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_post"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_post"."post_id" IS '岗位ID';
COMMENT ON TABLE "public"."sys_user_post" IS '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO "public"."sys_user_post"
VALUES (1, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_user_role";
CREATE TABLE "public"."sys_user_role"
(
    "user_id" int8 NOT NULL,
    "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."sys_user_role" IS '用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "public"."sys_user_role"
VALUES (1, 1);

-- ----------------------------
-- View structure for view_self_table_columns
-- ----------------------------
DROP VIEW IF EXISTS "public"."view_self_table_columns";
CREATE VIEW "public"."view_self_table_columns" AS
SELECT columns.table_catalog,
       columns.table_schema,
       columns.table_name,
       columns.ordinal_position                                                                               AS sort,
       columns.column_name,
       columns.data_type                                                                                      AS typename,
       CASE
           WHEN columns.is_nullable::text = 'NO'::text THEN '1'::text
           ELSE NULL::text
           END                                                                                                AS is_required,
       CASE
           WHEN c.contype = 'p'::"char" THEN '1'::text
           ELSE '0'::text
           END                                                                                                AS is_pk,
       COALESCE(columns.character_maximum_length::integer, columns.numeric_precision::integer, '-1'::integer) AS length,
       columns.numeric_scale                                                                                  AS scale,
       CASE columns.is_nullable
           WHEN 'NO'::text THEN 0
           ELSE 1
           END                                                                                                AS cannull,
       columns.column_default                                                                                 AS defaultval,
       CASE
           WHEN "position"(columns.column_default::text, 'nextval'::text) > 0 THEN 1
           ELSE 0
           END                                                                                                AS isidentity,
       CASE
           WHEN "position"(columns.column_default::text, 'nextval'::text) > 0 THEN 1
           ELSE 0
           END                                                                                                AS is_increment,
       c.detext                                                                                               AS column_comment,
       c.typname                                                                                              AS column_type,
       c.contype,
       columns.ordinal_position
FROM information_schema.columns
         LEFT JOIN (SELECT pg_database.datname,
                           pg_get_userbyid(pg_class.relowner) AS tableowner,
                           pg_ns.nspname,
                           pg_class.relname,
                           pg_attr.attname,
                           pg_desc.description                AS detext,
                           pg_type.typname,
                           pg_cons.contype
                    FROM pg_class
                             LEFT JOIN pg_attribute pg_attr ON pg_attr.attrelid = pg_class.oid
                             LEFT JOIN pg_description pg_desc ON pg_desc.objoid = pg_attr.attrelid AND pg_desc.objsubid = pg_attr.attnum
                             LEFT JOIN pg_namespace pg_ns ON pg_ns.oid = pg_class.relnamespace
                             LEFT JOIN pg_database ON pg_class.relowner = pg_database.datdba
                             LEFT JOIN pg_type ON pg_attr.atttypid = pg_type.oid
                             LEFT JOIN (SELECT pg_con.conname,
                                               pg_con.connamespace,
                                               pg_con.contype,
                                               pg_con.condeferrable,
                                               pg_con.condeferred,
                                               pg_con.convalidated,
                                               pg_con.conrelid,
                                               pg_con.contypid,
                                               pg_con.conindid,
                                               pg_con.confrelid,
                                               pg_con.confupdtype,
                                               pg_con.confdeltype,
                                               pg_con.confmatchtype,
                                               pg_con.conislocal,
                                               pg_con.coninhcount,
                                               pg_con.connoinherit,
                                               pg_con.conkey,
                                               pg_con.confkey,
                                               pg_con.conpfeqop,
                                               pg_con.conppeqop,
                                               pg_con.conffeqop,
                                               pg_con.conexclop,
                                               pg_con.conbin,
                                               pg_con.consrc,
                                               unnest(pg_con.conkey) AS conkey_new
                                        FROM pg_constraint pg_con) pg_cons
                                       ON pg_attr.attrelid = pg_class.oid AND pg_attr.attnum = pg_cons.conkey_new AND
                                          pg_cons.conrelid = pg_class.oid
                    WHERE pg_attr.attnum > 0
                      AND pg_attr.attrelid = pg_class.oid) c
                   ON columns.table_catalog::name = c.datname AND columns.table_schema::name = c.nspname AND
                      columns.table_name::name = c.relname AND columns.column_name::name = c.attname;

-- ----------------------------
-- View structure for view_self_table
-- ----------------------------
DROP VIEW IF EXISTS "public"."view_self_table";
CREATE VIEW "public"."view_self_table" AS
SELECT pg_database.datname                                                 AS table_catalog,
       pg_get_userbyid(c.relowner)                                         AS tableowner,
       pg_ns.nspname                                                       AS table_schema,
       c.relname                                                           AS table_name,
       obj_description(c.relfilenode, 'pg_class'::name)::character varying AS table_comment,
       CURRENT_TIMESTAMP                                                   AS create_time,
       CURRENT_TIMESTAMP                                                   AS update_time
FROM pg_class c
         LEFT JOIN pg_namespace pg_ns ON pg_ns.oid = c.relnamespace
         LEFT JOIN pg_database ON c.relowner = pg_database.datdba
WHERE (c.relname IN (SELECT pg_tables.tablename
                     FROM pg_tables));

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."file_storage_file_id_seq"
    OWNED BY "public"."file_storage"."file_id";
SELECT setval('"public"."file_storage_file_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_column_column_id_seq"
    OWNED BY "public"."gen_table_column"."column_id";
SELECT setval('"public"."gen_table_column_column_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."gen_table_table_id_seq"
    OWNED BY "public"."gen_table"."table_id";
SELECT setval('"public"."gen_table_table_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_auth_config_config_id_seq"
    OWNED BY "public"."sys_auth_config"."config_id";
SELECT setval('"public"."sys_auth_config_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_auth_user_auth_id_seq"
    OWNED BY "public"."sys_auth_user"."auth_id";
SELECT setval('"public"."sys_auth_user_auth_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_config_config_id_seq"
    OWNED BY "public"."sys_config"."config_id";
SELECT setval('"public"."sys_config_config_id_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dept_dept_id_seq"
    OWNED BY "public"."sys_dept"."dept_id";
SELECT setval('"public"."sys_dept_dept_id_seq"', 200, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_data_dict_code_seq"
    OWNED BY "public"."sys_dict_data"."dict_code";
SELECT setval('"public"."sys_dict_data_dict_code_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_dict_type_dict_id_seq"
    OWNED BY "public"."sys_dict_type"."dict_id";
SELECT setval('"public"."sys_dict_type_dict_id_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_job_id_seq"
    OWNED BY "public"."sys_job"."job_id";
SELECT setval('"public"."sys_job_job_id_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_job_log_job_log_id_seq"
    OWNED BY "public"."sys_job_log"."job_log_id";
SELECT setval('"public"."sys_job_log_job_log_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_logininfor_info_id_seq"
    OWNED BY "public"."sys_logininfor"."info_id";
SELECT setval('"public"."sys_logininfor_info_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_menu_menu_id_seq"
    OWNED BY "public"."sys_menu"."menu_id";
SELECT setval('"public"."sys_menu_menu_id_seq"', 2000, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_micro_app_micro_app_id_seq"
    OWNED BY "public"."sys_micro_app"."micro_app_id";
SELECT setval('"public"."sys_micro_app_micro_app_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_notice_notice_id_seq"
    OWNED BY "public"."sys_notice"."notice_id";
SELECT setval('"public"."sys_notice_notice_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_oper_log_oper_id_seq"
    OWNED BY "public"."sys_oper_log"."oper_id";
SELECT setval('"public"."sys_oper_log_oper_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_post_post_id_seq"
    OWNED BY "public"."sys_post"."post_id";
SELECT setval('"public"."sys_post_post_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_role_role_id_seq"
    OWNED BY "public"."sys_role"."role_id";
SELECT setval('"public"."sys_role_role_id_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_group_user_group_id_seq"
    OWNED BY "public"."sys_user_group"."user_group_id";
SELECT setval('"public"."sys_user_group_user_group_id_seq"', 100, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_user_user_id_seq"
    OWNED BY "public"."sys_user"."user_id";
SELECT setval('"public"."sys_user_user_id_seq"', 100, false);

-- ----------------------------
-- Primary Key structure for table file_storage
-- ----------------------------
ALTER TABLE "public"."file_storage"
    ADD CONSTRAINT "file_storage_pkey" PRIMARY KEY ("file_id");

-- ----------------------------
-- Primary Key structure for table gen_table
-- ----------------------------
ALTER TABLE "public"."gen_table"
    ADD CONSTRAINT "gen_table_pkey" PRIMARY KEY ("table_id");

-- ----------------------------
-- Primary Key structure for table gen_table_column
-- ----------------------------
ALTER TABLE "public"."gen_table_column"
    ADD CONSTRAINT "gen_table_column_pkey" PRIMARY KEY ("column_id");

-- ----------------------------
-- Primary Key structure for table sys_auth_config
-- ----------------------------
ALTER TABLE "public"."sys_auth_config"
    ADD CONSTRAINT "sys_auth_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table sys_auth_user
-- ----------------------------
ALTER TABLE "public"."sys_auth_user"
    ADD CONSTRAINT "sys_auth_user_pkey" PRIMARY KEY ("auth_id");

-- ----------------------------
-- Primary Key structure for table sys_config
-- ----------------------------
ALTER TABLE "public"."sys_config"
    ADD CONSTRAINT "sys_config_pkey" PRIMARY KEY ("config_id");

-- ----------------------------
-- Primary Key structure for table sys_dept
-- ----------------------------
ALTER TABLE "public"."sys_dept"
    ADD CONSTRAINT "sys_dept_pkey" PRIMARY KEY ("dept_id");

-- ----------------------------
-- Primary Key structure for table sys_dict_data
-- ----------------------------
ALTER TABLE "public"."sys_dict_data"
    ADD CONSTRAINT "sys_dict_data_pkey" PRIMARY KEY ("dict_code");

-- ----------------------------
-- Indexes structure for table sys_dict_type
-- ----------------------------
CREATE INDEX "dict_type" ON "public"."sys_dict_type" USING btree (
                                                                  "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC
                                                                  NULLS LAST
    );

-- ----------------------------
-- Primary Key structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "public"."sys_dict_type"
    ADD CONSTRAINT "sys_dict_type_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Primary Key structure for table sys_job
-- ----------------------------
ALTER TABLE "public"."sys_job"
    ADD CONSTRAINT "sys_job_pkey" PRIMARY KEY ("job_id", "job_name", "job_group");

-- ----------------------------
-- Primary Key structure for table sys_job_log
-- ----------------------------
ALTER TABLE "public"."sys_job_log"
    ADD CONSTRAINT "sys_job_log_pkey" PRIMARY KEY ("job_log_id");

-- ----------------------------
-- Primary Key structure for table sys_logininfor
-- ----------------------------
ALTER TABLE "public"."sys_logininfor"
    ADD CONSTRAINT "sys_logininfor_pkey" PRIMARY KEY ("info_id");

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "public"."sys_menu"
    ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("menu_id");

-- ----------------------------
-- Primary Key structure for table sys_micro_app
-- ----------------------------
ALTER TABLE "public"."sys_micro_app"
    ADD CONSTRAINT "sys_micro_app_pkey" PRIMARY KEY ("micro_app_id");

-- ----------------------------
-- Primary Key structure for table sys_notice
-- ----------------------------
ALTER TABLE "public"."sys_notice"
    ADD CONSTRAINT "sys_notice_pkey" PRIMARY KEY ("notice_id");

-- ----------------------------
-- Primary Key structure for table sys_oper_log
-- ----------------------------
ALTER TABLE "public"."sys_oper_log"
    ADD CONSTRAINT "sys_oper_log_pkey" PRIMARY KEY ("oper_id");

-- ----------------------------
-- Primary Key structure for table sys_post
-- ----------------------------
ALTER TABLE "public"."sys_post"
    ADD CONSTRAINT "sys_post_pkey" PRIMARY KEY ("post_id");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "public"."sys_role"
    ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("role_id");

-- ----------------------------
-- Primary Key structure for table sys_role_dept
-- ----------------------------
ALTER TABLE "public"."sys_role_dept"
    ADD CONSTRAINT "sys_role_dept_pkey" PRIMARY KEY ("role_id", "dept_id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "public"."sys_role_menu"
    ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("role_id", "menu_id");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "public"."sys_user"
    ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("user_id");

-- ----------------------------
-- Primary Key structure for table sys_user_group
-- ----------------------------
ALTER TABLE "public"."sys_user_group"
    ADD CONSTRAINT "sys_user_group_pkey" PRIMARY KEY ("user_group_id");

-- ----------------------------
-- Primary Key structure for table sys_user_group_role
-- ----------------------------
ALTER TABLE "public"."sys_user_group_role"
    ADD CONSTRAINT "sys_user_group_role_pkey" PRIMARY KEY ("user_group_id", "role_id");

-- ----------------------------
-- Primary Key structure for table sys_user_group_user
-- ----------------------------
ALTER TABLE "public"."sys_user_group_user"
    ADD CONSTRAINT "sys_user_group_user_pkey" PRIMARY KEY ("user_group_id", "user_id");

-- ----------------------------
-- Primary Key structure for table sys_user_post
-- ----------------------------
ALTER TABLE "public"."sys_user_post"
    ADD CONSTRAINT "sys_user_post_pkey" PRIMARY KEY ("user_id", "post_id");

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "public"."sys_user_role"
    ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("user_id", "role_id");
