import './index.less'

import PropTypes from 'ant-design-vue/es/_util/vue-types'
import 'ant-design-vue/es/layout/style'
import Layout from 'ant-design-vue/es/layout'
import { isFun } from '../../utils/util'
import BaseMenu from '../RouteMenu'

const { Sider } = Layout

export const SiderMenuProps = {
  i18nRender: PropTypes.oneOfType([PropTypes.func, PropTypes.bool]).def(false),
  mode: PropTypes.string.def('inline'),
  theme: PropTypes.string.def('dark'),
  contentWidth: PropTypes.oneOf(['Fluid', 'Fixed']).def('Fluid'),
  collapsible: PropTypes.bool,
  collapsed: PropTypes.bool,
  handleCollapse: PropTypes.func,
  menus: PropTypes.array,
  siderWidth: PropTypes.number.def(256),
  isMobile: PropTypes.bool,
  layout: PropTypes.string.def('inline'),
  fixSiderbar: PropTypes.bool,
  logo: PropTypes.any,
  title: PropTypes.string.def(''),
  // render function or vnode
  menuHeaderRender: PropTypes.oneOfType([PropTypes.func, PropTypes.array, PropTypes.object, PropTypes.bool]),
  menuRender: PropTypes.oneOfType([PropTypes.func, PropTypes.array, PropTypes.object, PropTypes.bool])
}

export const defaultRenderLogo = (h, logo) => {
  if (typeof logo === 'string') {
    return <img src={logo} alt="logo" />
  }
  if (typeof logo === 'function') {
    return logo()
  }
  return h(logo)
}

export const defaultRenderLogoAntTitle = (h, props) => {
  const { logo = 'https://gw.alipayobjects.com/zos/antfincdn/PmY%24TNNDBI/logo.svg', title, menuHeaderRender } = props

  if (menuHeaderRender === false) {
    return null
  }
  const logoDom = defaultRenderLogo(h, logo)
  const titleDom = <h1>{title}</h1>

  if (menuHeaderRender) {
    return (
      (isFun(menuHeaderRender) && menuHeaderRender(h, logoDom, props.collapsed ? null : titleDom, props)) ||
      menuHeaderRender
    )
  }
  return (
    <span>
      {logoDom}
      {titleDom}
    </span>
  )
}

const SiderMenu = {
  name: 'SiderMenu',
  model: {
    prop: 'collapsed',
    event: 'collapse'
  },
  props: SiderMenuProps,
  computed: {
    menuTheme() {
      if (this.layout === 'mixmenu' && this.theme === 'dark') {
        return 'light'
      }

      return this.theme === 'light' ? this.theme : 'dark'
    },
    currentCollapsed() {
      if (this.layout === 'mixmenu') {
        return false
      }
      return this.collapsed
    },
    currentMenus() {
      if (this.layout !== 'mixmenu') {
        return this.menus
      }

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
    const {
      collapsible,
      collapsed,
      siderWidth,
      fixSiderbar,
      mode,
      menuTheme,
      logo,
      title,
      onMenuHeaderClick = () => null,
      i18nRender,
      menuHeaderRender,
      menuRender,
      layout,
      isMobile
    } = this
    const siderCls = ['ant-pro-sider-menu-sider']
    if (fixSiderbar) siderCls.push('fix-sider-bar')
    if (menuTheme === 'light') siderCls.push('light')
    //
    // const handleCollapse = (collapsed, type) => {
    //   this.$emit('collapse', collapsed)
    // }

    let headerDom = defaultRenderLogoAntTitle(h, {
      logo,
      title,
      menuHeaderRender,
      collapsed
    })

    if (layout === 'mixmenu' && !isMobile) {
      siderCls.push('mix')
      headerDom = null
    }
    return (
      this.currentMenus && (
        <Sider
          class={siderCls}
          breakpoint={'lg'}
          trigger={null}
          width={siderWidth}
          collapsedWidth={48}
          theme={menuTheme}
          collapsible={collapsible}
          collapsed={this.currentCollapsed}
        >
          {headerDom && (
            <div class="ant-pro-sider-menu-logo" onClick={onMenuHeaderClick} id="logo">
              <router-link to={{ path: '/' }}>{headerDom}</router-link>
            </div>
          )}
          {(menuRender && ((isFun(menuRender) && menuRender(h, this.$props)) || menuRender)) || (
            <BaseMenu
              collapsed={collapsed}
              menus={this.currentMenus}
              mode={mode}
              theme={menuTheme}
              i18nRender={i18nRender}
            />
          )}
        </Sider>
      )
    )
  }
}

export default SiderMenu
