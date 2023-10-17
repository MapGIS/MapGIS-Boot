/*
 Navicat Premium Data Transfer

 Source Server         : localhost-pg
 Source Server Type    : PostgreSQL
 Source Server Version : 100023
 Source Host           : localhost:5432
 Source Catalog        : mapgis-xxx-access-log
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100023
 File Encoding         : 65001

 Date: 16/10/2023 14:16:27
*/


-- ----------------------------
-- Sequence structure for sys_http_access_access_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_http_access_access_id_seq";
CREATE SEQUENCE "public"."sys_http_access_access_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Table structure for sys_http_access
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_http_access";
CREATE TABLE "public"."sys_http_access"
(
    "access_id"          int8 NOT NULL                               DEFAULT nextval('sys_http_access_access_id_seq'::regclass),
    "ipaddr"             varchar(128) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
    "url"                text COLLATE "pg_catalog"."default",
    "method"             text COLLATE "pg_catalog"."default",
    "query_string"       text COLLATE "pg_catalog"."default",
    "request_headers"    text COLLATE "pg_catalog"."default",
    "request_body"       text COLLATE "pg_catalog"."default",
    "request_body_size"  int4                                        DEFAULT 0,
    "response_status"    int4,
    "response_headers"   text COLLATE "pg_catalog"."default",
    "response_body"      text COLLATE "pg_catalog"."default",
    "response_body_size" int4                                        DEFAULT 0,
    "time"               int4,
    "access_time"        timestamp(6)
)
;
COMMENT ON COLUMN "public"."sys_http_access"."access_id" IS '访问ID';
COMMENT ON COLUMN "public"."sys_http_access"."ipaddr" IS '客户端IP';
COMMENT ON COLUMN "public"."sys_http_access"."url" IS '请求URL';
COMMENT ON COLUMN "public"."sys_http_access"."method" IS '方法名称';
COMMENT ON COLUMN "public"."sys_http_access"."query_string" IS '请求参数';
COMMENT ON COLUMN "public"."sys_http_access"."request_headers" IS '请求头';
COMMENT ON COLUMN "public"."sys_http_access"."request_body" IS '请求体';
COMMENT ON COLUMN "public"."sys_http_access"."request_body_size" IS '请求体大小';
COMMENT ON COLUMN "public"."sys_http_access"."response_status" IS '返回状态码';
COMMENT ON COLUMN "public"."sys_http_access"."response_headers" IS '响应头';
COMMENT ON COLUMN "public"."sys_http_access"."response_body" IS '响应体';
COMMENT ON COLUMN "public"."sys_http_access"."response_body_size" IS '响应体大小';
COMMENT ON COLUMN "public"."sys_http_access"."time" IS '耗时';
COMMENT ON COLUMN "public"."sys_http_access"."access_time" IS '开始时间';
COMMENT ON TABLE "public"."sys_http_access" IS 'Http访问日志表';

-- ----------------------------
-- Records of sys_http_access
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_http_access_access_id_seq"
    OWNED BY "public"."sys_http_access"."access_id";
SELECT setval('"public"."sys_http_access_access_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table sys_http_access
-- ----------------------------
ALTER TABLE "public"."sys_http_access"
    ADD CONSTRAINT "sys_http_access_pkey" PRIMARY KEY ("access_id");
