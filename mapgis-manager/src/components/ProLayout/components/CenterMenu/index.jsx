import './index.less'

import BaseMenu from '../RouteMenu/BaseMenu'
import { SiderMenuProps } from '../SiderMenu/SiderMenu'

const CenterMenuProps = {
  ...SiderMenuProps
}

const CenterMenu = {
  name: 'CenterMenu',
  props: CenterMenuProps,
  computed: {
    menuTheme() {
      return 'light'
    },
    currentMenus() {
      const pathName = this.$route.matched[1].name
      let secondMenus = []
      this.menus.forEach(item => {
        if (pathName) {
          !item.hidden && item.name === pathName && (secondMenus = item.children)
        } else {
          !item.hidden && (secondMenus = item.children)
        }
      })

      return secondMenus
    }
  },
  render(h) {
    const { currentMenus } = this
    const baseCls = 'ant-pro'

    return currentMenus ? (
      <div class={`${baseCls}-mix-menu-center`}>
        <BaseMenu menus={currentMenus} mode="horizontal" theme={this.menuTheme} i18nRender={this.i18nRender} />
      </div>
    ) : null
  }
}

export default CenterMenu
