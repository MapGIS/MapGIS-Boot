<template>
  <div>
    <a-form-model v-if="configLoaded" ref="form" :model="form" layout="vertical">
      <a-form-model-item label="创建用户的默认角色" prop="defaultRoleIds">
        <a-select mode="multiple" v-model="form.defaultRoleIds" placeholder="请选择">
          <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
            {{ d.roleName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item style="margin-bottom: 0">
        <a-button
          type="primary"
          :loading="submitLoading"
          @click="submit"
          v-hasPermi="['system:config:edit']"
          style="float: right"
        >
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
  defaultRoleIds: []
}

export default {
  name: 'ThirdUserConfig',
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
    const configInfoResult = await getConfigByKey('security.oauth')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, { ...configValue })

    this.configLoaded = true
  },
  methods: {
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
