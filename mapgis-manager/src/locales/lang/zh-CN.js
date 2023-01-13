import antd from 'ant-design-vue/es/locale-provider/zh_CN'
import momentCN from 'moment/locale/zh-cn'
import global from './zh-CN/global'

import setting from './zh-CN/setting'
import account from './zh-CN/account'
import user from './zh-CN/user'
import config from './zh-CN/config'
import monitor from './zh-CN/monitor'
import log from './zh-CN/log'
import security from './zh-CN/security'
import schedule from './zh-CN/schedule'
import msg from './zh-CN/msg'
import dev from './zh-CN/dev'

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
  ...user,
  ...account,
  ...config,
  ...monitor,
  ...log,
  ...security,
  ...schedule,
  ...msg,
  ...dev
}
