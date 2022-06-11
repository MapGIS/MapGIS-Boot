# 前端手册

## 基础功能

### 全局配置文件
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