import PropTypes from 'ant-design-vue/es/_util/vue-types'

import 'ant-design-vue/es/menu/style'
import Menu from 'ant-design-vue/es/menu'
import 'ant-design-vue/es/icon/style'
import Icon from 'ant-design-vue/es/icon'

const { Item: MenuItem, SubMenu } = Menu

export const RouteMenuProps = {
  menus: PropTypes.array,
  theme: PropTypes.string.def('dark'),
  mode: PropTypes.string.def('inline'),
  collapsed: PropTypes.bool.def(false),
  i18nRender: PropTypes.oneOfType([PropTypes.func, PropTypes.bool]).def(false),
  isFirstMenus: PropTypes.bool.def(false)
}

const httpReg = /(http|https|ftp):\/\/([\w.]+\/?)\S*/

const renderMenu = (h, item, i18nRender, mode) => {
  if (item && !item.hidden) {
    const bool = item.children && !item.hideChildrenInMenu
    return bool ? renderSubMenu(h, item, i18nRender, mode) : renderMenuItem(h, item, i18nRender, mode)
  }
  return null
}

const renderSubMenu = (h, item, i18nRender) => {
  return (
    <SubMenu
      key={item.name}
      title={
        <span>
          {renderIcon(h, item.meta.icon)}
          <span>{renderTitle(h, item.meta.title, i18nRender)}</span>
        </span>
      }
    >
      {!item.hideChildrenInMenu && item.children.map(cd => renderMenu(h, cd, i18nRender))}
    </SubMenu>
  )
}

const renderMenuItem = (h, item, i18nRender, mode) => {
  const meta = Object.assign({}, item.meta)
  const target = meta.target || null
  const hasRemoteUrl = httpReg.test(item.path)
  const CustomTag = (target && 'a') || 'router-link'
  // 微应用的路由是以'/*'结尾，但这里菜单的链接需要动态去掉'*'
  const menuPath = item.path?.endsWith('/*') ? item.path.substring(0, item.path.length - 1) : item.path
  const props = { to: { path: menuPath } }
  const attrs = hasRemoteUrl || target ? { href: item.path, target: target } : {}
  if (item.children && item.hideChildrenInMenu) {
    // 把有子菜单的 并且 父菜单是要隐藏子菜单的
    // 都给子菜单增加一个 hidden 属性
    // 用来给刷新页面时， selectedKeys 做控制用
    item.children.forEach(cd => {
      cd.meta = Object.assign(cd.meta || {}, { hidden: true })
    })
  }
  return (
    <MenuItem key={item.name}>
      <CustomTag {...{ props, attrs }}>
        {renderIcon(h, meta.icon, mode)}
        {renderTitle(h, meta.title, i18nRender)}
      </CustomTag>
    </MenuItem>
  )
}

const renderIcon = (h, icon, mode) => {
  if (icon === undefined || icon === 'none' || icon === null) {
    return null
  }
  if (icon === '' || icon === '#') {
    if (mode !== undefined && mode === 'horizontal') {
      return null
    }
  }

  const props = {}
  typeof icon === 'object' ? (props.component = icon) : (props.type = icon)
  return <Icon {...{ props }} />
}

const renderTitle = (h, title, i18nRender) => {
  return <span>{(i18nRender && i18nRender(title)) || title}</span>
}

const RouteMenu = {
  name: 'RouteMenu',
  props: RouteMenuProps,
  data() {
    return {
      openKeys: [],
      selectedKeys: [],
      cachedOpenKeys: [],
      cachedSelectedKeys: []
    }
  },
  render(h) {
    const { mode, menuTheme, menus, i18nRender } = this
    const handleOpenChange = openKeys => {
      // 在水平模式下时，不再执行后续
      if (mode === 'horizontal') {
        this.openKeys = openKeys
        return
      }
      const latestOpenKey = openKeys.find(key => !this.openKeys.includes(key))
      if (!this.rootSubmenuKeys.includes(latestOpenKey)) {
        this.openKeys = openKeys
      } else {
        this.openKeys = latestOpenKey ? [latestOpenKey] : []
      }
    }

    const dynamicProps = {
      props: {
        mode,
        theme: menuTheme,
        openKeys: this.openKeys,
        selectedKeys: this.selectedKeys
      },
      on: {
        select: menu => {
          this.$emit('select', menu)
          if (!httpReg.test(menu.key)) {
            this.selectedKeys = menu.selectedKeys
          }
        },
        openChange: handleOpenChange
      }
    }

    const menuItems = menus.map(item => {
      if (item.hidden) {
        return null
      }
      return renderMenu(h, item, i18nRender, mode)
    })
    return <Menu {...dynamicProps}>{menuItems}</Menu>
  },
  methods: {
    updateMenu() {
      const routes = this.$route.matched.concat()
      const { hidden } = this.$route.meta
      if (this.isFirstMenus) {
        this.selectedKeys = [routes[1].name]
      } else {
        if (routes.length >= 3 && hidden) {
          routes.pop()
          this.selectedKeys = [routes[routes.length - 1].name]
        } else {
          this.selectedKeys = [routes.pop().name]
        }
      }
      const openKeys = []
      if (this.mode === 'inline') {
        routes.forEach(item => {
          item.name && openKeys.push(item.name)
        })
      }

      this.collapsed ? (this.cachedOpenKeys = openKeys) : (this.openKeys = openKeys)
    }
  },
  computed: {
    rootSubmenuKeys: vm => {
      const keys = []
      vm.menus.forEach(item => keys.push(item.name))
      return keys
    },
    menuTheme() {
      return this.theme === 'light' ? this.theme : 'dark'
    }
  },
  created() {
    this.$watch('$route', () => {
      this.updateMenu()
    })
    this.$watch('collapsed', val => {
      if (val) {
        this.cachedOpenKeys = this.openKeys.concat()
        this.openKeys = []
      } else {
        this.openKeys = this.cachedOpenKeys
      }
    })
  },
  mounted() {
    this.updateMenu()
  }
}

export default RouteMenu
