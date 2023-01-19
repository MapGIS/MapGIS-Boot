<template>
  <a-modal :title="title" :visible="open" :confirm-loading="submitLoading" @ok="submitForm" @cancel="cancel">
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item :label="$t('username')" prop="userName">
        <a-input v-model="form.userName" :disabled="true" />
      </a-form-model-item>
      <a-form-model-item has-feedback :label="$t('password.new')" prop="newPassword">
        <a-input-password
          v-model="form.newPassword"
          :placeholder="$t('please.input.password.new')"
          :maxLength="16"
          autocomplete="new-password"
        />
      </a-form-model-item>
      <a-form-model-item has-feedback :label="$t('password.confirm')" prop="confirmPassword">
        <a-input-password
          v-model="form.confirmPassword"
          :placeholder="$t('please.password.confirm')"
          :maxLength="16"
          autocomplete="new-password"
        />
      </a-form-model-item>
    </a-form-model>
  </a-modal>
</template>
<script>
import { resetUserPwd } from '@/api/system/user'

export default {
  name: 'ResetPassword',
  props: {},
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
      title: this.$t('security.user.reset.password'),
      open: false,
      childrenDrawer: false,
      formLayout: 'horizontal',
      form: {
        userName: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      rules: {
        newPassword: [{ required: true, validator: validateNewPass, trigger: 'change' }],
        confirmPassword: [{ required: true, validator: validateConfirmPass, trigger: 'change' }]
      }
    }
  },
  methods: {
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        userName: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      }
    },
    handleResetPwd(row) {
      this.form = {
        userId: row.userId,
        userName: row.userName
      }
      this.open = true
    },
    /** 重置密码按钮操作 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          resetUserPwd(this.form.userId, this.form.newPassword)
            .then(response => {
              this.$message.success(this.$t('security.user.reset.success'), 3)
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
