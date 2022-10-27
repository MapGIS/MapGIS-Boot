<template>
  <div>
    <a-form-model v-if="configLoaded" :label-col="{ span: 4 }" :wrapper-col="{ span: 12 }" ref="form" :model="form">
      <a-form-model-item label="是否启用防暴力破解" prop="enabled">
        <a-checkbox :checked="form.enabled" @change="handleChange" />
      </a-form-model-item>
      <a-form-model-item label="允许连续失败次数" prop="maxRetryCount">
        <a-input-number v-model="form.maxRetryCount" :min="1" />
      </a-form-model-item>
      <a-form-model-item label="自动解锁时间（分钟）" prop="maxRetryCount">
        <a-input-number v-model="form.lockTime" :min="1" />
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
  enabled: false,
  maxRetryCount: 5,
  lockTime: 10
}

export default {
  name: 'PasswordProtectedConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {}
    }
  },
  async mounted() {
    const configInfoResult = await getConfigByKey('security.passwordProtected')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, {
      enabled: configValue.enabled,
      maxRetryCount: configValue.maxRetryCount,
      lockTime: configValue.lockTime
    })

    this.configLoaded = true
  },
  methods: {
    handleChange(e) {
      this.$set(this.form, 'enabled', e.target.checked)
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.configInfo.configValue = JSON.stringify({
            enabled: this.form.enabled,
            maxRetryCount: this.form.maxRetryCount,
            lockTime: this.form.lockTime
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

<style></style>
