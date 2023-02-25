import message from 'ant-design-vue/es/message'
import themeUtil from '@/utils/themeUtil'
import i18n from '@/locales'

// let lessNodesAppended
function getColorList() {
  return [
    {
      key: i18n.t('app.setting.themecolor.dust'),
      color: '#f5222d'
    },
    {
      key: i18n.t('app.setting.themecolor.volcano'),
      color: '#fa541c'
    },
    {
      key: i18n.t('app.setting.themecolor.sunset'),
      color: '#faad14'
    },
    {
      key: i18n.t('app.setting.themecolor.cyan'),
      color: '#13c2c2'
    },
    {
      key: i18n.t('app.setting.themecolor.green'),
      color: '#52c41a'
    },
    {
      key: i18n.t('app.setting.themecolor.daybreak'),
      color: '#1890ff'
    },
    {
      key: i18n.t('app.setting.themecolor.geekblue'),
      color: '#2f54eb'
    },
    {
      key: i18n.t('app.setting.themecolor.purple'),
      color: '#722ed1'
    }
  ]
}

const updateTheme = (theme, color) => {
  const hideMessage = message.loading(i18n.t('app.setting.theme.updating'), 0)
  themeUtil.changeThemeColor(color, theme).then(r => {
    hideMessage()
  })
}

const updateColorWeak = colorWeak => {
  // document.body.className = colorWeak ? 'colorWeak' : '';
  const app = document.body.querySelector('#app')
  colorWeak ? app.classList.add('colorWeak') : app.classList.remove('colorWeak')
}

export { updateTheme, getColorList, updateColorWeak }
