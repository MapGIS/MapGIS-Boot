import Vue from 'vue'
import Vuex from 'vuex'

import app from './modules/app'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/async-router'
import microApps from './modules/micro-app'
import cas from './modules/cas'

import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    permission,
    microApps,
    cas
  },
  state: {},
  mutations: {},
  actions: {},
  getters
})
