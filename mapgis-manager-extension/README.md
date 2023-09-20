# 微应用

本项目是一个接入 qiankun 主应用的微应用模板，任何人都可以基于它进行快速扩展

> https://qiankun.umijs.org/zh/guide

## 一、准备工作

### 1.1 取名

给微应用起一个专用的名字，作为微应用路由的一部分，会展示到浏览器地址栏中，如模板中的**extension**，后面所有涉及到 extension 的地方都需要采用专有名字

### 1.2 替换名称

#### 1.2.1 替换项目名

`package.json`的 name，将其中的 extension 进行替换

```json
{
  "name": "mapgis-manager-extension"
  // ...
}
```

#### 1.2.2 替换路由基地址

`src/router/index.js`的路由 base，将其中的 extension 进行替换

```javascript
base: window.__POWERED_BY_QIANKUN__ ? '/xxx/manager/extension/' : '/',
```

> 注意：前面的**/xxx/manager/**必须与主应用路由基地址保持一致！！！

#### 1.2.3 替换静态资源访问路径

`.env`的`VUE_APP_CONTEXT_PATH`，将其中的 extension 进行替换

```properties
VUE_APP_CONTEXT_PATH=/xxx/static/manager-apps/extension
```

## 二、微应用开发和运行

### 2.1 微应用开发

微应用本身具备完全自主权，可以独立开发，所以你完全可以像以往一样采用任何你喜欢的技术来实现你想要的效果，本模板基于 Ant Design Vue 提供了主题切换功能，建议开发者也采用该 UI 库进行开发

### 2.2 独立运行

在集成到主应用之前，开发者可以通过命令`yarn serve`启动项目

### 2.3 集中到主应用运行

#### 2.3.1 新增微应用路由

在《开发扩展/微应用路由配置》中，新增微应用

| 微应用名称 | 微应用入口       | 微应用路由              |
| ---------- | ---------------- | ----------------------- |
| extension  | //localhost:9000 | /xxx/manager/extension/ |

> 微应用名称：可以随意
>
> 微应用入口：确保端口正确，不需要添加前面的**http:**，直接以**//**开头即可，采用完整地址也没问题
>
> 微应用路由：需要与路由基地址保持一致，最后的**/**，可加可不加

#### 2.3.2 配置微应用菜单

##### 2.3.2.1 配置带有子路由页面的菜单

模板提供了 2 个路由页面，一个是主页面/，一个是子页面/about，当我们想在主应用的一个菜单页面下展示所有的路由页面，可按如下进行配置：

| 菜单类型 | 菜单名称 | 路由地址  | 组件路径       |
| -------- | -------- | --------- | -------------- |
| 菜单     | 微应用   | extension | 使用微应用组件 |

> 因微应用的路由基地址必须以主应用的路由基地址作为前缀，所以在这里只需要配置 extension，注意，这里必须将这个菜单放到顶级，不然经过拼接后地址会不正确

配置完成后刷新主页面，可通过点击《微应用》菜单访问，在页面内部，点击《About》可以在微应用内进行切换，同时，主应用的浏览器地址也会跟随变化

##### 2.3.2.2 配置单独路由页面的菜单

这里以 about 页面为例，因访问它的完整地址是/xxx/manager/extension/about，所以我们可以在主应用中先配置一个目录，路由为 extension，再在这个目录内配置一个菜单并设置它的路由为 about，实现单独展示微应用某个路由页面的功能

| 菜单类型 | 菜单名称 | 路由地址  | 组件路径       |
| -------- | -------- | --------- | -------------- |
| 目录     | 微应用   | extension | /              |
| 菜单     | 关于     | about     | 使用微应用组件 |

配置完成后刷新主页面，可通过点击《微应用/关于》菜单访问，直接就可以看到 about 页面的内容，同时，主应用的浏览器地址也会跟随变化

## 三、微应用打包部署和运行

### 3.1 微应用打包

运行 yarn build 完成微应用打包

### 3.2 微应用部署

按照`VUE_APP_CONTEXT_PATH=/xxx/static/manager-apps/extension`指向的路径，将`dist`里面的内容拷贝到主应用包的`resource/static/manager-apps/extension`下

### 3.3 微应用运行

配置运行同[2.3](# 2.3 集中到主应用运行)，只需要在配置微应用路由的时候，修改微应用路由为`/xxx/static/manager-apps/extension/index.html`，如下：

| 微应用名称 | 微应用入口                                    | 微应用路由              |
| ---------- | --------------------------------------------- | ----------------------- |
| extension  | /xxx/static/manager-apps/extension/index.html | /xxx/manager/extension/ |