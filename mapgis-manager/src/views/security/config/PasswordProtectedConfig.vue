<template>
  <a-form-model
    v-if="configLoaded"
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
  >
    <a-form-model-item label="是否启用防暴力破解" prop="enabled">
      <a-checkbox :checked="form.enabled" @change="handleChange" />
    </a-form-model-item>
    <a-form-model-item label="允许连续失败次数" prop="maxRetryCount">
      <a-input-number v-model="form.maxRetryCount" :min="1" />
    </a-form-model-item>
    <a-form-model-item label="自动解锁时间（分钟）" prop="maxRetryCount">
      <a-input-number v-model="form.lockTime" :min="1" />
    </a-form-model-item>
    <a-form-model-item label="是否通过IP锁定" prop="isLockedByIp">
      <a-checkbox :checked="form.isLockedByIp" @change="handleIsLockedByIpChange" />
    </a-form-model-item>
    <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
      <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
        保存
      </a-button>
    </a-form-model-item>
  </a-form-model>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'

const defaultConfigValue = {
  enabled: false,
  maxRetryCount: 5,
  lockTime: 10,
  isLockedByIp: true
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

    this.form = Object.assign({}, this.form, { ...configValue })

    this.configLoaded = true
  },
  methods: {
    handleChange(e) {
      this.$set(this.form, 'enabled', e.target.checked)
    },
    handleIsLockedByIpChange(e) {
      this.$set(this.form, 'isLockedByIp', e.target.checked)
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.configInfo.configValue = JSON.stringify({ ...this.form })
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
