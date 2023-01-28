<template>
  <a-form-model
    v-if="configLoaded"
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
    :rules="rules"
  >
    <a-form-model-item :label="$t('whether.to.enable')" prop="enabled">
      <a-checkbox :checked="form.enabled" @change="handleChange" />
    </a-form-model-item>
    <a-form-model-item label="LDAP服务器地址" prop="url">
      <a-input v-model="form.url" placeholder="ldap(s)://{host}:{port}">
        <a-tooltip slot="suffix" title="LDAP默认端口为389，LDAPS默认端口为636">
          <a-icon type="info-circle" />
        </a-tooltip>
      </a-input>
    </a-form-model-item>
    <a-form-model-item label="根条目位置" prop="base">
      <a-input v-model="form.base" />
    </a-form-model-item>
    <a-form-model-item label="LDAP管理员名称" prop="userDn">
      <a-input v-model="form.userDn" />
    </a-form-model-item>
    <a-form-model-item label="LDAP管理员密码" prop="password">
      <a-input-password v-model="form.password" :visibilityToggle="false" autocomplete="new-password" />
    </a-form-model-item>
    <!-- 暂时不提供创建用户的默认用户组配置，默认就是第三方用户组（useGroupId为1） -->
    <a-form-model-item :label="$t('security.usergroup.default.user.group')" prop="defaultUserGroupIds" v-if="false">
      <a-select mode="multiple" v-model="form.defaultUserGroupIds" :placeholder="$t('please.select')">
        <a-select-option v-for="(d, index) in userGroupOptions" :key="index" :value="d.userGroupId">
          {{ d.userGroupName }}
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
import cloneDeep from 'lodash.clonedeep'
import { getConfigByKey, updateConfig } from '@/api/system/config'
import { getUser } from '@/api/system/user'

const defaultConfigValue = {
  enabled: false,
  url: '',
  base: '',
  userDn: '',
  password: '',
  defaultUserGroupIds: [],
  roleMapping: []
}

export default {
  name: 'LdapBaseConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      rules: {
        url: [{ required: true, message: '请输入LDAP服务器地址', trigger: 'blur' }],
        base: [{ required: true, message: '请输入根条目位置', trigger: 'blur' }],
        userDn: [{ required: true, message: '请输入LDAP管理员名称', trigger: 'blur' }]
      }
    }
  },
  async mounted() {
    const userInfoResult = await getUser()
    this.userGroupOptions = userInfoResult.userGroups
    const configInfoResult = await getConfigByKey('security.ldap')
    this.configInfo = configInfoResult.data
    const tempDefaultConfigValue = cloneDeep(defaultConfigValue)
    const configValue = merge(
      tempDefaultConfigValue,
      this.configInfo && JSON.parse(this.configInfo.configValue || '{}')
    )

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
