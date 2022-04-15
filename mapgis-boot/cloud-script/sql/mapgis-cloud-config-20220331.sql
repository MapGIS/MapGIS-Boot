/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : mapgis-cloud-config

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 31/03/2022 18:36:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT 'source user',
    `src_ip`       varchar(50) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'source ip',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    `c_desc`       varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `c_use`        varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `effect`       varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `type`         varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `c_schema`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info`
VALUES (1, 'mapgis-xxx-application-dev.yml', 'DEFAULT_GROUP',
        'mapgis:\r\n  name: mapgis-xxx\r\nspring:\n  main:\n    allow-circular-references: true\n    allow-bean-definition-overriding: true\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n  # 资源信息\r\n  messages:\r\n    # 国际化资源文件路径\r\n    basename: i18n/messages\n  mvc:\n    pathmatch:\r\n      # 适配 boot 2.6 路由与 springfox 兼容\n      matching-strategy: ant_path_matcher\n  cloud:\n    sentinel:\n      filter:\n        # sentinel 在 springboot 2.6.x 不兼容问题的处理\n        enabled: false\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\r\n\r\n# 安全配置\r\nsecurity:\r\n  # 访问\r\n  access:\r\n    # 只读开关\r\n    readonlyEnabled: false\n',
        'e6a8dafc6fcec97f777836bea3ed5028', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '通用配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (2, 'mapgis-xxx-application-prod.yml', 'DEFAULT_GROUP',
        'spring:\r\n  main:\r\n    allow-circular-references: true\r\n    allow-bean-definition-overriding: true\r\n  autoconfigure:\r\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\r\n  # 资源信息\r\n  messages:\r\n    # 国际化资源文件路径\r\n    basename: i18n/messages\r\n  mvc:\r\n    pathmatch:\r\n      # 适配 boot 2.6 路由与 springfox 兼容\r\n      matching-strategy: ant_path_matcher\r\n  cloud:\r\n    sentinel:\r\n      filter:\r\n        # sentinel 在 springboot 2.6.x 不兼容问题的处理\r\n        enabled: false\r\n\r\n# feign 配置\r\nfeign:\r\n  sentinel:\r\n    enabled: true\r\n  okhttp:\r\n    enabled: true\r\n  httpclient:\r\n    enabled: false\r\n  client:\r\n    config:\r\n      default:\r\n        connectTimeout: 10000\r\n        readTimeout: 10000\r\n  compression:\r\n    request:\r\n      enabled: true\r\n    response:\r\n      enabled: true\r\n\r\n# 暴露监控端点\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \'*\'\r\n\r\n# 安全配置\r\nsecurity:\r\n  # 访问\r\n  access:\r\n    # 只读开关\r\n    readonlyEnabled: false\r\n',
        'e6a8dafc6fcec97f777836bea3ed5028', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '通用配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (3, 'mapgis-xxx-gateway-server-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  redis:\n    host: localhost\n    port: 6379\n    password: \n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: mapgis-xxx-auth-server\n          uri: lb://mapgis-xxx-auth-server\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n        # 定时任务\n        - id: mapgis-xxx-job-server\n          uri: lb://mapgis-xxx-job-server\n          predicates:\n            - Path=/schedule/**\n          filters:\n        # 系统模块\n        - id: mapgis-xxx-system-server\n          uri: lb://mapgis-xxx-system-server\n          predicates:\n            - Path=/system/**\n          filters:\n        # 文件服务\n        - id: mapgis-xxx-file-server\n          uri: lb://mapgis-xxx-file-server\n          predicates:\n            - Path=/file/**\n          filters:\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\r\n    # 验证码类型 math 数组计算 char 字符验证\n    type: math\n  # 防止XSS攻击\n  xss:\r\n    # 过滤开关\n    enabled: true\r\n    # 排除链接\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n',
        '75e569565ece9f2787f93c29407bc8cc', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '网关模块', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (4, 'mapgis-xxx-gateway-server-prod.yml', 'DEFAULT_GROUP',
        'spring:\r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n  cloud:\r\n    gateway:\r\n      discovery:\r\n        locator:\r\n          lowerCaseServiceId: true\r\n          enabled: true\r\n      routes:\r\n        # 认证中心\r\n        - id: mapgis-xxx-auth-server\r\n          uri: lb://mapgis-xxx-auth-server\r\n          predicates:\r\n            - Path=/auth/**\r\n          filters:\r\n            # 验证码处理\r\n            - CacheRequestFilter\r\n            - ValidateCodeFilter\r\n        # 定时任务\r\n        - id: mapgis-xxx-job-server\r\n          uri: lb://mapgis-xxx-job-server\r\n          predicates:\r\n            - Path=/schedule/**\r\n          filters:\r\n        # 系统模块\r\n        - id: mapgis-xxx-system-server\r\n          uri: lb://mapgis-xxx-system-server\r\n          predicates:\r\n            - Path=/system/**\r\n          filters:\r\n        # 文件服务\r\n        - id: mapgis-xxx-file-server\r\n          uri: lb://mapgis-xxx-file-server\r\n          predicates:\r\n            - Path=/file/**\r\n          filters:\r\n\r\n# 安全配置\r\nsecurity:\r\n  # 验证码\r\n  captcha:\r\n    enabled: true\r\n    # 验证码类型 math 数组计算 char 字符验证\r\n    type: math\r\n  # 防止XSS攻击\r\n  xss:\r\n    # 过滤开关\r\n    enabled: true\r\n    # 排除链接\r\n    excludeUrls:\r\n      - /system/notice\r\n  # 不校验白名单\r\n  ignore:\r\n    whites:\r\n      - /auth/logout\r\n      - /auth/login\r\n      - /auth/register\r\n      - /*/v2/api-docs\r\n      - /csrf\r\n',
        '75e569565ece9f2787f93c29407bc8cc', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '网关模块', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (5, 'mapgis-xxx-auth-server-dev.yml', 'DEFAULT_GROUP',
        'spring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n',
        'b7354e1eb62c2d846d44a796d9ec6930', '2022-03-31 00:00:00', '2022-03-31 12:00:00', NULL, '0:0:0:0:0:0:0:1', '',
        '', '认证中心', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (6, 'mapgis-xxx-auth-server-prod.yml', 'DEFAULT_GROUP',
        'spring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n',
        'b7354e1eb62c2d846d44a796d9ec6930', '2022-03-31 00:00:00', '2022-03-31 12:00:00', NULL, '0:0:0:0:0:0:0:1', '',
        '', '认证中心', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (7, 'mapgis-xxx-monitor-server-dev.yml', 'DEFAULT_GROUP',
        '# spring\nspring: \n  security:\n    user:\n      name: admin\n      password: sa.mapgis\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n',
        '17a5ae101eee1632cf44285e62057ccb', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '监控中心', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (8, 'mapgis-xxx-monitor-server-prod.yml', 'DEFAULT_GROUP',
        '# spring\nspring: \n  security:\n    user:\n      name: admin\n      password: sa.mapgis\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n',
        '17a5ae101eee1632cf44285e62057ccb', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '监控中心', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (9, 'mapgis-xxx-system-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: localhost\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: sa.mapgis\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/mapgis-cloud-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档',
        'a2a5d3b5cf25141b08ed79315ab273d0', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '系统模块', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (10, 'mapgis-xxx-system-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: localhost\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: sa.mapgis\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/mapgis-cloud-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档',
        'a2a5d3b5cf25141b08ed79315ab273d0', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '系统模块', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (11, 'mapgis-xxx-job-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: localhost\n    port: 6379\n    password: \n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://localhost:3306/mapgis-cloud-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 定时任务接口文档\n',
        'fe43dba3a21f615ed0264a2672be982b', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '定时任务', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (12, 'mapgis-xxx-job-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: localhost\n    port: 6379\n    password: \n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://localhost:3306/mapgis-cloud-xxx?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 定时任务接口文档\n',
        'fe43dba3a21f615ed0264a2672be982b', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '定时任务', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (13, 'mapgis-xxx-file-server-dev.yml', 'DEFAULT_GROUP',
        '# 本地文件上传\nfile:\n  domain: http://127.0.0.1:9300\n  path: /upload\n  prefix: /prefix\n\n# FastDFS配置\nfdfs:\n  domain: http://127.0.0.1\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 127.0.0.1:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test',
        '75448789bd5377a974e02cb4e9e92ece', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '文件服务', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (14, 'mapgis-xxx-file-server-prod.yml', 'DEFAULT_GROUP',
        '# 本地文件上传\nfile:\n  domain: http://127.0.0.1:9300\n  path: /upload\n  prefix: /prefix\n\n# FastDFS配置\nfdfs:\n  domain: http://127.0.0.1\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 127.0.0.1:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test',
        '75448789bd5377a974e02cb4e9e92ece', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '文件服务', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info`
VALUES (15, 'sentinel-mapgis-xxx-gateway-server', 'DEFAULT_GROUP',
        '[\n  {\n    \"resource\": \"mapgis-xxx-auth-server\",\n    \"count\": 500,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  },\n	{\n    \"resource\": \"mapgis-xxx-system-server\",\n    \"count\": 1000,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  },\n	{\n    \"resource\": \"mapgis-xxx-job-server\",\n    \"count\": 300,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  }\n]',
        '0e5d2748b0c96a55b83462b5220ecf3d', '2022-03-31 00:00:00', '2022-03-31 12:00:00', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '限流策略', 'null', 'null', 'json', 'null');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT '内容',
    `gmt_modified` datetime                                         NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '增加租户字段'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`           bigint(20)                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'group_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin      NOT NULL COMMENT 'content',
    `beta_ips`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'betaIps',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin          NULL COMMENT 'source user',
    `src_ip`       varchar(50) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'source ip',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_beta'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`
(
    `id`           bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT 'source user',
    `src_ip`       varchar(50) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_tag'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint(20)                                       NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint(20)                                       NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`) USING BTREE,
    UNIQUE INDEX `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_tag_relation'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`
(
    `id`                bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_group_id` (`group_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`           bigint(64) UNSIGNED                              NOT NULL,
    `nid`          bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT,
    `data_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `group_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `app_name`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'app_name',
    `content`      longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL,
    `md5`          varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `gmt_create`   datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `src_user`     text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    `src_ip`       varchar(50) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `op_type`      char(10) CHARACTER SET utf8 COLLATE utf8_bin     NULL     DEFAULT NULL,
    `tenant_id`    varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`nid`) USING BTREE,
    INDEX `idx_gmt_create` (`gmt_create`) USING BTREE,
    INDEX `idx_gmt_modified` (`gmt_modified`) USING BTREE,
    INDEX `idx_did` (`data_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '多租户改造'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `role`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `action`   varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`
(
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `role`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    UNIQUE INDEX `idx_user_role` (`username`, `role`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles`
VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
    `usage`             int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '使用量',
    `max_size`          int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int(10) UNSIGNED                                 NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
    `gmt_create`        datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '租户容量信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`
(
    `id`            bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint(20)                                       NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint(20)                                       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tenant_info_kptenantid` (`kp`, `tenant_id`) USING BTREE,
    INDEX `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'tenant_info'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `enabled`  tinyint(1)                                                    NOT NULL,
    PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
