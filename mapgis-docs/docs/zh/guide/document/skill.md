# 实战技巧

## 新建子模块

### 新建单体和微服务共用模块

建议项目结构如下：

```text
├── mapgis-module-xxx                                // 模块xxx
│   ├── mapgis-module-xxx-api                        // 模块API
│       ├── mapgis-module-xxx-base-api               // 基础API
│       ├── mapgis-module-xxx-cloud-api              // 微服务API
│       ├── mapgis-module-xxx-local-api              // 单体API
│   ├── mapgis-module-xxx-biz                        // 模块业务
│   ├── mapgis-module-xxx-server                     // 模块服务器
```

在 mapgis-boot 的根 `pom.xml` 中针对 local 和 cloud 两个 profile 编写
`mapgis.module.xxx.api.artifact`

```xml
<profiles>
  <!-- 单体模式 -->
  <profile>
    <id>local</id>
    <properties>
      <mapgis.module.xxx.api.artifact>mapgis-module-xxx-local-api</mapgis.module.xxx.api.artifact>
    </properties>
  </profile>
  <!-- 微服务模式 -->
  <profile>
    <id>local</id>
    <properties>
      <mapgis.module.xxx.api.artifact>mapgis-module-xxx-cloud-api</mapgis.module.xxx.api.artifact>
    </properties>
  </profile>
</profiles>
```

模块之间的依赖关系如下：

- `mapgis-module-xxx-local-api` 和 `mapgis-module-xxx-local-api` 依赖 `mapgis-module-xxx-base-api`

- `mapgis-module-xxx-biz`依赖`${mapgis.module.xxx.api.artifact}`

```
<dependency>
    <groupId>com.zondy.mapgis</groupId>
    <artifactId>${mapgis.module.xxx.api.artifact}</artifactId>
</dependency>
```

- `mapgis-module-xxx-server`依赖`mapgis-module-xxx-biz`

- `mapgis-server`依赖`mapgis-module-xxx-biz`

> `mapgis-module-xxx-server`为微服务版时需要独立运行的服务，在单体版下运行的是`mapgis-server`

### 新建微服务特有模块

可直接建立在 mapgis-cloud-module 模块下，建议模块名如下：

```
mapgis-cloud-module-xxx
```
