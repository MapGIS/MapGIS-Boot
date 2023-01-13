import antd from 'ant-design-vue/es/locale-provider/zh_CN'
import momentCN from 'moment/locale/zh-cn'
import global from './zh-CN/global'

import setting from './zh-CN/setting'
import config from './zh-CN/config'
import log from './zh-CN/log'

const components = {
  antLocale: antd,
  momentName: 'zh-cn',
  momentLocale: momentCN
}

export default {
  message: '-',

  ...components,
  ...global,
  ...setting,
  ...config,
  ...log
}
