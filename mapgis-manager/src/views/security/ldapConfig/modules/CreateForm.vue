<template>
  <pop-dialog
    :mode="formMode"
    :title="formTitle"
    width="35%"
    :visible="open"
    :loading="submitLoading"
    @ok="submitForm"
    @cancel="onClose"
  >
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item v-if="isAdd && ldapErrorMsg">
        <a-alert type="error" :message="ldapErrorMsg" banner />
      </a-form-model-item>
      <a-form-model-item :label="$t('security.ldap.group')" prop="externalRole">
        <a-select v-model="form.externalRole" :placeholder="$t('please.select')" :disabled="!isAdd">
          <a-select-option v-for="(role, index) in validLdapRoles" :key="index" :value="role">
            {{ role }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('mapping.role')" prop="systemRoleIds">
        <a-transfer
          :data-source="systemRoles"
          :titles="[$t('optional'), $t('selected')]"
          :target-keys="targetKeys"
          :render="item => item.roleName"
          @change="handleRoleChange"
        />
      </a-form-model-item>
      <!-- <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('ok') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div> -->
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { ldapRoles } from '@/api/system/security'
import { formMixin } from '@/store/form-mixin'
export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    systemRoles: {
      type: Array,
      default() {
        return []
      }
    }
  },
  components: {},
  data() {
    return {
      submitLoading: false,
      ldapRoles: [],
      existedExternalRoles: [],
      formTitle: '',
      // 表单参数
      form: {
        externalRole: undefined,
        systemRoleIds: []
      },
      open: false,
      rules: {
        externalRole: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('security.ldap.group') }),
            trigger: 'blur'
          }
        ],
        systemRoleIds: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('selected.roles') }),
            trigger: 'blur'
          }
        ]
      },
      isAdd: true,
      targetKeys: [],
      ldapErrorMsg: ''
    }
  },
  computed: {
    validLdapRoles() {
      return this.ldapRoles.filter(role => {
        return !this.existedExternalRoles.includes(role)
      })
    }
  },
  mounted() {
    ldapRoles().then(response => {
      this.ldapRoles = response.data
      if (this.ldapRoles.length === 0) {
        this.ldapErrorMsg = this.$t('security.ldap.connect.tip')
      }
    })
  },
  methods: {
    onClose() {
      this.open = false
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        externalRole: undefined,
        systemRoleIds: []
      }
      this.targetKeys = []
    },
    /** 新增按钮操作 */
    handleAdd(existedExternalRoles) {
      this.reset()
      this.open = true
      this.formTitle = this.$t('add.suffix', { content: this.$t('role.mapping.config') })
      this.existedExternalRoles = existedExternalRoles
      this.isAdd = true
    },
    /** 修改按钮操作 */
    handleUpdate(roleMappingItem, existedExternalRoles) {
      this.reset()
      this.form = { ...roleMappingItem }
      this.targetKeys = this.form.systemRoleIds.map(roleId => roleId.toString())
      this.open = true
      this.formTitle = this.$t('modify.suffix', { content: this.$t('role.mapping.config') })
      this.existedExternalRoles = existedExternalRoles
      this.isAdd = false
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isAdd) {
            const modifyMessage = this.$t('modify.success')
            this.$emit('add', this.form, () => {
              this.$message.success(modifyMessage, 3)
              this.open = false
              this.$emit('ok')
            })
          } else {
            const addMessage = this.$t('add.success')
            this.$emit('update', this.form, () => {
              this.$message.success(addMessage, 3)
              this.open = false
              this.$emit('ok')
            })
          }
        } else {
          return false
        }
      })
    },
    handleRoleChange(nextTargetKeys, direction, moveKeys) {
      this.targetKeys = nextTargetKeys
      this.form.systemRoleIds = this.targetKeys.map(key => {
        return parseInt(key)
      })
    }
  }
}
</script>
