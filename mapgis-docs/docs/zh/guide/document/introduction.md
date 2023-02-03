# 项目介绍

MapGIS Boot 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring Cloud & Alibaba、Ant Design Vue），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、代码生成、在线定时任务、路由监控等。

:::tip
本项目默认你有一定的 SpringBoot 基础和 Vue 使用经验,如果你对这些还不熟悉，我们建议你先查阅相关文档[Spring Boot](https://spring.io/projects/spring-boot)、[Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/README-zh.md)、[Vue](https://cn.vuejs.org/)、[Ant Design Vue](https://antdv.com/)

因本项目框架基于[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 和 [RuoYi-Cloud](https://gitee.com/y_project/RuoYi-Cloud)开发，在此特别鸣谢: [@y_project](https://gitee.com/y_project)。
:::

## 主要特性

- 支持单体和微服务自由切换
- 支持按钮及数据权限，可自定义部门数据权限
- 支持菜单动态路由
- 支持多主题动态切换
- 完善的 XSS 防范及脚本过滤，彻底杜绝 XSS 攻击
- Maven 多项目依赖，模块及插件分项目，尽量松耦合，方便模块升级、增减模块
- 完善的日志记录体系简单注解即可实现
- 强大的一键生成功能（包括控制器、模型、视图、菜单等）
- 支持后端微服务路由和前端微应用路由配置
- 支持第三方用户系统接入（包括CAS、OAuth2、LDAP等）

## 技术选型

### 开发环境

- 语言：Java 8+ (小于17)

- IDE(JAVA)： IDEA (必须安装lombok插件 )

- IDE(前端)： VSCode

- 依赖管理：Maven

- 缓存：Redis

- 数据库脚本：MySQL5.7+

### 后端

- 基础框架：Spring Boot 2.7.7

- 微服务框架： Spring Cloud Alibaba 2021.0.4.0

- 持久层框架：Mybatis-Plus 3.5.3

- 安全框架：Spring Security，Jwt

- 微服务技术栈：Spring Cloud Alibaba、Nacos、Gateway、Sentinel

- 数据库连接池：阿里巴巴 Druid 1.2.15

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
├─总览
├─监控管理
│  ├─在线用户
│  ├─服务器监控
│  ├─服务器性能监控
├─日志管理
│  ├─登录日志
│  ├─操作日志
│  ├─系统日志
│  ├─服务访问日志
│  ├─日志配置
├─安全管理
│  ├─安全配置
│  ├─用户管理
│  ├─用户组管理
│  ├─角色管理
│  ├─菜单管理
│  ├─部门管理
│  ├─岗位管理
│  ├─第三方登录配置
│  ├─CAS登录配置
│  ├─LDAP登录配置
├─计划任务
│  ├─定时任务
├─消息中心
│  ├─通知公告
├─开发扩展
│  ├─接口文档
│  ├─数据字典
│  ├─代码生成
│  ├─微应用路由配置
│  ├─微服务路由配置（微服务）
├─系统配置
│  ├─基本配置
│  ├─主题配置
```

## 更多

更多介绍请参考[项目说明](https://github.com/MapGIS/MapGIS-Boot)
