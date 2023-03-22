// with polyfills
import 'core-js/stable'
import 'regenerator-runtime/runtime'

import './config'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './theme/index.less'
import permission from './directive/permission'
import style from './directive/style'
import store from './store/'
import i18n from './locales'
import { VueAxios } from './utils/request'
import ProLayout, { PageHeaderWrapper } from '@/components/ProLayout'
import FooterToolBar from '@/components/FooterToolbar'
import FileUpload from '@/components/FileUpload'
import { TableSetting } from '@/components'
import PopDialog from '@/components/PopDialog'

import bootstrap from './core/bootstrap'
import './core/lazy_use' // use lazy load components
import './permission' // permission control
import './utils/filter' // global filter
import './global.less' // global style
import { getDicts } from '@/api/system/dict/data'
import { getConfigValueByKey } from '@/api/system/config'
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  handleTree,
  tableSorter
} from '@/utils/common'
import { download } from '@/utils/request'

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigValueByKey = getConfigValueByKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.prototype.tableSorter = tableSorter
Vue.config.productionTip = false

// mount axios to `Vue.$http` and `this.$http`
Vue.use(VueAxios)
// use pro-layout components
Vue.component('pro-layout', ProLayout)
Vue.component('page-container', PageHeaderWrapper)
Vue.component('page-header-wrapper', PageHeaderWrapper)
Vue.component('footer-tool-bar', FooterToolBar)
Vue.component('file-upload', FileUpload)
Vue.component('table-setting', TableSetting)
Vue.component('pop-dialog', PopDialog)

Vue.use(permission)
Vue.use(style)

new Vue({
  router,
  store,
  i18n,
  // init localstorage, vuex
  created: bootstrap,
  render: h => h(App)
}).$mount('#main-app')
