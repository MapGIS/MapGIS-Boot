import request from '@/utils/request'

const userApi = {
  Login: '/xxx/rest/services/auth/login',
  Logout: '/xxx/rest/services/auth/logout',
  Register: '/xxx/rest/services/auth/register',
  // get my info
  UserInfo: '/xxx/rest/manager/system/user/getInfo'
}

/**
 * login func
 * @param parameter
 * @returns {*}
 */
export function login(parameter) {
  return request({
    url: userApi.Login,
    method: 'post',
    data: parameter
  })
}

// 注册方法
export function register(data) {
  return request({
    url: userApi.Register,
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

export function getInfo() {
  return request({
    url: userApi.UserInfo,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function logout() {
  return request({
    url: userApi.Logout,
    method: 'delete',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/xxx/rest/services/auth/captchaImage',
    method: 'get'
  })
}
