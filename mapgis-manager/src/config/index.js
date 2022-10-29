/** init domain config */
import Vue from 'vue'
// 产品名称
window._CONFIG['productName'] = window._CONFIG.VUE_APP_PRODUCT_NAME
// 管理类接口前缀
window._CONFIG['apiPathManagerPrefix'] = `/${window._CONFIG.VUE_APP_PRODUCT_NAME}/rest/manager`
// 服务类接口前缀
window._CONFIG['apiPathServicesPrefix'] = `/${window._CONFIG.VUE_APP_PRODUCT_NAME}/rest/services`
// 设置全局API_BASE_URL
Vue.prototype.API_BASE_URL = window._CONFIG.VUE_APP_API_BASE_URL
  ? window._CONFIG.VUE_APP_API_BASE_URL
  : process.env.VUE_APP_API_BASE_URL
window._CONFIG['domianURL'] = Vue.prototype.API_BASE_URL
