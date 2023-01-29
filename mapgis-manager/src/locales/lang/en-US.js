import antdEnUS from 'ant-design-vue/es/locale-provider/en_US'
import momentEU from 'moment/locale/eu'
import global from './en-US/global'
import compos from './en-US/components'

import setting from './en-US/setting'
import account from './en-US/account'
import user from './en-US/user'
import config from './en-US/config'
import monitor from './en-US/monitor'
import log from './en-US/log'
import security from './en-US/security'
import schedule from './en-US/schedule'
import msg from './en-US/msg'
import dev from './en-US/dev'

const components = {
  antLocale: antdEnUS,
  momentName: 'eu',
  momentLocale: momentEU
}

export default {
  message: '-',

  ...components,
  ...global,
  ...compos,
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
