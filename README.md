# MapGIS Boot 开发平台（前后端分离版本）

## 项目介绍：

MapGIS Boot 前后端分离架构 Spring Boot 2.x，Spring Cloud，Ant Design Vue，Mybatis，JJWT，支持微服务。

## 适用项目

MapGIS Boot 开发平台，可以应用在任何 J2EE 项目的开发中。

## 为什么选择 MapGIS Boot?

- 1.采用最新主流前后分离框架（Spring Boot + Mybatis + Antd），容易上手
- 2.支持微服务 SpringCloud Alibaba(Nacos、Gateway、Sentinel)，支持单体和微服务自由切换
- 3.封装完善的用户、角色、菜单、组织机构、数据字典、在线定时任务等基础功能，支持访问授权、按钮权限、数据权限等功能
- 4.常用共通封装，各种工具类(定时任务,短信接口,邮件发送,Excel 导入导出等),基本满足 80%项目需求
- 5.采用前后分离技术，页面 UI 风格精美
- 6.采用 maven 分模块开发方式
- 7.支持菜单动态路由
- 8.权限控制采用 RBAC（Role-Based Access Control，基于角色的访问控制）

## 技术架构：

#### 开发环境

- 语言：Java 8

- IDE(JAVA)： IDEA

- IDE(前端)： VSCode

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7

#### 后端

- 基础框架：Spring Boot 2.6.3.RELEASE

- 微服务框架： Spring Cloud Alibaba 2021.1.RELEASE

- 持久层框架：Mybatis

- 安全框架：Spring Security，JJWT

- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel

- 数据库连接池：阿里巴巴 Druid

- 缓存框架：Redis

- 日志打印：Logback

- 其他：Poi，Swagge UI，Quartz, Lombok（简化代码）等。

#### 前端

- [Vue 2.x](https://cn.vuejs.org/),[Vuex](https://vuex.vuejs.org/zh/),[Vue Router](https://router.vuejs.org/zh/)
- [Axios](https://github.com/axios/axios)
- [ant-design-vue](https://vuecomponent.github.io/ant-design-vue/docs/vue/introduce-cn/)
- [webpack](https://www.webpackjs.com/),[yarn](https://yarnpkg.com/zh-Hans/)
- eslint，[@vue/cli](https://cli.vuejs.org/zh/guide)

### 功能模块

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
```

## 微服务整体解决方案(2.4+版本)

1、服务注册和发现 Nacos √

2、统一配置中心 Nacos √

3、路由网关 gateway √

4、分布式 http feign √

5、熔断和降级 Sentinel √

6、分布式文件 Minio √

7、统一权限控制 JWT + SpringSecurity √

8、服务监控 SpringBootAdmin√

9、分布式事务 Seata

10、支持 docker-compose

11、路由限流 √

## 后台开发环境和依赖

- java
- maven
- jdk8
- mysql
- redis

## 前端开发环境和依赖

- node
- yarn
- webpack
- eslint
- @vue/cli
- [ant-design-vue](https://github.com/vueComponent/ant-design-vue) - Ant Design Of Vue 实现

## 项目运行

1. 安装 node.js
2. 切换到 ant-design-vue-mapgis 文件夹下

```
# 安装yarn
npm install -g yarn

# 下载依赖
yarn install

# 启动
yarn run serve

# 编译项目
yarn run build
```

## 其他说明

- 项目使用的 [vue-cli3](https://cli.vuejs.org/guide/), 请更新您的 cli

- 修改 Ant Design 配色，在文件 `vue.config.js` 中，其他 less 变量覆盖参考 [ant design](https://ant.design/docs/react/customize-theme-cn) 官方说明

```ecmascript 6
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */

          'primary-color': '#F5222D',
          'link-color': '#F5222D',
          'border-radius-base': '4px',
        },
        javascriptEnabled: true,
      }
    }
  }
```

## 附属文档

- [Ant Design Vue](https://www.antdv.com/docs/vue/introduce-cn)

- [Vue](https://cn.vuejs.org/v2/guide)

- 其他待补充...
