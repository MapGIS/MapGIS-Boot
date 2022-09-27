# 前端手册

## 全局配置文件
前端build完也可以动态修改
> 核心思想：通过index.html引入静态资源文件static/config.js，main.js会在第一时间通过src/config/index.js提供对env文件配置的动态覆盖，所以可以在运行时直接修改

**static/config.js**
```js
/**
 * 存放配置常量
 */
window._CONFIG = {
  // 接口父路径(当值不为空时会覆盖env配置)
  VUE_APP_API_BASE_URL: ''
}
```

**src/config/index.js**
```js
/** init domain config */
import Vue from 'vue'
// 设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = window._CONFIG.VUE_APP_API_BASE_URL
  ? window._CONFIG.VUE_APP_API_BASE_URL
  : process.env.VUE_APP_API_BASE_URL
window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
```

## 微前端配置
微前端相关内容请参考[微应用路由管理](/zh/guide/document/backend.html#微应用路由管理)
### 主应用路由
目录主应用路由模式为`history`，路由base为`/xxx/manager/`，可根据需要进行修改。

### 主应用和微应用通信
默认主应用传递了`publicPath、token、store、router`数据给微应用，可根据需要进行修改，详见[state.js](https://github.com/MapGIS/MapGIS-Boot/blob/master/mapgis-manager/src/qiankun/state.js)
```js
export function getProps() {
  return {
    data: {
      publicPath: process.env.BASE_URL,
      token: storage.get(ACCESS_TOKEN),
      store,
      router
    }
  }
}

```
另外，可还可以在主应用中定义全局状态，微应用通过 props 获取通信方法，二者进行通信，详见[initGlobalState](https://qiankun.umijs.org/zh/api#initglobalstatestate)。