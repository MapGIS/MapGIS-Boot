<template>
  <a-form-model
    :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
    :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
    ref="form"
    :model="form"
    :rules="rules"
  >
    <a-form-model-item has-feedback :label="$t('password.old')" prop="oldPassword">
      <a-input-password v-model="form.oldPassword" :placeholder="$t('please.input.password.old')" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item has-feedback :label="$t('password.new')" prop="newPassword">
      <a-input-password v-model="form.newPassword" :placeholder="$t('please.input.password.new')" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item has-feedback :label="$t('password.confirm')" prop="confirmPassword">
      <a-input-password v-model="form.confirmPassword" :placeholder="$t('please.password.confirm')" :maxLength="16" />
    </a-form-model-item>
    <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
      <a-button type="primary" :loading="submitLoading" @click="submit" icon="save">{{ $t('save') }}</a-button>
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
        callback(new Error(this.$t('please.input.password.new')))
      } else if (
        !/((^(?=.*[a-z])(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[A-Z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*\W)[\da-zA-Z\W]{8,16}$)|(^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[\da-zA-Z\W]{8,16}$))/.test(
          value
        )
      ) {
        callback(new Error(this.$t('password.length.limit')))
      } else {
        if (this.form.confirmPassword !== '') {
          this.$refs.form.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('please.input.password.new.again.confirm')))
      } else if (value !== this.form.newPassword) {
        callback(new Error(this.$t('password.inconsistent')))
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
        oldPassword: [
          { required: true, message: this.$t('not.empty.suffix', { content: this.$t('password') }), trigger: 'blur' }
        ],
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
          const modifyMessage = this.$t('modify.success')
          updateUserPwd(this.form.oldPassword, this.form.newPassword)
            .then(response => {
              this.$message.success(modifyMessage, 3)
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
