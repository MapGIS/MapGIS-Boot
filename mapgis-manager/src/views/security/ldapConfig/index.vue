<template>
  <page-header-wrapper>
    <a-card>
      <a-form-model
        v-if="configLoaded"
        :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
        :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
        ref="form"
        :model="form"
        :rules="rules"
      >
        <a-form-model-item label="是否启用" prop="enabled">
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
          <a-input-password v-model="form.password" :visibilityToggle="false" />
        </a-form-model-item>
        <a-form-model-item label="创建用户的默认角色" prop="defaultRoleIds">
          <a-select mode="multiple" v-model="form.defaultRoleIds" placeholder="请选择">
            <a-select-option v-for="(d, index) in roleOptions" :key="index" :value="d.roleId">
              {{ d.roleName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
          <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
            保存
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'
import { getUser } from '@/api/system/user'

const defaultConfigValue = {
  enabled: false,
  url: '',
  base: '',
  userDn: '',
  password: '',
  defaultRoleIds: []
}

export default {
  name: 'LdapConfig',
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
    this.roleOptions = userInfoResult.roles
    const configInfoResult = await getConfigByKey('security.ldap')
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
