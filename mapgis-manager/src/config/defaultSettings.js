/**
 * 项目默认配置项
 * primaryColor - 默认主题色, 如果修改颜色不生效，请清理 localStorage
 * navTheme - sidebar theme ['dark', 'light', 'night'] 三种主题
 * colorWeak - 色盲模式
 * layout - 整体布局方式 ['sidemenu', 'topmenu', 'mixmenu', 'mixmenu-center'] 四种布局
 * fixedHeader - 固定 Header : boolean
 * fixSiderbar - 固定左侧菜单栏 ： boolean
 * contentWidth - 内容区布局： 流式 |  固定
 *
 * storageOptions: {} - Vue-ls 插件配置项 (localStorage/sessionStorage)
 *
 */

module.exports = {
  navTheme: 'dark', // theme for nav menu
  primaryColor: '#1890ff', // primary color of ant design
  layout: 'sidemenu', // nav menu position: `sidemenu` or `topmenu` or `mixmenu` or `mixmenu-center`
  contentWidth: 'Fluid', // layout of content: `Fluid` or `Fixed`, only works when layout is topmenu or `mixmenu-center`
  fixedHeader: true, // sticky header
  fixSiderbar: true, // sticky siderbar
  colorWeak: false,
  multiTab: true,
  tableSize: 'middle',
  tableBordered: false,
  hideFooter: false,
  hideBreadcrumb: false,
  formMode: 'Drawer', // crud form mode: `Drawer` or `Modal`
  enableDepartment: true,
  enablePost: true,
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
}
