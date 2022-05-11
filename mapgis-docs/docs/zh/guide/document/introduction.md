# 项目介绍

MapGIS Boot 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring Cloud & Alibaba、Ant Design Vue），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、在线定时任务等。

:::tip
本项目默认你有一定的 SpringBoot 基础和 Vue 使用经验,如果你对这些还不熟悉，我们建议你先查阅相关文档[Spring Boot](https://spring.io/projects/spring-boot)、[Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/README-zh.md)、[Vue](https://cn.vuejs.org/)、[Ant Design Vue](https://antdv.com/)

因本项目框架基于[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 和 [RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud)开发，在此特别鸣谢: [@y_project](https://gitee.com/y_project)。
:::

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
│  ├─Sentinel控制台（微服务）
│  ├─Nacos控制台（微服务）
│  ├─Admin控制台（微服务）
├─系统工具
│  ├─系统接口
```

## 更多

更多介绍请参考[项目说明](http://192.168.200.88/webgis/server/mapgis-boot/tree/master)
