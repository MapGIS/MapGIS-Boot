/*
 Navicat Premium Data Transfer

 Source Server         : mapgis-xxx-access-log
 Source Server Type    : SQLite
 Source Server Version : 3030001
 Source Schema         : main

 Target Server Type    : SQLite
 Target Server Version : 3030001
 File Encoding         : 65001

 Date: 28/11/2022 21:27:36
*/

-- ----------------------------
-- Table structure for sys_http_access
-- ----------------------------
DROP TABLE IF EXISTS "sys_http_access";
CREATE TABLE "sys_http_access"
(
    "access_id"          integer NOT NULL PRIMARY KEY AUTOINCREMENT,
    "ipaddr"             text(128) DEFAULT '',
    "url"                text      DEFAULT '',
    "method"             text      DEFAULT '',
    "query_string"       text      DEFAULT '',
    "request_headers"    text      DEFAULT '',
    "request_body"       text      DEFAULT '',
    "request_body_size"  integer   DEFAULT 0,
    "response_status"    integer   DEFAULT NULL,
    "response_headers"   text,
    "response_body"      text,
    "response_body_size" integer   DEFAULT 0,
    "time"               integer   DEFAULT NULL,
    "access_time"        text      DEFAULT NULL
);

-- ----------------------------
-- Records of sys_http_access
-- ----------------------------

