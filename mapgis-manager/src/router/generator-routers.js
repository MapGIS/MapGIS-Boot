// eslint-disable-next-line
import { getRouters } from '@/api/menu'
import { staticMenuRouterMap, otherRouterMap } from '@/config/router.config'
import allIcon from '@/core/icons'
import { validURL } from '@/utils/validate'
import { UserLayout, BlankLayout, PageView } from '@/layouts'
import auth from '@/plugins/auth'

// 前端路由表
const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: () => import('@/layouts/BasicLayout'),
  BlankLayout: BlankLayout,
  RouteView: () => import('@/layouts/RouteView'),
  PageView: PageView,
  UserLayout: UserLayout, // 登陆注册页面的通用布局

  // 你需要动态引入的页面组件
  Index: () => import('@/views/index'),
  // account
  AccountSettings: () => import('@/views/account/settings'),
  // job log
  JobLog: () => import('@/views/monitor/job/log'),
  // 授权用户
  AuthUser: () => import('@/views/system/role/authUser'),
  // 公告新增修改
  NoticeForm: () => import('@/views/system/notice/CreateForm'),
  // 修改生成配置
  GenEdit: () => import('@/views/tool/gen/modules/GenEdit')
}

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*',
  redirect: '/404',
  hidden: true
}

// 根级菜单
const rootRouter = {
  key: '',
  name: '',
  path: '',
  component: 'BasicLayout',
  redirect: '/dashboard',
  meta: {
    title: '总览'
  },
  children: []
}

/**
 * 动态生成菜单
 * @param token
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = token => {
  return new Promise((resolve, reject) => {
    // 向后端请求路由数据
    getRouters()
      .then(res => {
        const routers = generatorRouter(staticMenuRouterMap.concat(res.data))
        resolve(routers)
      })
      .catch(err => {
        reject(err)
      })
  })
}

export const generatorStaticRouter = () => {
  return new Promise((resolve, reject) => {
    resolve(generatorRouter(staticMenuRouterMap.concat([])))
  })
}

function generatorRouter(routerData) {
  const menuNav = []
  // 其他非菜单路由，需要进行权限过滤处理
  const filterOtherRouterMap = filterDynamicRoute(otherRouterMap)
  filterOtherRouterMap && routerData.unshift(filterOtherRouterMap)
  // 这里需要套一个根级菜单
  rootRouter.children = routerData
  menuNav.push(rootRouter)
  const routers = generator(menuNav)
  routers.push(notFoundRouter)
  return routers
}

// 动态路由遍历，验证是否具备权限
export function filterDynamicRoute(route) {
  if (route) {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        return route
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        return route
      }
    } else if (route.children && route.children.length > 0) {
      route.children = route.children.map(t => filterDynamicRoute(t)).filter(t => !!t)
      return route
    } else {
      return route
    }
  }
  return null
}

function formatHasOneMenuChild(item) {
  // 只有一个子路由且为菜单类型，直接将其提到外面
  if (item.meta === undefined && item.name === undefined && item.children && item.children.length === 1) {
    const child = item.children[0]
    item = child
  }
  return item
}

/**
 * 格式化树形结构数据 生成 vue-router 层级路由表
 *
 * @param routerMap
 * @param parent
 * @returns {*}
 */
export const generator = (routerMap, parent) => {
  return routerMap.map(item => {
    item = formatHasOneMenuChild(item)
    const { title, show, hideChildren, hiddenHeaderContent, hidden, icon, noCache } = item.meta || {}
    if (item.component) {
      // Layout ParentView 组件特殊处理
      if (item.component === 'Layout') {
        item.component = 'RouteView'
      } else if (item.component === 'ParentView') {
        // 三级菜单处理
        item.component = 'RouteView'
        item.path = `${(parent && parent.path) || ''}/${item.path}`
      }
    }
    // 是否为微应用
    const isMircoApp = item.component === 'system/microPage/index'
    const isInnerLink = item.meta && item.meta.routerType === 'innerLink'
    if (item.isFrame === 0) {
      item.target = '_blank'
    }
    const currentRouter = {
      // 如果路由设置了 path，则作为默认 path，否则 路由地址 动态拼接生成如 /dashboard/workplace
      path: item.path || `${(parent && parent.path) || ''}/${item.key}`,
      // 路由名称，建议唯一
      name: item.name || item.key || '',
      // 该路由对应页面的 组件(动态加载)
      component: constantRouterComponents[item.component || item.key] || (() => import(`@/views/${item.component}`)),
      hidden: item.hidden,
      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: {
        title: title,
        icon: allIcon[icon + 'Icon'] || icon,
        hiddenHeaderContent: hiddenHeaderContent,
        // 目前只能通过判断path的http链接来判断是否外链
        target: isInnerLink ? '_self' : validURL(item.path) ? '_blank' : '',
        permission: item.name,
        keepAlive: noCache === undefined ? false : !noCache,
        hidden: hidden
      },
      redirect: item.redirect
    }
    // 是否设置了隐藏菜单
    if (show === false) {
      currentRouter.hidden = true
    }
    // antdv-pro的pro-layout要求每个路径需为全路径
    if (isMircoApp) {
      if (!item.path.startsWith('/')) {
        item.path = '/' + item.path
      }
      // 实现微应用内部的子路由
      currentRouter.path = item.path + '/*'
    } else if (!isInnerLink && !constantRouterComponents[item.component || item.key]) {
      if (!validURL(item.path)) {
        currentRouter.path = `${(parent && parent.path) || ''}/${item.path}`
      }
    }
    // path中可能出现'//',导致路由异常
    if (currentRouter.path && !validURL(currentRouter.path)) {
      currentRouter.path = currentRouter.path.replace('//', '/')
    }
    // 是否设置了隐藏子菜单
    if (hideChildren) {
      currentRouter.hideChildrenInMenu = true
    }
    // 是否有子菜单，并递归处理，并将父path传入
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter)
      if (!item.meta || !item.meta.noRedirect) {
        const children = currentRouter.children
        for (let i = 0; i < children.length; i++) {
          if (!children[i].hidden) {
            currentRouter.redirect = currentRouter.children[i].path
            break
          }
        }
      }
    }
    return currentRouter
  })
}
