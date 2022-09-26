import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import tagsView from './modules/tagsView'

// default router permission control
// import permission from './modules/permission'
import permission from './modules/async-router'

// dynamic router permission control (Experimental)
// import permission from './modules/async-router'

import microApps from './modules/micro-app'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    permission,
    microApps
  },
  state: {},
  mutations: {},
  actions: {},
  getters
})
