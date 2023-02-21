/**
 * 向后端请求用户的菜单，动态生成路由
 */
import { constantRouterMap } from '@/config/router.config'
import { generatorDynamicRouter } from '@/router/generator-routers'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = constantRouterMap.concat(routers)
      state.routers = constantRouterMap.concat(routers)
      // state.routers = constantRouterMap
    }
  },
  actions: {
    generateRoutes({ commit }, data) {
      return new Promise(resolve => {
        generatorDynamicRouter(data).then(routers => {
          commit('SET_ROUTERS', routers)
          resolve()
        })
      })
    }
  }
}

export default permission
