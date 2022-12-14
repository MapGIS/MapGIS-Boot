import storage from 'store'
import { login, getInfo, logout, thirdLogin, casLogin } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo)
          .then(res => {
            storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', res.token)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo(state.token)
          .then(res => {
            const user = res.user
            let avatarUrl = window._CONFIG['domianURL'] + user.avatar
            if (user.avatar.startsWith('http') || user.avatar.startsWith('https')) {
              avatarUrl = user.avatar
            }
            const avatar = user.avatar === '' ? require('@/assets/images/profile.jpg') : avatarUrl
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit('SET_ROLES', res.roles)
              commit('SET_PERMISSIONS', res.permissions)
            } else {
              commit('SET_ROLES', ['ROLE_DEFAULT'])
            }
            commit('SET_NAME', user.nickName)
            commit('SET_AVATAR', avatar)
            resolve(res)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 登出
    Logout({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            if (window._CONFIG['enableSSO']) {
              window.location.href = window._CONFIG['casLogoutUrl']
            }
            resolve()
          })
          .catch(error => {
            reject(error)
          })
          .finally(() => {
            commit('SET_TOKEN', '')
            commit('SET_ROLES', [])
            commit('SET_PERMISSIONS', [])
            storage.remove(ACCESS_TOKEN)
          })
      })
    },

    // 第三方登录
    ThirdLogin({ commit }, param) {
      return new Promise((resolve, reject) => {
        thirdLogin(param.token, param.source)
          .then(res => {
            storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', res.token)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // CAS登录
    ValidateLogin({ commit }, token) {
      return new Promise((resolve, reject) => {
        casLogin(token)
          .then(res => {
            storage.set(ACCESS_TOKEN, res.token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', res.token)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
}

export default user
