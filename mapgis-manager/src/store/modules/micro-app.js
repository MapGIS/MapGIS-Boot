import { getMicroApps } from '@/api/system/microApp'

export default {
  state: {
    apps: [],
    mounting: false
  },
  mutations: {
    SET_APPS(state, apps) {
      state.apps = apps
    },
    TOGGLE_MOUNTING(state, mounting) {
      state.mounting = mounting
    }
  },
  actions: {
    GenerateMicroApps({ commit }) {
      return new Promise(resolve => {
        getMicroApps().then(res => {
          commit('SET_APPS', res.data)
          resolve()
        })
      })
    },
    beforeMount({ commit }) {
      commit('TOGGLE_MOUNTING', true)
    },
    afterUnmount({ commit }) {
      commit('TOGGLE_MOUNTING', false)
    }
  }
}
