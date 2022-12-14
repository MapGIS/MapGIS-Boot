import router from './router'
import store from './store'
import storage from 'store'
import NProgress from 'nprogress' // progress bar
import '@/components/NProgress/nprogress.less' // progress bar custom style
import notification from 'ant-design-vue/es/notification'
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { i18nRender } from '@/locales'
import qs from 'qs'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const allowList = ['login', 'register'] // no redirect allowList
const loginRoutePath = '/user/login'
const defaultRoutePath = '/index'

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && typeof to.meta.title !== 'undefined' && setDocumentTitle(`${i18nRender(to.meta.title)} - ${domTitle}`)
  /* has token */
  if (storage.get(ACCESS_TOKEN)) {
    if (to.path === loginRoutePath || to.path === '/') {
      next({ path: defaultRoutePath })
      NProgress.done()
    } else {
      // check login user.roles is null
      if (store.getters.roles.length === 0) {
        // request login userInfo
        store
          .dispatch('GetInfo')
          .then(async res => {
            // generate micro apps
            await store.dispatch('GenerateMicroApps')
            // const roles = res.result && res.result.role
            const roles = res.roles
            // generate dynamic router
            store.dispatch('GenerateRoutes', { roles }).then(() => {
              // 根据roles权限生成可访问的路由表
              // 动态添加可访问路由表
              router.addRoutes(store.getters.addRouters)
              // router.addRoutes(accessRoutes)
              // 请求带有 redirect 重定向时，登录自动重定向到该地址
              next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
              // const redirect = decodeURIComponent(from.query.redirect || to.path)
              // if (to.path === redirect) {
              //   // set the replace: true so the navigation will not leave a history record
              //   next({ ...to, replace: true })
              // } else {
              //   // 跳转到目的路由
              //   next({ path: redirect })
              // }
            })
          })
          .catch(() => {
            notification.error({
              message: '错误',
              description: '请求用户信息失败，请重试'
            })
            // 失败时，获取用户信息失败时，调用登出，来清空历史保留信息
            store.dispatch('Logout').then(() => {
              location.href = '/'
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (allowList.includes(to.name)) {
      // 在免登录名单，直接进入
      next()
    } else {
      if (window._CONFIG['enableSSO']) {
        const queryParams = qs.parse(document.location.toString().split('?')[1])
        const token = queryParams.token

        // 判断来源是不是cas的地址
        if (
          token &&
          (window._CONFIG['casLoginUrl'].includes(document.referrer) ||
            document.referrer.includes(document.location.host))
        ) {
          validateToken(token, to, from, next)
        } else {
          window.location.href = window._CONFIG['casLoginUrl']
        }
      } else {
        next({ path: loginRoutePath, query: { redirect: to.fullPath } })
      }
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

function validateToken(token, to, from, next) {
  store
    .dispatch('ValidateLogin', token)
    .then(res => {
      const url = document.location.toString().split('?')[0]
      window.location.href = url
    })
    .catch(() => {
      window.location.href = window._CONFIG['casLoginUrl']
    })
}
