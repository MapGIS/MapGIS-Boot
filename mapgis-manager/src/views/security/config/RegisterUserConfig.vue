<template>
  <a-form-model
    v-if="configLoaded"
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
  >
    <a-form-model-item :label="$t('security.config.whether.to.enable.register')" prop="enabled">
      <a-checkbox :checked="form.enabled" @change="handleChange" />
    </a-form-model-item>
    <a-form-model-item :label="$t('security.config.register.user.default.role')" prop="defaultRoleIds">
      <a-select mode="multiple" v-model="form.defaultRoleIds" :placeholder="$t('please.select')">
        <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
          {{ d.roleName }}
        </a-select-option>
      </a-select>
    </a-form-model-item>
    <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
      <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
        {{ $t('save') }}
      </a-button>
    </a-form-model-item>
  </a-form-model>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'
import { getUser } from '@/api/system/user'

const defaultConfigValue = {
  enabled: true,
  defaultRoleIds: []
}

export default {
  name: 'RegisterUserConfig',
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
    const userInfoResult = await getUser()
    this.roleOptions = userInfoResult.roles
    const configInfoResult = await getConfigByKey('security.register')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, { ...configValue })

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

<style></style>
