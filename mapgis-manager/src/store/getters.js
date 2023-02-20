const getters = {
  isMobile: state => state.app.isMobile,
  lang: state => state.app.lang,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.name,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  userInfo: state => state.user.info,
  addRouters: state => state.permission.addRouters,
  multiTab: state => state.app.multiTab,
  cachedViews: state => state.tagsView.cachedViews,
  microApps: state => state.microApps.apps,
  mounting: state => state.microApps.mounting,
  casInfo: state => state.cas.info,
  systemConfig: state => state.server.systemConfig,
  baseConfig: state => state.server.baseConfig
}

export default getters
