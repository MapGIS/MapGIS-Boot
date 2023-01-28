<template>
  <div>
    <a-form-model v-if="configLoaded" ref="form" :model="form" layout="vertical">
      <a-form-model-item :label="$t('security.usergroup.default.user.group')" prop="defaultUserGroupIds">
        <a-select mode="multiple" v-model="form.defaultUserGroupIds" :placeholder="$t('please.select')">
          <a-select-option v-for="(d, index) in userGroupOptions" :key="index" :value="d.userGroupId">
            {{ d.userGroupName }}
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
          {{ $t('save') }}
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
  defaultUserGroupIds: []
}

export default {
  name: 'ThirdUserConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      userGroupOptions: []
    }
  },
  async mounted() {
    const userInfoResult = await getUser()
    this.userGroupOptions = userInfoResult.userGroups
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
