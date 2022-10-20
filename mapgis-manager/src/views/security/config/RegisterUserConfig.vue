<template>
  <div>
    <a-form-model v-if="configLoaded" :label-col="{ span: 4 }" :wrapper-col="{ span: 12 }" ref="form" :model="form">
      <a-form-model-item label="是否启用注册功能" prop="enabled">
        <a-checkbox :checked="form.enabled" @change="handleChange" />
      </a-form-model-item>
      <a-form-model-item label="注册用户的默认角色" prop="roleIds">
        <a-select mode="multiple" v-model="form.roleIds" placeholder="请选择">
          <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
            {{ d.roleName }}
          </a-select-option>
        </a-select>
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

    this.form = Object.assign({}, this.form, {
      enabled: configValue.enabled,
      roleIds: configValue.defaultRoleIds
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
            defaultRoleIds: this.form.roleIds
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
