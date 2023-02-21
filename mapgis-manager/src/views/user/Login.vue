<template>
  <div class="main">
    <a-form-model id="formLogin" ref="form" class="user-layout-login" :model="form" :rules="rules">
      <a-alert
        v-if="isLoginError"
        type="error"
        showIcon
        style="margin-bottom: 24px"
        :message="loginErrorInfo"
        closable
        :after-close="handleCloseLoginError"
      />
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" placeholder="admin">
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item prop="password">
        <a-input-password v-model="form.password" size="large" placeholder="cloud123.mapgis">
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }" />
        </a-input-password>
      </a-form-model-item>
      <a-row :gutter="16" v-if="captchaEnabled">
        <a-col class="gutter-row" :span="16">
          <a-form-model-item prop="code">
            <a-input v-model="form.code" size="large" type="text" autocomplete="off" :placeholder="$t('captcha')">
              <a-icon slot="prefix" type="security-scan" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-model-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <img class="getCaptcha" :src="codeUrl" @click="getCode" />
        </a-col>
      </a-row>
      <a-form-model-item prop="rememberMe">
        <a-checkbox :checked="form.rememberMe" @change="rememberMe">
          {{ $t('user.login.password.remember') }}
        </a-checkbox>
        <router-link v-if="registerConfig.enabled" class="register" :to="{ name: 'register' }" style="float: right">
          {{ $t('user.login.account.register') }}
        </router-link>
      </a-form-model-item>
      <a-form-item style="margin-top: 24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="logining"
          :disabled="logining"
          @click="handleSubmit"
        >
          {{ $t('login') }}
        </a-button>
      </a-form-item>
      <div v-if="otherLoginValid" class="user-login-other">
        <span>{{ $t('user.login.others') }}</span>
        <cas-login :config="casConfig"></cas-login>
        <third-login :config="oauthConfig" ref="thirdLogin"></third-login>
      </div>
    </a-form-model>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { getIsNeedCode, getCodeImg } from '@/api/login'
import { serverMixin } from '@/store/server-mixin'
import { LOGIN_USERNAME, LOGIN_PASSWORD, LOGIN_REMEMBERME } from '@/store/mutation-types'
import storage from 'store'
import ThirdLogin from './third/ThirdLogin'
import CasLogin from './cas/CasLogin'

export default {
  components: {
    ThirdLogin,
    CasLogin
  },
  mixins: [serverMixin],
  data() {
    return {
      codeUrl: '',
      isLoginError: false,
      loginErrorInfo: '',
      form: {
        username: 'admin',
        password: '',
        code: undefined,
        uuid: '',
        rememberMe: false
      },
      rules: {
        username: [{ required: true, message: this.$t('please.input.username'), trigger: 'blur' }],
        password: [{ required: true, message: this.$t('please.input.password'), trigger: 'blur' }],
        code: [{ required: true, message: this.$t('please.input.captcha'), trigger: 'blur' }]
      },
      logining: false,
      isNeedCaptcha: false,
      registerConfig: {
        enabled: false
      },
      casConfig: {},
      oauthConfig: {},
      loginConfig: {
        captchaEnabled: false,
        maxRetryCount: 1
      }
    }
  },
  computed: {
    otherLoginValid() {
      return (this.casConfig.enabled && this.casConfig.isReserveDefaultLogin) || this.oauthConfig.length
    },
    captchaEnabled() {
      if (this.loginConfig.captchaEnabled && (this.isNeedCaptcha || this.loginConfig.maxRetryCount === 0)) {
        return true
      }
      return false
    }
  },
  created() {
    this.getStorage()
  },
  mounted() {
    this.casConfig = this.systemConfig.casConfig
    this.oauthConfig = this.systemConfig.oauthConfig
    this.registerConfig = this.systemConfig.registerConfig
    this.loginConfig = this.systemConfig.loginConfig

    if (this.captchaEnabled) {
      this.getCode()
    }
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = 'data:image/gif;base64,' + res.img
        this.form.uuid = res.uuid
      })
    },
    getStorage() {
      const username = storage.get(LOGIN_USERNAME)
      const password = storage.get(LOGIN_PASSWORD)
      const rememberMe = storage.get(LOGIN_REMEMBERME)
      if (username) {
        this.form = {
          username: username,
          password: password,
          rememberMe: rememberMe
        }
      }
    },
    rememberMe(e) {
      this.form.rememberMe = e.target.checked
    },
    ...mapActions(['login', 'logout']),
    handleSubmit() {
      this.logining = true
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.rememberMe) {
            storage.set(LOGIN_USERNAME, this.form.username)
            storage.set(LOGIN_PASSWORD, this.form.password)
            storage.set(LOGIN_REMEMBERME, this.form.rememberMe)
          } else {
            storage.remove(LOGIN_USERNAME)
            storage.remove(LOGIN_PASSWORD)
            storage.remove(LOGIN_REMEMBERME)
          }
          this.login(this.form)
            .then(res => this.loginSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.logining = false
            })
        } else {
          setTimeout(() => {
            this.logining = false
          }, 600)
        }
      })
    },
    loginSuccess(res) {
      this.$router.push({ path: '/' })
      this.handleCloseLoginError()
    },
    requestFailed(err) {
      this.isLoginError = true
      this.loginErrorInfo = err
      this.form.code = undefined
      getIsNeedCode(this.form.username).then(res => {
        this.isNeedCaptcha = res.isNeedCaptcha
        if (this.captchaEnabled) {
          this.getCode()
        }
      })
    },
    handleCloseLoginError() {
      this.isLoginError = false
      this.loginErrorInfo = ''
    }
  }
}
</script>

<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }
  .user-login-other {
    display: flex;
    align-items: center;
    text-align: left;
    margin-top: 24px;
    color: rgba(255, 255, 255, 0.85);
  }

  /deep/ .ant-checkbox-wrapper,
  .register {
    color: rgba(255, 255, 255, 0.85);
  }
}
</style>
