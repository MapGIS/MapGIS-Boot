<template>
  <pro-layout
    :menus="menus"
    :collapsed="collapsed"
    :mediaQuery="query"
    :isMobile="isMobile"
    :handleMediaQuery="handleMediaQuery"
    :handleCollapse="handleCollapse"
    :i18nRender="i18nRender"
    v-bind="settings"
  >
    <!-- layout content -->
    <!-- 2021.01.15 默认固定页头，去掉样式paddingTop: fixedHeader ? '64' : '0'  -->
    <a-layout-content
      :style="{ height: '100%', margin: '0 0 0px 0' }"
      :class="settings.multiTab ? 'has-multi-tab' : ''"
    >
      <multi-tab v-if="settings.multiTab"></multi-tab>
      <transition name="page-transition"> </transition>
    </a-layout-content>
    <!-- 1.0.0+ 版本 pro-layout 提供 API，
          我们推荐使用这种方式进行 LOGO 和 title 自定义
    -->
    <template v-slot:menuHeaderRender>
      <div>
        <img v-if="logo" :src="logo" class="logo" alt="logo" />
        <h1>{{ title }}</h1>
      </div>
    </template>

    <setting-drawer v-if="isProPreviewSite" :settings="settings" @change="handleSettingChange" />
    <template v-slot:rightContentRender>
      <right-content
        :top-menu="
          settings.layout === 'topmenu' || settings.layout === 'mixmenu' || settings.layout === 'mixmenu-center'
        "
        :is-mobile="isMobile"
        :theme="settings.theme"
      />
    </template>
    <template v-slot:footerRender v-if="!hideFooter">
      <global-footer :copyright="copyright" />
    </template>
    <keep-alive>
      <router-view />
    </keep-alive>
    <page-header-wrapper v-show="mounting">
      <div id="micro-page" class="micro-page-view-box"></div>
    </page-header-wrapper>
  </pro-layout>
</template>

<script>
import MultiTab from '@/components/MultiTab'
import { updateTheme } from '@/components/SettingDrawer/settingConfig'
import SettingDrawer from '@/components/SettingDrawer'
import { i18nRender } from '@/locales'
import { mapState } from 'vuex'
import {
  CONTENT_WIDTH_TYPE,
  SIDE_COLLAPSED,
  TOGGLE_MOBILE_TYPE,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_FIXED_HEADER,
  TOGGLE_FIXED_SIDEBAR,
  TOGGLE_LAYOUT,
  TOGGLE_NAV_THEME,
  TOGGLE_WEAK,
  TOGGLE_COLOR,
  TOGGLE_MULTI_TAB,
  TABLE_SIZE,
  TABLE_BORDERED,
  HIDE_FOOTER,
  HIDE_BREADCRUMB,
  FORM_MODE
} from '@/store/mutation-types'

import defaultSettings from '@/config/defaultSettings'
import RightContent from '@/components/GlobalHeader/RightContent'
import GlobalFooter from '@/components/GlobalFooter'
import Ads from '@/components/Other/CarbonAds'
import { baseMixin } from '@/store/app-mixin'
import { getBaseConfig } from '@/api/system/config'

export default {
  name: 'BasicLayout',
  components: {
    SettingDrawer,
    RightContent,
    GlobalFooter,
    Ads,
    MultiTab
  },
  mixins: [baseMixin],
  data() {
    return {
      // preview.pro.antdv.com only use.
      isProPreviewSite: process.env.VUE_APP_PREVIEW === 'true',
      menus: [],
      // 侧栏展开状态
      collapsed: false,
      settings: {
        // 布局类型
        layout: defaultSettings.layout, // 'sidemenu', 'topmenu', 'mixmenu', 'mixmenu-center'
        // CONTENT_WIDTH_TYPE
        contentWidth: defaultSettings.layout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : defaultSettings.contentWidth,
        // 主题 'dark' | 'light' | 'night'
        theme: defaultSettings.navTheme,
        // 主色调
        primaryColor: defaultSettings.primaryColor,
        fixedHeader: defaultSettings.fixedHeader,
        fixSiderbar: defaultSettings.fixSiderbar,
        multiTab: defaultSettings.multiTab,
        colorWeak: defaultSettings.colorWeak,

        hideHintAlert: true,
        hideCopyButton: false,
        tableSize: defaultSettings.tableSize,
        tableBordered: defaultSettings.tableBordered,
        hideFooter: defaultSettings.hideFooter,
        hideBreadcrumb: defaultSettings.hideBreadcrumb
      },
      // 媒体查询
      query: {},
      logo: '',
      title: '',
      copyright: ''
    }
  },
  computed: {
    ...mapState({
      // 动态主路由
      mainMenu: state => state.permission.addRouters,
      mounting: state => state.microApps.mounting
    })
  },
  watch: {
    navTheme(val) {
      this.settings.theme = val
    },
    primaryColor(val) {
      this.settings.primaryColor = val
    },
    layout(val) {
      this.settings.layout = val
      if (val !== 'topmenu' && val !== 'mixmenu-center') {
        this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fluid
        if (val === 'mixmenu') {
          this.settings.fixedHeader = true
        }
      } else {
        this.settings.fixSiderbar = false
        this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fixed
      }
    },
    contentWidth(val) {
      this.settings.contentWidth = val
    },
    fixedHeader(val) {
      this.settings.fixedHeader = val
    },
    fixedSidebar(val) {
      this.settings.fixSiderbar = val
    },
    hideFooter(val) {
      this.settings.hideFooter = val
    },
    hideBreadcrumb(val) {
      this.settings.hideBreadcrumb = val
    },
    multiTab(val) {
      this.settings.multiTab = val
    },
    colorWeak(val) {
      this.settings.colorWeak = val
    },
    tableSize(val) {
      this.settings.tableSize = val
    },
    tableBordered(val) {
      this.settings.tableBordered = val
    }
  },
  created() {
    const routes = this.mainMenu.find(item => item.path === '/')
    this.menus = (routes && routes.children) || []
    // 处理侧栏展开状态
    this.$watch('collapsed', () => {
      this.$store.commit(SIDE_COLLAPSED, this.collapsed)
    })
  },
  mounted() {
    this.settings.layout = this.layout
    this.settings.contentWidth = this.contentWidth
    this.settings.theme = this.navTheme
    this.settings.primaryColor = this.primaryColor
    this.settings.fixedHeader = this.fixedHeader
    this.settings.fixSiderbar = this.fixedSidebar
    this.settings.multiTab = this.multiTab
    this.settings.colorWeak = this.colorWeak
    this.settings.tableSize = this.tableSize
    this.settings.tableBordered = this.tableBordered
    this.settings.hideFooter = this.hideFooter
    this.settings.hideBreadcrumb = this.hideBreadcrumb

    this.collapsed = this.sideCollapsed
    const userAgent = navigator.userAgent
    if (userAgent.indexOf('Edge') > -1) {
      this.$nextTick(() => {
        this.collapsed = !this.collapsed
        setTimeout(() => {
          this.collapsed = !this.collapsed
        }, 16)
      })
    }

    updateTheme(this.navTheme, this.settings.primaryColor)

    getBaseConfig().then(response => {
      const configValue = response.data
      if (configValue) {
        const {
          header: { logo, title },
          footer: { copyright }
        } = JSON.parse(configValue)
        this.logo = logo
        this.title = title
        this.copyright = copyright
      }
    })
  },
  methods: {
    i18nRender,
    handleMediaQuery(val) {
      this.query = val
      if (this.isMobile && !val['screen-xs']) {
        this.$store.commit(TOGGLE_MOBILE_TYPE, false)
        return
      }
      if (!this.isMobile && val['screen-xs']) {
        this.$store.commit(TOGGLE_MOBILE_TYPE, true)
        this.collapsed = true
        this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fluid
        // this.settings.fixSiderbar = false
      }
    },
    handleCollapse(val) {
      this.collapsed = val
    },
    handleSettingChange({ type, value }) {
      type && (this.settings[type] = value)
      switch (type) {
        case 'theme':
          this.$store.commit(TOGGLE_NAV_THEME, value)
          break
        case 'primaryColor':
          this.$store.commit(TOGGLE_COLOR, value)
          break
        case 'contentWidth':
          this.$store.commit(TOGGLE_CONTENT_WIDTH, value)
          break
        case 'layout':
          this.$store.commit(TOGGLE_LAYOUT, value)
          if (value !== 'topmenu' && value !== 'mixmenu-center') {
            this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fluid
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fluid)
            if (value === 'mixmenu') {
              this.settings.fixedHeader = true
              this.$store.commit(TOGGLE_FIXED_HEADER, true)
            }
          } else {
            this.settings.fixSiderbar = false
            this.$store.commit(TOGGLE_FIXED_SIDEBAR, false)
            this.settings.contentWidth = CONTENT_WIDTH_TYPE.Fixed
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fixed)
          }
          break
        case 'fixSiderbar':
          this.$store.commit(TOGGLE_FIXED_SIDEBAR, value)
          break
        case 'fixedHeader':
          this.$store.commit(TOGGLE_FIXED_HEADER, value)
          break
        case 'multiTab':
          this.$store.commit(TOGGLE_MULTI_TAB, value)
          break
        case 'colorWeak':
          this.$store.commit(TOGGLE_WEAK, value)
          break
        case 'tableSize':
          this.$store.commit(TABLE_SIZE, value)
          break
        case 'tableBordered':
          this.$store.commit(TABLE_BORDERED, value)
          break
        case 'hideFooter':
          this.$store.commit(HIDE_FOOTER, value)
          break
        case 'hideBreadcrumb':
          this.$store.commit(HIDE_BREADCRUMB, value)
          break
        case 'formMode':
          this.$store.commit(FORM_MODE, value)
          break
      }
    }
  }
}
</script>

<style lang="less">
@import './BasicLayout.less';
</style>
