<template>
  <div>
    <a-form-model v-if="configLoaded" ref="form" :model="form" layout="vertical">
      <a-form-model-item label="日志级别" prop="systemLoglevel">
        <a-select v-model="form.systemLoglevel" placeholder="请选择">
          <a-select-option v-for="(l, index) in levelOptions" :key="index" :value="l.value">
            {{ l.label }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item style="margin-bottom: 0">
        <a-button
          type="primary"
          :loading="submitLoading"
          @click="submit"
          v-hasPermi="['system:systemlog:config']"
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
import { getConfig, updateConfig } from '@/api/system/systemlog'

const defaultConfigValue = {
  systemLoglevel: 'INFO'
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
      ]
    }
  },
  async mounted() {
    const configInfoResult = await getConfig('log')
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

      this.$emit('ok')
    }
  }
}
</script>

<style></style>
