# 实战技巧

## 项目扩展

### 如何扩展

#### 克隆本项目，并移除`mapgis-docs`目录
> `mapgis-docs`为`mapgis-boot`的开发手册，用于扩展项目参考，建议在扩展项目中移除
#### 修改产品标识

全项目搜索 `xxx`，替换成产品小写标识，如：`igs、datastore、igs-x、igs-s、workspace、psmap、portal、manager`

#### 推送到新的项目
推送现有的Git仓库到一个新的项目仓库
```
cd existing_repo
git remote rename origin old-origin
git remote add origin git@github.com:xyz/xyz.git
git push -u origin --all
git push -u origin --tags
```

### 如何升级
`MapGIS Boot`属于平台级产品，每次升级改动内容较多，目前做不到平滑升级，这里给用户的升级建议是这样的：
- 通过cherry-pick进行仓库之间的同步，参考：[五、转移到另一个代码库](https://www.ruanyifeng.com/blog/2020/04/git-cherry-pick.html)
  >  对于sql脚本、docker脚本的同步需要仔细对比代码
```shell
git remote add boot git@github.com:MapGIS/MapGIS-Boot.git
git fetch boot
git log boot/master
git cherry-pick <commitHash>
```
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

- `mapgis-module-xxx-local-api` 和 `mapgis-module-xxx-cloud-api` 依赖 `mapgis-module-xxx-base-api`

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
