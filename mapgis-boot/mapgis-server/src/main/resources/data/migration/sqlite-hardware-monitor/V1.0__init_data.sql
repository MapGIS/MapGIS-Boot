/*
 Navicat Premium Data Transfer

 Source Server         : mapgis-xxx-hardware-monitor
 Source Server Type    : SQLite
 Source Server Version : 3030001
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3030001
 File Encoding         : 65001

 Date: 30/12/2022 17:20:19
*/

-- ----------------------------
-- Table structure for sys_cpu
-- ----------------------------
DROP TABLE IF EXISTS "sys_cpu";
CREATE TABLE "sys_cpu"
(
    "cpu_id"  integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "percent" float   NOT NULL,
    "time"    text    NOT NULL
);

-- ----------------------------
-- Records of sys_cpu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_disk
-- ----------------------------
DROP TABLE IF EXISTS "sys_disk";
CREATE TABLE "sys_disk"
(
    "disk_id"   integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "speed"     float   NOT NULL,
    "readwrite" integer(1) DEFAULT 0,
    "time"      text    NOT NULL
);

-- ----------------------------
-- Records of sys_disk
-- ----------------------------

-- ----------------------------
-- Table structure for sys_memory
-- ----------------------------
DROP TABLE IF EXISTS "sys_memory";
CREATE TABLE "sys_memory"
(
    "memory_id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "percent"   float   NOT NULL,
    "time"      text    NOT NULL
);

-- ----------------------------
-- Records of sys_memory
-- ----------------------------

-- ----------------------------
-- Table structure for sys_network
-- ----------------------------
DROP TABLE IF EXISTS "sys_network";
CREATE TABLE "sys_network"
(
    "network_id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "speed"      float   NOT NULL,
    "updown"     integer(1) DEFAULT 0,
    "time"       text    NOT NULL
);

-- ----------------------------
-- Records of sys_network
-- ----------------------------
