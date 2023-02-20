<template>
  <a-form-model
    v-if="configLoaded"
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
  >
    <a-form-model-item :label="$t('security.config.whether.to.allow.simultaneous.login')" prop="soloLoginEnabled">
      <a-checkbox :checked="form.soloLoginEnabled" @change="handleSoloLoginChange" />
    </a-form-model-item>
    <a-form-model-item :label="$t('security.config.whether.to.enable.captcha')" prop="captchaEnabled">
      <a-checkbox v-model="form.captchaEnabled" @change="handleCaptchaChange" />
    </a-form-model-item>
    <a-form-model-item :label="$t('security.config.captcha.type')" prop="captchaType">
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
    <a-form-model-item :label="$t('security.config.captcha.display.limit')" prop="maxRetryCount">
      <a-input-number v-model="form.maxRetryCount" :min="0" />
    </a-form-model-item>
    <a-form-model-item :label="$t('security.config.error.record.validity.period')" prop="recordTime">
      <a-input-number v-model="form.recordTime" :min="1" />
    </a-form-model-item>
    <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
      <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']" icon="save">
        {{ $t('save') }}
      </a-button>
    </a-form-model-item>
  </a-form-model>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'

const defaultConfigValue = {
  soloLoginEnabled: true,
  captchaEnabled: true,
  captchaType: 'math',
  maxRetryCount: 1,
  recordTime: 10
}

export default {
  name: 'UserLoginConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {}
    }
  },
  async mounted() {
    const configInfoResult = await getConfigByKey('security.login')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, { ...configValue })

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
          this.configInfo.configValue = JSON.stringify({ ...this.form })
          const settingMessage = this.$t('setting.success')
          updateConfig(this.configInfo)
            .then(response => {
              this.$message.success(settingMessage, 3)
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
