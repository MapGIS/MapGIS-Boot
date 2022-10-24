<template>
  <div>
    <a-form-model v-if="configLoaded" :label-col="{ span: 4 }" :wrapper-col="{ span: 12 }" ref="form" :model="form">
      <a-form-model-item label="是否允许同时登录" prop="soloLoginEnabled">
        <a-checkbox :checked="form.soloLoginEnabled" @change="handleSoloLoginChange" />
      </a-form-model-item>
      <a-form-model-item label="是否启用验证码" prop="captchaEnabled">
        <a-checkbox v-model="form.captchaEnabled" @change="handleCaptchaChange" />
      </a-form-model-item>
      <a-form-model-item label="验证码类型" prop="captchaType">
        <div class="login-config-captcha">
          <div class="captcha-type-item" @click="form.captchaType = 'math'">
            <img :src="require('@/assets/images/captcha-math.png')" alt="math" />
            <div class="captcha-type-selectIcon" v-if="form.captchaType === 'math'">
              <a-icon type="check" />
            </div>
          </div>
          <div class="captcha-type-item" @click="form.captchaType = 'char'">
            <img :src="require('@/assets/images/captcha-char.png')" alt="char" />
            <div class="captcha-type-selectIcon" v-if="form.captchaType === 'char'">
              <a-icon type="check" />
            </div>
          </div>
        </div>
      </a-form-model-item>
      <a-form-model-item :wrapper-col="{ span: 12, offset: 4 }">
        <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
          保存
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'

const defaultConfigValue = {
  soloLoginEnabled: true,
  captchaEnabled: true,
  captchaType: 'math'
}

export default {
  name: 'UserLoginConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      roleOptions: []
    }
  },
  async mounted() {
    const configInfoResult = await getConfigByKey('security.login')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, {
      soloLoginEnabled: configValue.soloLoginEnabled,
      captchaEnabled: configValue.captchaEnabled,
      captchaType: configValue.captchaType
    })

    this.configLoaded = true
  },
  methods: {
    handleSoloLoginChange(e) {
      this.$set(this.form, 'soloLoginEnabled', e.target.checked)
    },
    handleCaptchaChange(e) {
      this.$set(this.form, 'captchaEnabled', e.target.checked)
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.configInfo.configValue = JSON.stringify({
            soloLoginEnabled: this.form.soloLoginEnabled,
            captchaEnabled: this.form.captchaEnabled,
            captchaType: this.form.captchaType
          })
          updateConfig(this.configInfo)
            .then(response => {
              this.$message.success('设置成功', 3)
            })
            .finally(() => {
              this.submitLoading = false
            })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login-config-captcha {
  display: flex;

  .captcha-type-item {
    margin-right: 16px;
    position: relative;
    border-radius: 4px;
    cursor: pointer;

    img {
      width: 90px;
      height: 32px;
    }

    .captcha-type-selectIcon {
      position: absolute;
      top: 0;
      right: 0;
      width: 100%;
      padding-top: 12px;
      padding-left: 72px;
      height: 100%;
      color: @primary-color;
      font-size: 20px;
      font-weight: 900;
    }
  }
}
</style>
