import store from '@/store'
import storage from 'store'
import {
  SIDE_COLLAPSED,
  ACCESS_TOKEN,
  APP_LANGUAGE,
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
  FORM_MODE,
  ENABLE_DEPARTMENT,
  ENABLE_POST
} from '@/store/mutation-types'
import defaultSettings from '@/config/defaultSettings'

export default function Initializer() {
  store.commit(SIDE_COLLAPSED, storage.get(SIDE_COLLAPSED, false))
  store.commit(TOGGLE_LAYOUT, storage.get(TOGGLE_LAYOUT, defaultSettings.layout))
  store.commit(TOGGLE_FIXED_HEADER, storage.get(TOGGLE_FIXED_HEADER, defaultSettings.fixedHeader))
  store.commit(TOGGLE_FIXED_SIDEBAR, storage.get(TOGGLE_FIXED_SIDEBAR, defaultSettings.fixSiderbar))
  store.commit(TOGGLE_CONTENT_WIDTH, storage.get(TOGGLE_CONTENT_WIDTH, defaultSettings.contentWidth))

  store.commit(TOGGLE_NAV_THEME, storage.get(TOGGLE_NAV_THEME, defaultSettings.navTheme))
  store.commit(TOGGLE_WEAK, storage.get(TOGGLE_WEAK, defaultSettings.colorWeak))
  store.commit(TOGGLE_COLOR, storage.get(TOGGLE_COLOR, defaultSettings.primaryColor))
  store.commit(TOGGLE_MULTI_TAB, storage.get(TOGGLE_MULTI_TAB, defaultSettings.multiTab))
  store.commit('SET_TOKEN', storage.get(ACCESS_TOKEN))

  store.dispatch('setLang', storage.get(APP_LANGUAGE, 'zh-CN'))
  store.commit(TABLE_SIZE, storage.get(TABLE_SIZE, defaultSettings.tableSize))
  store.commit(TABLE_BORDERED, storage.get(TABLE_BORDERED, defaultSettings.tableBordered))
  store.commit(HIDE_FOOTER, storage.get(HIDE_FOOTER, defaultSettings.hideFooter))
  store.commit(HIDE_BREADCRUMB, storage.get(HIDE_BREADCRUMB, defaultSettings.hideBreadcrumb))
  store.commit(FORM_MODE, storage.get(FORM_MODE, defaultSettings.formMode))
  store.commit(ENABLE_DEPARTMENT, storage.get(ENABLE_DEPARTMENT, defaultSettings.enableDepartment))
  store.commit(ENABLE_POST, storage.get(ENABLE_POST, defaultSettings.enablePost))
  // last step
}
