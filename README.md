# 介绍

<h2 align="center">MapGIS Boot</h2>
<h5 align="center">基于Spring Boot、Spring Cloud & Alibaba的前后端分离微服务极速后台开发框架。</h3>

MapGIS Boot 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring Cloud & Alibaba、Ant Design Vue），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、在线定时任务等。

**系统需求**

- JDK >= 1.8
- MySQL >= 5.7
- Maven >= 3.0
- Node >= 12

# 快速了解

## 项目简介

MapGIS Boot 是一款基于 Spring Boot、Spring Cloud & Alibaba、MyBatis、Jwt、Ant Design Vue 的前后端分离微服务极速后台开发框架。

## 主要特性

- 支持单体和微服务自由切换
- 支持按钮及数据权限，可自定义部门数据权限
- 支持菜单动态路由
- 完善的 XSS 防范及脚本过滤，彻底杜绝 XSS 攻击
- Maven 多项目依赖，模块及插件分项目，尽量松耦合，方便模块升级、增减模块
- 完善的日志记录体系简单注解即可实现

## 技术选型

### 开发环境

- 语言：Java 8

- IDE(JAVA)： IDEA

- IDE(前端)： VSCode

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7

### 后端

- 基础框架：Spring Boot 2.6.3.RELEASE

- 微服务框架： Spring Cloud Alibaba 2021.1.RELEASE

- 持久层框架：Mybatis

- 安全框架：Spring Security，Jwt

- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel

- 数据库连接池：阿里巴巴 Druid

- 缓存框架：Redis

- 日志打印：Logback

- 其他：Poi，Swagge UI，Quartz, Lombok（简化代码）等

### 前端

- [Vue 2.x](https://cn.vuejs.org/),[Vuex](https://vuex.vuejs.org/zh/),[Vue Router](https://router.vuejs.org/zh/)
- [Axios](https://github.com/axios/axios)
- [ant-design-vue](https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn/)
- [webpack](https://www.webpackjs.com/),[yarn](https://yarnpkg.com/zh-Hans/)
- eslint，[@vue/cli](https://cli.vuejs.org/zh/guide)

## 内置功能

```
├─系统管理
│  ├─用户管理
│  ├─角色管理
│  ├─菜单管理
│  ├─部门管理
│  ├─岗位管理
│  ├─字典管理
│  ├─参数管理
│  ├─通知公告
│  ├─日志管理
│  │  ├─操作日志
│  │  ├─登录日志
├─系统监控
│  ├─在线用户
│  ├─定时任务
│  ├─数据监控（单体）
│  ├─服务监控（单体）
│  ├─缓存监控（单体）
│  ├─Sentinel控制台（单体）
│  ├─Nacos控制台（单体）
│  ├─Admin控制台（单体）
├─系统工具
│  ├─系统接口
```

# 环境部署

## 准备工作

```text
JDK >= 1.8 (推荐1.8版本)
Mysql >= 5.7.0 (推荐5.7版本)
Redis >= 3.0
Maven >= 3.0
Node >= 12
nacos >= 1.1.0 (mapgis-cloud >= 3.0.0需要下载nacos >= 2.x.x版本)
sentinel >= 1.6.0
```

## 运行系统

### 后端运行

#### 后端项目导入

后端工程`mapgis-boot`，将其导入到`IDEA`，通过右侧 maven 窗口下载依赖

#### 后端项目启动（单体模式）

1、初始化数据库

创建数据库`mapgis-boot`并导入`local-script/sql`下数据脚本`mapgis-boot-xxxxxxxx.sql`（必须），`quartz.sql`（可选）

> 字符集：utf8mb4、排序规则：utf8mb4_general_ci

2、修改项目配置 (数据库、redis 等)

> 配置文件：`mapgis-boot/local-script/config/application.properties`，可参考`application.example.properties`中变量。

将上述配置文件设置到启动类`mapgis-boot/mapgis-server/src/main/java/com/zondy/mapgis/MapApplication`的环境变量中：

`Edit Configurations->Configuration->Environment->Environment Variables：`

> --spring.config.additional-location=file:local-script/config

3、运行 Redis

4、运行 `mapgis-boot/mapgis-server/src/main/java/com/zondy/mapgis/MapApplication.java`，出现如下图表示启动成功

```
__  __              _____ _____  _____    _____
|  \/  |            / ____|_   _|/ ____|  / ____|
| \  / | __ _ _ __ | |  __  | | | (___   | (___   ___ _ ____   _____ _ __
| |\/| |/ _` | '_ \| | |_ | | |  \___ \   \___ \ / _ \ '__\ \ / / _ \ '__|
| |  | | (_| | |_) | |__| |_| |_ ____) |  ____) |  __/ |   \ V /  __/ |
|_|  |_|\__,_| .__/ \_____|_____|_____/  |_____/ \___|_|    \_/ \___|_|
             | |
             |_|
```

> 提示
>
> 运行前需要先启动`redis`，后端运行成功后可以继续参考下面步骤部署 mapgis-manager 前端，然后通过前端地址来访问

#### 后端项目启动（微服务模式）

1、初始化数据库

创建数据库`mapgis-cloud`并导入 `cloud-script/sql`下数据脚本`mapgis-cloud-xxxxxxxx.sql`（必须），`quartz.sql`（可选）

创建数据库`mapgis-cloud-config`并导入数据脚本`mapgis-cloud-config-xxxxxxxx.sql`（必须）

> 字符集：utf8mb4、排序规则：utf8mb4_general_ci

2、配置`nacos`持久化，修改`conf/application.properties`文件，增加支持`mysql`数据源配置

```properties
# db mysql
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://localhost:3306/mapgis-cloud-config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user=root
db.password=123456
```

> 提示
>
> 配置文件`application.properties`是在下载的`nacos-server`包`conf`目录下
> 需要下载的`nacos-server`必须`>=2.x.x`版本
> 默认配置单机模式，`nacos`集群/多集群部署模式参考 ([Nacos 支持三种部署模式](https://nacos.io/zh-cn/docs/deployment.html))

3、运行 Nacos 修改项目配置 (数据库、redis 等)

访问[http://localhost:8848/nacos](http://localhost:8848/nacos)，修改组为 MAPGIS_GROUP 下的配置文件

> -dev 为开发环境下的配置，-prod 为生产环境下的配置

4、运行 Redis

5、运行各模块（启动没有先后顺序）

- MapGatewayApplication （网关模块 必须）
- MapAuthApplication （认证模块 必须）
- MapSystemApplication （系统模块 必须）
- MapMonitorApplication （监控中心 可选）
- MapJobApplication （定时任务 可选）
- MapFileApplication （文件服务 可选）

> 提示
>
> 运行前需要先启动`nacos`和`redis`，后端运行成功后可以继续参考下面步骤部署 mapgis-manager 前端，然后通过前端地址来访问

### 前端运行

```shell
# 进入项目目录
cd mapgis-manager

# 安装依赖
npm install

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npmmirror.com

# 本地开发 启动项目
npm run serve
```

打开浏览器，输入：[http://localhost:8000](http://localhost:8000/)，默认账户/密码 `admin/admin123`
若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功

> 提示
>
> 因为本项目是前后端完全分离的，所以需要前后端都单独启动好，才能进行访问
> 前端安装完 node 后，最好设置下淘宝的镜像源，不建议使用 cnpm（可能会出现奇怪的问题）

# 项目结构

## 后端结构

```text
com.zondy.mapgis
├── mapgis-common                                       // 通用模块
│   ├── mapgis-common-core                              // 核心模块
│   ├── mapgis-common-datascope                         // 权限范围
│   ├── mapgis-common-datasource                        // 多数据源
│   ├── mapgis-common-log                               // 日志记录
│       ├── mapgis-common-base-log                      // 基础日志记录
│       ├── mapgis-common-cloud-log                     // 微服务日志记录
│       ├── mapgis-common-local-log                     // 单体日志记录
│   └── mapgis-common-ratelimiter                       // 接口限流
│   └── mapgis-common-redis                             // 缓存服务
│   └── mapgis-common-repeatsubmit                      // 防重复提交
│   ├── mapgis-common-security                          // 安全模块
│       ├── mapgis-common-base-security                 // 基础安全模块
│       ├── mapgis-common-cloud-security                // 微服务安全模块
│       ├── mapgis-common-local-security                // 单体安全模块
│   └── mapgis-common-swagger                           // 系统接口
├── mapgis-module-auth                                  // 授权模块
│   ├── mapgis-module-auth-api                          // 授权API
│       ├── mapgis-module-auth-base-api                 // 基础授权API
│       ├── mapgis-module-auth-cloud-api                // 微服务授权API
│       ├── mapgis-module-auth-local-api                // 单体授权API
│   ├── mapgis-module-auth-biz                          // 授权业务
│   ├── mapgis-module-auth-server                       // 授权服务器 [9200]
├── mapgis-module-system                                // 系统模块
│   ├── mapgis-module-system-api                        // 系统API
│       ├── mapgis-module-system-base-api               // 基础系统API
│       ├── mapgis-module-system-cloud-api              // 微服务系统API
│       ├── mapgis-module-system-local-api              // 单体系统权API
│   ├── mapgis-module-system-biz                        // 系统业务
│   ├── mapgis-module-system-server                     // 系统服务器 [9201]
├── mapgis-module-file                                  // 文件模块
│   ├── mapgis-module-file-api                          // 文件API
│       ├── mapgis-module-file-base-api                 // 基础文件API
│       ├── mapgis-module-file-cloud-api                // 微服务文件API
│       ├── mapgis-module-file-local-api                // 单体文件API
│   ├── mapgis-module-file-biz                          // 文件业务
│   ├── mapgis-module-file-server                       // 文件服务器 [9300]
├── mapgis-module-job                                   // 定时任务模块
│   ├── mapgis-module-job-biz                           // 定时任务业务
│   ├── mapgis-module-job-server                        // 定时任务服务器 [9203]
├── mapgis-server                                       // 单体服务器 [8080]
├── mapgis-cloud-module                                 // 微服务模块
│   ├── mapgis-cloud-module-gateway                     // 网关模块 [8080]
│   ├── mapgis-cloud-module-monitor                     // 监控中心 [9100]
├── pom.xml                                             // 公共依赖
```

## 前端结构

```text
├── build                      // 构建相关
├── bin                        // 执行脚本
├── public                     // 公共文件
│   ├── favicon.ico            // favicon图标
│   ├── index.html             // html模板
├── src                        // 源代码
│   ├── api                    // 所有请求
│   ├── assets                 // 主题 字体等静态资源
│   ├── components             // 全局公用组件
│   ├── directive              // 全局指令
│   ├── layout                 // 布局
│   ├── router                 // 路由
│   ├── store                  // 全局 store管理
│   ├── utils                  // 全局公用方法
│   ├── views                  // view
│   ├── App.vue                // 入口页面
│   ├── main.js                // 入口 加载组件 初始化等
│   ├── permission.js          // 权限管理
│   ├── settings.js            // 系统配置
├── .editorconfig              // 编码格式
├── .env.development           // 开发环境配置
├── .env.production            // 生产环境配置
├── .env.staging               // 测试环境配置
├── .eslintignore              // 忽略语法检查
├── .eslintrc.js               // eslint 配置项
├── .gitignore                 // git 忽略项
├── babel.config.js            // babel.config.js
├── package.json               // package.json
├── vue.config.js              // vue.config.js
```
