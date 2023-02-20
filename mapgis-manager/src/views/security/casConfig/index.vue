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
        <a-form-model-item :label="$t('whether.to.enable')" prop="enabled">
          <a-checkbox :checked="form.enabled" @change="handleChange" />
        </a-form-model-item>
        <a-form-model-item :label="$t('security.cas.whether.to.keep.default.login')" prop="isReserveDefaultLogin">
          <a-checkbox :checked="form.isReserveDefaultLogin" @change="handleReserveDefaultLoginChange" />
        </a-form-model-item>
        <a-form-model-item :label="$t('security.cas.server.url')" prop="casServerUrl">
          <a-input v-model="form.casServerUrl" placeholder="http://{host}:{port}/cas" />
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
          <a-button
            type="primary"
            :loading="submitLoading"
            @click="submit"
            v-hasPermi="['system:config:edit']"
            icon="save"
          >
            {{ $t('save') }}
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
        casServerUrl: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('security.cas.server.url') }),
            trigger: 'blur'
          }
        ],
        casServiceHostUrl: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('security.cas.service.host.url') }),
            trigger: 'blur'
          }
        ],
        casServiceWebUrl: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('security.cas.service.web.url') }),
            trigger: 'blur'
          }
        ]
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
          const settingMessage = this.$t('setting.success')
          updateConfig(this.configInfo)
            .then(response => {
              // 更新store中cas信息
              this.$store.dispatch('GetCasInfo')
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
