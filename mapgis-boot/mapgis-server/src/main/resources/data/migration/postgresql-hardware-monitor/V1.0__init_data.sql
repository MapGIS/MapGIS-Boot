/*
 Navicat Premium Data Transfer

 Source Server         : localhost-pg
 Source Server Type    : PostgreSQL
 Source Server Version : 100023
 Source Host           : localhost:5432
 Source Catalog        : mapgis-xxx-hardware-monitor
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100023
 File Encoding         : 65001

 Date: 16/10/2023 14:20:29
*/


-- ----------------------------
-- Sequence structure for sys_cpu_cpu_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_cpu_cpu_id_seq";
CREATE SEQUENCE "public"."sys_cpu_cpu_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_disk_disk_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_disk_disk_id_seq";
CREATE SEQUENCE "public"."sys_disk_disk_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_memory_memory_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_memory_memory_id_seq";
CREATE SEQUENCE "public"."sys_memory_memory_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Sequence structure for sys_network_network_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."sys_network_network_id_seq";
CREATE SEQUENCE "public"."sys_network_network_id_seq"
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;

-- ----------------------------
-- Table structure for sys_cpu
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_cpu";
CREATE TABLE "public"."sys_cpu"
(
    "cpu_id"  int8         NOT NULL DEFAULT nextval('sys_cpu_cpu_id_seq'::regclass),
    "percent" float4       NOT NULL,
    "time"    timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_cpu"."cpu_id" IS 'CPU编号';
COMMENT ON COLUMN "public"."sys_cpu"."percent" IS 'CPU使用率';
COMMENT ON COLUMN "public"."sys_cpu"."time" IS '监控时间';
COMMENT ON TABLE "public"."sys_cpu" IS 'CPU信息表';

-- ----------------------------
-- Records of sys_cpu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_disk
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_disk";
CREATE TABLE "public"."sys_disk"
(
    "disk_id"   int8         NOT NULL DEFAULT nextval('sys_disk_disk_id_seq'::regclass),
    "speed"     float4       NOT NULL,
    "readwrite" int4                  DEFAULT 0,
    "time"      timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_disk"."disk_id" IS '磁盘编号';
COMMENT ON COLUMN "public"."sys_disk"."speed" IS '磁盘读写速度';
COMMENT ON COLUMN "public"."sys_disk"."readwrite" IS '读写状态（0读取 1写入）';
COMMENT ON COLUMN "public"."sys_disk"."time" IS '监控时间';
COMMENT ON TABLE "public"."sys_disk" IS '磁盘信息表';

-- ----------------------------
-- Records of sys_disk
-- ----------------------------

-- ----------------------------
-- Table structure for sys_memory
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_memory";
CREATE TABLE "public"."sys_memory"
(
    "memory_id" int8         NOT NULL DEFAULT nextval('sys_memory_memory_id_seq'::regclass),
    "percent"   float4       NOT NULL,
    "time"      timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_memory"."memory_id" IS '内存ID';
COMMENT ON COLUMN "public"."sys_memory"."percent" IS '内存使用率';
COMMENT ON COLUMN "public"."sys_memory"."time" IS '监控时间';
COMMENT ON TABLE "public"."sys_memory" IS '内存信息表';

-- ----------------------------
-- Records of sys_memory
-- ----------------------------

-- ----------------------------
-- Table structure for sys_network
-- ----------------------------
DROP TABLE IF EXISTS "public"."sys_network";
CREATE TABLE "public"."sys_network"
(
    "network_id" int8         NOT NULL DEFAULT nextval('sys_network_network_id_seq'::regclass),
    "speed"      float4       NOT NULL,
    "updown"     int4                  DEFAULT 0,
    "time"       timestamp(6) NOT NULL
)
;
COMMENT ON COLUMN "public"."sys_network"."network_id" IS '网络ID';
COMMENT ON COLUMN "public"."sys_network"."speed" IS '网络访问速度';
COMMENT ON COLUMN "public"."sys_network"."updown" IS '上行下行状态（0上行 1下行）';
COMMENT ON COLUMN "public"."sys_network"."time" IS '监控时间';
COMMENT ON TABLE "public"."sys_network" IS '网络信息表';

-- ----------------------------
-- Records of sys_network
-- ----------------------------

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_cpu_cpu_id_seq"
    OWNED BY "public"."sys_cpu"."cpu_id";
SELECT setval('"public"."sys_cpu_cpu_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_disk_disk_id_seq"
    OWNED BY "public"."sys_disk"."disk_id";
SELECT setval('"public"."sys_disk_disk_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_memory_memory_id_seq"
    OWNED BY "public"."sys_memory"."memory_id";
SELECT setval('"public"."sys_memory_memory_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."sys_network_network_id_seq"
    OWNED BY "public"."sys_network"."network_id";
SELECT setval('"public"."sys_network_network_id_seq"', 1, false);

-- ----------------------------
-- Primary Key structure for table sys_cpu
-- ----------------------------
ALTER TABLE "public"."sys_cpu"
    ADD CONSTRAINT "sys_cpu_pkey" PRIMARY KEY ("cpu_id");

-- ----------------------------
-- Primary Key structure for table sys_disk
-- ----------------------------
ALTER TABLE "public"."sys_disk"
    ADD CONSTRAINT "sys_disk_pkey" PRIMARY KEY ("disk_id");

-- ----------------------------
-- Primary Key structure for table sys_memory
-- ----------------------------
ALTER TABLE "public"."sys_memory"
    ADD CONSTRAINT "sys_memory_pkey" PRIMARY KEY ("memory_id");

-- ----------------------------
-- Primary Key structure for table sys_network
-- ----------------------------
ALTER TABLE "public"."sys_network"
    ADD CONSTRAINT "sys_network_pkey" PRIMARY KEY ("network_id");
