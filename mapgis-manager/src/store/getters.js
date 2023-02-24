const getters = {
  isMobile: state => state.app.isMobile,
  lang: state => state.app.lang,
  theme: state => state.app.theme,
  color: state => state.app.color,
  enableDepartment: state => state.app.enableDepartment,
  enablePost: state => state.app.enablePost,
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
  baseConfig: state => state.server.baseConfig,
  domTitle: state => {
    if (
      state.server.baseConfig &&
      state.server.baseConfig.header &&
      !state.server.baseConfig.header.defaultLogoAndTitle
    ) {
      return state.server.baseConfig.header.title
    }
    return window._CONFIG['productTitle']
  }
}

export default getters
