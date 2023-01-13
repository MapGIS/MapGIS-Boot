<template>
  <page-header-wrapper
    ><a-card>
      <a-form-model
        v-if="configLoaded"
        :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
        :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
        ref="form"
        :model="form"
      >
        <a-divider orientation="left">系统日志</a-divider>
        <a-form-model-item label="日志级别" prop="systemLoglevel">
          <a-select v-model="form.systemLoglevel" :placeholder="$t('please.select')">
            <a-select-option v-for="(l, index) in levelOptions" :key="index" :value="l.value">
              {{ l.label }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-divider orientation="left">服务访问日志</a-divider>
        <a-form-model-item label="是否启用" prop="httpAccessEnabled">
          <a-checkbox :checked="form.httpAccessEnabled" @change="handleChange" />
        </a-form-model-item>
        <a-form-model-item label="监控的服务访问URL列表" prop="httpAccessMonitorUrls">
          <a-layout class="monitor-layout">
            <a-layout-content style="padding: 6px 10px 10px 10px">
              <a-tag
                v-for="tag in form.httpAccessMonitorUrls"
                :key="tag"
                color="blue"
                class="monitor-tag"
                closable
                @close="handleClose(tag)"
              >
                {{ tag }}
              </a-tag>
              <a-input
                v-if="inputVisible"
                ref="saveTagInput"
                v-model="inputValue"
                class="input-new-tag"
                size="small"
                @change="validateInput"
                @keyup.enter.native="handleInputConfirm"
                @blur="handleInputConfirm"
              />
              <a-button v-else class="button-new-tag" size="small" @click="showInput">
                {{ addMonitorUrlTip }}
              </a-button>
              <span v-if="showError" class="error-msg">监控URL输入不合法</span>
            </a-layout-content>
          </a-layout>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
          <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
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
  systemLoglevel: 'INFO',
  httpAccessEnabled: false,
  httpAccessMonitorUrls: []
}

export default {
  name: 'LogConfig',
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      levelOptions: [
        {
          label: 'ALL',
          value: 'ALL'
        },
        {
          label: 'DEBUG',
          value: 'DEBUG'
        },
        {
          label: 'INFO',
          value: 'INFO'
        },
        {
          label: 'WARN',
          value: 'WARN'
        },
        {
          label: 'ERROR',
          value: 'ERROR'
        },
        {
          label: 'OFF',
          value: 'OFF'
        }
      ],
      inputVisible: false,
      inputValue: '',
      urlRegex: /^(\/?[a-zA-Z0-9_\-*]+)+$/,
      showError: false
    }
  },
  computed: {
    addMonitorUrlTip() {
      return `新增URL（示例：/${window._CONFIG['productName']}/*,/${window._CONFIG['productName']}/**）`
    }
  },
  async mounted() {
    const configInfoResult = await getConfigByKey('log')
    this.configInfo = configInfoResult.data
    const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))

    this.form = Object.assign({}, this.form, { ...configValue })

    this.configLoaded = true
  },
  methods: {
    handleChange(e) {
      this.$set(this.form, 'httpAccessEnabled', e.target.checked)
    },
    handleClose(url) {
      this.form.httpAccessMonitorUrls.splice(this.form.httpAccessMonitorUrls.indexOf(url), 1)
    },
    validateInput() {
      const valid = this.inputValue === '' || this.urlRegex.test(this.inputValue)
      this.showError = !valid
      return valid
    },
    handleInputConfirm() {
      const valid = this.validateInput()
      if (!valid) {
        return
      }
      const inputValue = this.inputValue
      if (inputValue) {
        this.form.httpAccessMonitorUrls.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    showInput() {
      this.inputVisible = true
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
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

      this.$emit('ok')
    }
  }
}
</script>

<style lang="less" scoped>
.monitor-layout {
  background-color: @base-bg-color;
  border: 1px solid @border-color;
  height: 160px;
  .monitor-tag + .monitor-tag {
    margin-left: 5px;
  }
  .monitor-tag + .button-new-tag {
    margin-left: 5px;
  }
  .button-new-tag {
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 5px;
    margin-bottom: 5px;
    vertical-align: bottom;
  }
  span.error-msg {
    margin-left: 10px;
    color: red;
    font-size: 14px;
  }
}
</style>
