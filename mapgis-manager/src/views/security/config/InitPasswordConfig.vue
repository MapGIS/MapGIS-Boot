<template>
  <div>
    <a-form-model
      :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
      :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
      ref="form"
      :model="form"
      :rules="rules"
    >
      <a-form-model-item label="初始密码" prop="configValue">
        <a-input-password v-model="form.configValue" placeholder="请确认密码" :maxLength="16" />
      </a-form-model-item>
      <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
        <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
          保存
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
import { getConfigByKey, updateConfig } from '@/api/system/config'

export default {
  name: 'InitPasswordConfig',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (
        !/((^(?=.*[a-z])(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[\da-zA-Z\W]{8,16}$))/.test(
          value
        )
      ) {
        callback(new Error('请输入8-16位字符，至少包含数字、大写字母、小写字母和特殊字符中的三种类型'))
      } else {
        callback()
      }
    }
    return {
      submitLoading: false,
      form: {
        configValue: ''
      },
      rules: {
        configValue: [{ required: true, validator: validatePass, trigger: 'change' }]
      }
    }
  },
  mounted() {
    getConfigByKey('security.initPassword').then(response => {
      this.form = response.data
    })
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          updateConfig(this.form)
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
