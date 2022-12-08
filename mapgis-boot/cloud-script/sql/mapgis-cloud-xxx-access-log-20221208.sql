CREATE DATABASE IF NOT EXISTS `mapgis-cloud-xxx-access-log` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `mapgis-cloud-xxx-access-log`;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : mapgis-cloud-xxx-access-log

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 08/12/2022 17:02:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_http_access
-- ----------------------------
DROP TABLE IF EXISTS `sys_http_access`;
CREATE TABLE `sys_http_access`
(
    `access_id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `ipaddr`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '客户端IP',
    `url`                longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '请求URL',
    `method`             longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '方法名称',
    `query_string`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '请求参数',
    `request_headers`    longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '请求头',
    `request_body`       longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '请求体',
    `request_body_size`  int(11)                                                       NULL DEFAULT 0 COMMENT '请求体大小',
    `response_status`    int(11)                                                       NULL DEFAULT NULL COMMENT '返回状态码',
    `response_headers`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '响应头',
    `response_body`      longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL COMMENT '响应体',
    `response_body_size` int(11)                                                       NULL DEFAULT 0 COMMENT '响应体大小',
    `time`               int(11)                                                       NULL DEFAULT NULL COMMENT '耗时',
    `access_time`        datetime                                                      NULL DEFAULT NULL COMMENT '开始时间',
    PRIMARY KEY (`access_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'Http访问日志表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_http_access
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
