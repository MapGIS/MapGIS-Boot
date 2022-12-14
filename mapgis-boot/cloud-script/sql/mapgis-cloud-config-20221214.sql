CREATE DATABASE IF NOT EXISTS `mapgis-cloud-config` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `mapgis-cloud-config`;

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

 Date: 14/12/2022 10:23:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`
(
    `id`                 bigint(20)                                       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id`           varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `content`            longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL COMMENT 'content',
    `md5`                varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT 'source user',
    `src_ip`             varchar(50) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'source ip',
    `app_name`           varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `tenant_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    `c_desc`             varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL,
    `c_use`              varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `effect`             varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `type`               varchar(64) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `c_schema`           text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT '秘钥',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info`
VALUES (1, 'mapgis-xxx-application-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  # 资源信息\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/messages\n  # jackson配置\n  jackson:\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n\n# Swagger配置\nswagger:\n  # 是否开启swagger\n  enabled: true\n  # 标题\n  title: MapGIS服务器产品 接口文档\n  # 描述\n  description: MapGIS服务器产品\n  gateway: http://${GATEWAY_PUBLIC_HOST:${mapgis.product.full-name}-gateway}:${GATEWAY_PUBLIC_PORT:8080}\n\n# SpringDoc配置\nspringdoc:\n  swagger-ui:\n    # 开启Swagger UI界面\n    enabled: ${swagger.enabled}\n\n# API前缀\napi:\n  path:\n    services-prefix: /${mapgis.product.name}/rest/services\n    manager-prefix: /${mapgis.product.name}/rest/manager\n\n# 安全配置\nsecurity:\n  # 访问\n  access:\n    # 只读开关\n    readonlyEnabled: false',
        '8df54292428136e6442ba5a792bd5f56', '2022-03-31 00:00:00', '2022-07-19 02:13:12', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '通用配置', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (2, 'mapgis-xxx-application-prod.yml', 'DEFAULT_GROUP',
        'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  # 资源信息\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/messages\n  # jackson配置\n  jackson:\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    time-zone: GMT+8\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n\n# Swagger配置\nswagger:\n  # 是否开启swagger\n  enabled: true\n  # 标题\n  title: MapGIS服务器产品 接口文档\n  # 描述\n  description: MapGIS服务器产品\n  gateway: http://${GATEWAY_PUBLIC_HOST:${mapgis.product.full-name}-gateway}:${GATEWAY_PUBLIC_PORT:8080}\n\n# SpringDoc配置\nspringdoc:\n  swagger-ui:\n    # 开启Swagger UI界面\n    enabled: ${swagger.enabled}\n\n# API前缀\napi:\n  path:\n    services-prefix: /${mapgis.product.name}/rest/services\n    manager-prefix: /${mapgis.product.name}/rest/manager\n\n# 安全配置\nsecurity:\n  # 访问\n  access:\n    # 只读开关\n    readonlyEnabled: false',
        '8df54292428136e6442ba5a792bd5f56', '2022-03-31 00:00:00', '2022-07-19 03:55:13', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '通用配置', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (3, 'mapgis-xxx-gateway-server-dev.yml', 'DEFAULT_GROUP',
        'spring:\n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  cloud:\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n\n# 安全配置\nsecurity:\n  # 防止XSS攻击\n  xss:\n    # 过滤开关\n    enabled: true\n    # 排除链接\n    excludeUrls:\n      - ${api.path.manager-prefix}/system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - ${api.path.manager-prefix}/system/webConfig/**\n      - ${api.path.services-prefix}/auth/logout\n      - ${api.path.services-prefix}/auth/login\n      - ${api.path.services-prefix}/auth/register\n      - ${api.path.services-prefix}/auth/thirdLogin/**\n      - ${api.path.services-prefix}/auth/casLogin/**\n      - /${mapgis.product.name}/static/**\n      - /${mapgis.product.name}/manager/**\n      - /**/api-docs/**\n      - /csrf\n      - /',
        '940bf7c08962e58702d96f22f96d64ea', '2022-03-31 00:00:00', '2022-10-28 09:53:17', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '网关模块', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (4, 'mapgis-xxx-gateway-server-prod.yml', 'DEFAULT_GROUP',
        'spring:\n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  cloud:\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n\n# 安全配置\nsecurity:\n  # 防止XSS攻击\n  xss:\n    # 过滤开关\n    enabled: true\n    # 排除链接\n    excludeUrls:\n      - ${api.path.manager-prefix}/system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - ${api.path.manager-prefix}/system/webConfig/**\n      - ${api.path.services-prefix}/auth/logout\n      - ${api.path.services-prefix}/auth/login\n      - ${api.path.services-prefix}/auth/register\n      - ${api.path.services-prefix}/auth/thirdLogin/**\n      - ${api.path.services-prefix}/auth/casLogin/**\n      - /${mapgis.product.name}/static/**\n      - /${mapgis.product.name}/manager/**\n      - /**/api-docs/**\n      - /csrf\n      - /',
        '940bf7c08962e58702d96f22f96d64ea', '2022-03-31 00:00:00', '2022-10-28 09:53:29', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '网关模块', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (5, 'mapgis-xxx-auth-server-dev.yml', 'DEFAULT_GROUP',
        'spring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  # 配置freemarker\n  freemarker:\n    # 设置模板后缀名\n    suffix: .ftl\n    # 设置文档类型\n    content-type: text/html\n    # 设置页面编码格式\n    charset: UTF-8\n    # 设置页面缓存\n    cache: false\n    prefer-file-system-access: false\n    # 设置ftl文件路径\n    template-loader-path:\n      - classpath:/templates',
        '792fd6bb0cc55d6b2f4a60b17f4d29df', '2022-03-31 00:00:00', '2022-10-28 09:53:42', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '认证中心', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (6, 'mapgis-xxx-auth-server-prod.yml', 'DEFAULT_GROUP',
        'spring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  # 配置freemarker\n  freemarker:\n    # 设置模板后缀名\n    suffix: .ftl\n    # 设置文档类型\n    content-type: text/html\n    # 设置页面编码格式\n    charset: UTF-8\n    # 设置页面缓存\n    cache: false\n    prefer-file-system-access: false\n    # 设置ftl文件路径\n    template-loader-path:\n      - classpath:/templates',
        '792fd6bb0cc55d6b2f4a60b17f4d29df', '2022-03-31 00:00:00', '2022-10-28 09:53:53', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '认证中心', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (7, 'mapgis-xxx-monitor-server-dev.yml', 'DEFAULT_GROUP',
        '# spring\nspring: \n  security:\n    user:\n      name: admin\n      password: cloud123.mapgis\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控',
        '20ede49252525dd25c1c66a69ac28a19', '2022-03-31 00:00:00', '2022-07-19 03:58:50', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '监控中心', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (8, 'mapgis-xxx-monitor-server-prod.yml', 'DEFAULT_GROUP',
        '# spring\nspring: \n  security:\n    user:\n      name: admin\n      password: cloud123.mapgis\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控',
        '20ede49252525dd25c1c66a69ac28a19', '2022-03-31 00:00:00', '2022-07-19 03:58:57', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '监控中心', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (9, 'mapgis-xxx-system-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: cloud123.mapgis\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: ${MYSQL_USER:root}\n            password: ${MYSQL_PWD:cloud123.mapgis}\n          # HTTP访问日志库数据源\n          access_log:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/mapgis-cloud-${mapgis.product.name}-access-log?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: ${MYSQL_USER:root}\n            password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        'ea787d9a2a83339004bb705bd525f067', '2022-03-31 00:00:00', '2022-12-08 09:16:45', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '系统模块', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (10, 'mapgis-xxx-system-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: cloud123.mapgis\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: ${MYSQL_USER:root}\n            password: ${MYSQL_PWD:cloud123.mapgis}\n          # HTTP访问日志库数据源\n          access_log:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/mapgis-cloud-${mapgis.product.name}-access-log?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: ${MYSQL_USER:root}\n            password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.system\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        'ea787d9a2a83339004bb705bd525f067', '2022-03-31 00:00:00', '2022-12-08 10:25:33', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '系统模块', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (11, 'mapgis-xxx-job-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: ${MYSQL_USER:root}\n    password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        '75d6a004958254bcd877d0aa7d42bb4e', '2022-03-31 00:00:00', '2022-07-19 06:09:24', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '定时任务', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (12, 'mapgis-xxx-job-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: ${MYSQL_USER:root}\n    password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.job.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        '75d6a004958254bcd877d0aa7d42bb4e', '2022-03-31 00:00:00', '2022-07-19 06:09:59', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '定时任务', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (13, 'mapgis-xxx-file-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n\n# 本地文件上传\nfile:\n  domain: http://127.0.0.1:12000\n  path: /upload\n  prefix: /file\n\n# FastDFS配置\nfdfs:\n  domain: http://127.0.0.1\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 127.0.0.1:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test',
        'd5e40804d4b4d2a2b44b6c6169afa029', '2022-03-31 00:00:00', '2022-12-14 01:05:41', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '文件服务', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (14, 'mapgis-xxx-file-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n\n# 本地文件上传\nfile:\n  domain: http://127.0.0.1:12000\n  path: /upload\n  prefix: /file\n\n# FastDFS配置\nfdfs:\n  domain: http://127.0.0.1\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 127.0.0.1:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test',
        'd5e40804d4b4d2a2b44b6c6169afa029', '2022-03-31 00:00:00', '2022-12-14 01:06:03', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '文件服务', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (15, 'sentinel-mapgis-xxx-gateway-server', 'DEFAULT_GROUP',
        '[\n  {\n    \"resource\": \"${mapgis.product.full-name}-auth-server\",\n    \"count\": 500,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  },\n	{\n    \"resource\": \"${mapgis.product.full-name}-system-server\",\n    \"count\": 1000,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  },\n	{\n    \"resource\": \"${mapgis.product.full-name}-job-server\",\n    \"count\": 300,\n    \"grade\": 1,\n    \"limitApp\": \"default\",\n    \"strategy\": 0,\n    \"controlBehavior\": 0\n  }\n]',
        '83691fff36399ab429022a111d96e642', '2022-03-31 00:00:00', '2022-07-19 06:10:38', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '限流策略', 'null', 'null', 'json', NULL, '');
INSERT INTO `config_info`
VALUES (16, 'mapgis-xxx-gen-server-dev.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: ${MYSQL_USER:root}\n    password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.gen.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        '2bd181ba4852bc1effb40d4e02c4ee67', '2022-08-29 04:52:56', '2022-08-29 04:57:18', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '代码生成', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info`
VALUES (17, 'mapgis-xxx-gen-server-prod.yml', 'DEFAULT_GROUP',
        '# spring配置\nspring: \n  redis:\n    host: ${REDIS_HOST:${mapgis.product.full-name}-redis}\n    port: ${REDIS_PORT:6379}\n    password: ${REDIS_PWD:cloud123.mapgis}\n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://${MYSQL_HOST:${mapgis.product.full-name}-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:mapgis-cloud-${mapgis.product.name}}?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: ${MYSQL_USER:root}\n    password: ${MYSQL_PWD:cloud123.mapgis}\n\n# mybatis配置\nmybatis:\n  # 搜索指定包别名\n  typeAliasesPackage: com.zondy.mapgis.gen.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/**/*.xml',
        '2bd181ba4852bc1effb40d4e02c4ee67', '2022-08-29 04:58:18', '2022-08-29 04:58:18', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '代码生成', 'null', 'null', 'yaml', NULL, '');

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`
(
    `id`                 bigint(20)                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'data_id',
    `group_id`           varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NOT NULL COMMENT 'group_id',
    `app_name`           varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL COMMENT 'app_name',
    `content`            longtext CHARACTER SET utf8 COLLATE utf8_bin      NOT NULL COMMENT 'content',
    `beta_ips`           varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'betaIps',
    `md5`                varchar(32) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime                                          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime                                          NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text CHARACTER SET utf8 COLLATE utf8_bin          NULL COMMENT 'source user',
    `src_ip`             varchar(50) CHARACTER SET utf8 COLLATE utf8_bin   NULL     DEFAULT NULL COMMENT 'source ip',
    `tenant_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin          NULL COMMENT '秘钥',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = 'config_info_beta'
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`
(
    `id`                 bigint(64) UNSIGNED                              NOT NULL,
    `nid`                bigint(20) UNSIGNED                              NOT NULL AUTO_INCREMENT,
    `data_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `group_id`           varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `app_name`           varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT NULL COMMENT 'app_name',
    `content`            longtext CHARACTER SET utf8 COLLATE utf8_bin     NOT NULL,
    `md5`                varchar(32) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `gmt_create`         datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified`       datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `src_user`           text CHARACTER SET utf8 COLLATE utf8_bin         NULL,
    `src_ip`             varchar(50) CHARACTER SET utf8 COLLATE utf8_bin  NULL     DEFAULT NULL,
    `op_type`            char(10) CHARACTER SET utf8 COLLATE utf8_bin     NULL     DEFAULT NULL,
    `tenant_id`          varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL     DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin         NULL COMMENT '秘钥',
    PRIMARY KEY (`nid`) USING BTREE,
    INDEX `idx_gmt_create` (`gmt_create`) USING BTREE,
    INDEX `idx_gmt_modified` (`gmt_modified`) USING BTREE,
    INDEX `idx_did` (`data_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_bin COMMENT = '多租户改造'
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

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
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users`
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
