/**
 *第三方登录
 */
import { mapActions } from 'vuex'
import { timeFix } from '@/utils/util'

const thirdLoginMixin = {
  data() {
    return {
      // 第三方登录相关信息
      thirdType: '',
      thirdLoginState: false
    }
  },
  created() {},
  methods: {
    ...mapActions(['ThirdLogin']),
    onThirdLogin(source) {
      const loginUrl = process.env.VUE_APP_BASE_API + `/xxx/rest/services/auth/thirdLogin/render/${source}`
      window.open(
        loginUrl,
        `login ${source}`,
        'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no'
      )
      const that = this
      that.thirdType = source
      that.thirdLoginState = false
      const receiveMessage = function (event) {
        const token = event.data
        if (typeof token === 'string') {
          // 如果是字符串类型 说明是token信息
          if (token === '登录失败') {
            that.$message.warning(token)
          } else {
            that.doThirdLogin(token)
          }
        } else if (typeof token === 'object') {
          that.$message.warning('对不起，您没有绑定注册用户，请先注册后在个人设置绑定第三方授权信息！')
        } else {
          that.$message.warning('不识别的信息传递')
        }

        window.removeEventListener('message', receiveMessage)
      }
      window.addEventListener('message', receiveMessage, false)
    },
    // 根据token执行登录
    doThirdLogin(token) {
      if (this.thirdLoginState === false) {
        this.thirdLoginState = true
        const param = {}
        param.source = this.thirdType
        param.token = token
        this.ThirdLogin(param)
          .then(res => this.loginSuccess(res))
          .catch(err => this.requestFailed(err))
      }
    },
    loginSuccess(res) {
      this.$router.push({ path: '/' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      }, 1000)
    },
    requestFailed(err) {
      const description = ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试'
      this.$notification['error']({
        message: '登录失败',
        description: description,
        duration: 4
      })
    }
  }
}

export { thirdLoginMixin }
