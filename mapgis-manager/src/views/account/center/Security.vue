<template>
  <a-form-model
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
    :rules="rules"
  >
    <a-form-model-item has-feedback label="旧密码" prop="oldPassword">
      <a-input-password v-model="form.oldPassword" placeholder="请输入旧密码" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item has-feedback label="新密码" prop="newPassword">
      <a-input-password v-model="form.newPassword" placeholder="请输入新密码" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item has-feedback label="确认密码" prop="confirmPassword">
      <a-input-password v-model="form.confirmPassword" placeholder="请确认密码" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
      <a-button type="primary" :loading="submitLoading" @click="submit">保存</a-button>
    </a-form-model-item>
  </a-form-model>
</template>

<script>
import { updateUserPwd } from '@/api/system/user'

export default {
  name: 'SecuritySettings',
  data() {
    const validateNewPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else if (
        !/((^(?=.*[a-z])(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[\da-zA-Z\W]{8,16}$))/.test(
          value
        )
      ) {
        callback(new Error('请输入8-16位字符，至少包含数字、大写字母、小写字母和特殊字符中的三种类型'))
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码确认'))
      } else if (value !== this.form.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      submitLoading: false,
      form: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      rules: {
        oldPassword: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
        newPassword: [{ required: true, validator: validateNewPass, trigger: 'change' }],
        confirmPassword: [{ required: true, validator: validateConfirmPass, trigger: 'change' }]
      }
    }
  },
  methods: {
    /** 重置密码按钮操作 */
    submit: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          updateUserPwd(this.form.oldPassword, this.form.newPassword)
            .then(response => {
              this.$message.success('修改成功', 3)
              this.open = false
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

<style scoped></style>
