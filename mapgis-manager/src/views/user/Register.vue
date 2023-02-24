<template>
  <div class="main user-layout-register">
    <h3>
      <span>{{ $t('register') }}</span>
    </h3>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-alert
        v-if="isRegisterError"
        type="error"
        showIcon
        style="margin-bottom: 24px"
        :message="registerErrorInfo"
        closable
        :after-close="handleCloseRegisterError"
      />
      <a-form-model-item prop="username">
        <a-input v-model="form.username" size="large" autocomplete="off" :placeholder="$t('username')" />
      </a-form-model-item>
      <a-form-model-item has-feedback prop="password">
        <a-input-password
          v-model="form.password"
          size="large"
          autocomplete="off"
          :placeholder="$t('password')"
          :maxLength="16"
        />
      </a-form-model-item>
      <a-form-model-item has-feedback prop="confirmPassword">
        <a-input-password
          v-model="form.confirmPassword"
          size="large"
          autocomplete="off"
          :placeholder="$t('password.confirm')"
          :maxLength="20"
        />
      </a-form-model-item>
      <a-row :gutter="16" v-if="loginConfig.captchaEnabled">
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
      <a-form-item>
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="register-button"
          :loading="registering"
          @click.stop.prevent="handleRegister"
          :disabled="registering"
        >
          {{ $t('register') }}
        </a-button>
        <router-link class="login" :to="{ name: 'login' }">
          {{ $t('user.register.use.existed.account.login') }}
        </router-link>
      </a-form-item>
    </a-form-model>
  </div>
</template>

<script>
import { getCodeImg, register } from '@/api/login'
import { serverMixin } from '@/store/server-mixin'

export default {
  name: 'Register',
  components: {},
  mixins: [serverMixin],
  data() {
    const validateNewPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('please.input.password.new')))
      } else if (
        !/((^(?=.*[a-z])(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[\da-zA-Z\W]{8,16}$))/.test(
          value
        )
      ) {
        callback(new Error(this.$t('password.length.limit')))
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('please.input.password.new.again.confirm')))
      } else if (value !== this.form.password) {
        callback(new Error(this.$t('password.inconsistent')))
      } else {
        callback()
      }
    }
    return {
      codeUrl: '',
      isRegisterError: false,
      registerErrorInfo: '',
      form: {
        username: undefined,
        password: undefined,
        confirmPassword: undefined,
        code: undefined,
        uuid: undefined
      },
      rules: {
        username: [
          { required: true, trigger: 'blur', message: this.$t('please.input.username') },
          {
            min: 2,
            max: 20,
            message: this.$t('username.length.limit'),
            trigger: 'blur'
          }
        ],
        password: [
          { required: true, trigger: 'blur', message: this.$t('please.input.password') },
          { required: true, validator: validateNewPass, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: this.$t('please.password.confirm') },
          { required: true, validator: validateConfirmPass, trigger: 'blur' }
        ],
        code: [{ required: true, trigger: 'change', message: this.$t('please.input.captcha') }]
      },
      registerBtn: false,
      registering: false,
      loginConfig: {}
    }
  },
  computed: {},
  created() {},
  mounted() {
    this.loginConfig = this.systemConfig.loginConfig
    if (this.loginConfig.captchaEnabled) {
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
    handleRegister() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.registering = true
          register(this.form)
            .then(res => this.registerSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.registering = false
            })
        } else {
          setTimeout(() => {
            this.registering = false
          }, 600)
        }
      })
    },
    registerSuccess(res) {
      this.$router.push({ path: '/login' })
      // 延迟 1 秒显示欢迎信息
      const username = this.form.username
      const message = this.$t('user.register.success', { username: username })
      setTimeout(() => {
        this.$notification.success({
          message: message
        })
      }, 1000)
      this.handleCloseRegisterError()
    },
    requestFailed(err) {
      this.isRegisterError = true
      this.registerErrorInfo = err
      this.form.code = undefined
      if (this.loginConfig.captchaEnabled) {
        this.getCode()
      }
    },
    handleCloseRegisterError() {
      this.isRegisterError = false
      this.registerErrorInfo = ''
    }
  }
}
</script>
<style lang="less">
.user-register {
  &.error {
    color: #ff0000;
  }
  &.warning {
    color: #ff7e05;
  }
  &.success {
    color: #52c41a;
  }
}
.user-layout-register {
  .ant-input-group-addon:first-child {
    background-color: #fff;
  }
}
</style>
<style lang="less" scoped>
.user-layout-register {
  & > h3 {
    font-size: 16px;
    margin-bottom: 20px;
  }
  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }
  .register-button {
    width: 50%;
  }
  .login {
    float: right;
    line-height: 40px;
  }
}
</style>
