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
        <a-form-model-item label="是否保留默认登录方式" prop="isReserveDefaultLogin">
          <a-checkbox :checked="form.isReserveDefaultLogin" @change="handleReserveDefaultLoginChange" />
        </a-form-model-item>
        <a-form-model-item label="CAS服务器访问地址" prop="casServerUrl">
          <a-input v-model="form.casServerUrl" placeholder="http://{host}:{port}/cas" />
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

const defaultConfigValue = {
  enabled: false,
  isReserveDefaultLogin: false,
  casServerUrl: '',
  // document.location.origin + window._CONFIG['domainURL']
  casServiceHostUrl: '',
  // document.location.origin
  casServiceWebUrl: ''
}

export default {
  name: 'CasConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      rules: {
        casServerUrl: [{ required: true, message: '请输入CAS服务器访问地址', trigger: 'blur' }],
        casServiceHostUrl: [{ required: true, message: '请输入CAS客户端服务访问地址', trigger: 'blur' }],
        casServiceWebUrl: [{ required: true, message: '请输入CAS客户端Web访问地址', trigger: 'blur' }]
      }
    }
  },
  async mounted() {
    const configInfoResult = await getConfigByKey('security.cas')
    this.configInfo = configInfoResult.data
    defaultConfigValue.casServiceHostUrl = document.location.origin + window._CONFIG['domainURL']
    defaultConfigValue.casServiceWebUrl = document.location.origin
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, { ...configValue })

    this.configLoaded = true
  },
  methods: {
    handleChange(e) {
      this.$set(this.form, 'enabled', e.target.checked)
    },
    handleReserveDefaultLoginChange(e) {
      this.$set(this.form, 'isReserveDefaultLogin', e.target.checked)
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.configInfo.configValue = JSON.stringify({ ...this.form })
          updateConfig(this.configInfo)
            .then(response => {
              // 更新store中cas信息
              this.$store.dispatch('GetCasInfo')
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
