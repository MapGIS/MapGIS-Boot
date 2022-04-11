import message from 'ant-design-vue/es/message'
import themeUtil from '@/utils/themeUtil'

// let lessNodesAppended
const colorList = [
  {
    key: '薄暮',
    color: '#f5222d'
  },
  {
    key: '火山',
    color: '#fa541c'
  },
  {
    key: '日暮',
    color: '#faad14'
  },
  {
    key: '明青',
    color: '#13c2c2'
  },
  {
    key: '极光绿',
    color: '#52c41a'
  },
  {
    key: '拂晓蓝（默认）',
    color: '#1890ff'
  },
  {
    key: '极客蓝',
    color: '#2f54eb'
  },
  {
    key: '酱紫',
    color: '#722ed1'
  }
]

const updateTheme = (theme, color) => {
  const hideMessage = message.loading('正在切换主题！', 0)
  themeUtil.changeThemeColor(color, theme).then(r => {
    hideMessage()
  })
}

const updateColorWeak = colorWeak => {
  // document.body.className = colorWeak ? 'colorWeak' : '';
  const app = document.body.querySelector('#app')
  colorWeak ? app.classList.add('colorWeak') : app.classList.remove('colorWeak')
}

export { updateTheme, colorList, updateColorWeak }
