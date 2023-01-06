/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : mapgis-xxx-hardware-monitor

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 30/12/2022 17:47:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_cpu
-- ----------------------------
DROP TABLE IF EXISTS `sys_cpu`;
CREATE TABLE `sys_cpu`
(
    `cpu_id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'CPU编号',
    `percent` float      NOT NULL COMMENT 'CPU使用率',
    `time`    datetime   NOT NULL COMMENT '监控时间',
    PRIMARY KEY (`cpu_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'CPU信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_cpu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_disk
-- ----------------------------
DROP TABLE IF EXISTS `sys_disk`;
CREATE TABLE `sys_disk`
(
    `disk_id`   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '磁盘编号',
    `speed`     float      NOT NULL COMMENT '磁盘读写速度',
    `readwrite` int(1)     NULL DEFAULT 0 COMMENT '读写状态（0读取 1写入）',
    `time`      datetime   NOT NULL COMMENT '监控时间',
    PRIMARY KEY (`disk_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '磁盘信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_disk
-- ----------------------------

-- ----------------------------
-- Table structure for sys_memory
-- ----------------------------
DROP TABLE IF EXISTS `sys_memory`;
CREATE TABLE `sys_memory`
(
    `memory_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内存ID',
    `percent`   float      NOT NULL COMMENT '内存使用率',
    `time`      datetime   NOT NULL COMMENT '监控时间',
    PRIMARY KEY (`memory_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '内存信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_memory
-- ----------------------------

-- ----------------------------
-- Table structure for sys_network
-- ----------------------------
DROP TABLE IF EXISTS `sys_network`;
CREATE TABLE `sys_network`
(
    `network_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '网络ID',
    `speed`      float      NOT NULL COMMENT '网络访问速度',
    `updown`     int(1)     NULL DEFAULT 0 COMMENT '上行下行状态（0上行 1下行）',
    `time`       datetime   NOT NULL COMMENT '监控时间',
    PRIMARY KEY (`network_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '网络信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_network
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
