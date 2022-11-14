<template>
  <a-drawer width="35%" :visible="open" @close="onClose">
    <a-divider orientation="left">
      <b>{{ formTitle }}</b>
    </a-divider>
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item v-if="isAdd && ldapErrorMsg">
        <a-alert type="error" :message="ldapErrorMsg" banner />
      </a-form-model-item>
      <a-form-model-item label="LDAP群组" prop="externalRole">
        <a-select v-model="form.externalRole" placeholder="请选择" :disabled="!isAdd">
          <a-select-option v-for="(role, index) in validLdapRoles" :key="index" :value="role">
            {{ role }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="映射角色" prop="systemRoleIds">
        <a-transfer
          :data-source="systemRoles"
          :titles="['供选择的角色', '选中的角色']"
          :target-keys="targetKeys"
          :render="item => item.roleName"
          @change="handleRoleChange"
        />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" @click="submitForm"> 保存 </a-button>
          <a-button type="dashed" @click="cancel"> 取消 </a-button>
        </a-space>
      </div>
    </a-form-model>
  </a-drawer>
</template>

<script>
import { ldapRoles } from '@/api/system/security'

export default {
  name: 'CreateForm',
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
        externalRole: [{ required: true, message: 'LDAP群组不能为空', trigger: 'blur' }],
        systemRoleIds: [{ required: true, message: '选中的角色不能为空', trigger: 'blur' }]
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
        this.ldapErrorMsg = '请确保LDAP登录配置可用!'
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
      this.formTitle = '添加角色映射配置'
      this.existedExternalRoles = existedExternalRoles
      this.isAdd = true
    },
    /** 修改按钮操作 */
    handleUpdate(roleMappingItem, existedExternalRoles) {
      this.reset()
      this.form = { ...roleMappingItem }
      this.targetKeys = this.form.systemRoleIds.map(roleId => roleId.toString())
      this.open = true
      this.formTitle = '修改角色映射配置'
      this.existedExternalRoles = existedExternalRoles
      this.isAdd = false
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.isAdd) {
            this.$emit('add', this.form, () => {
              this.$message.success('修改成功', 3)
              this.open = false
              this.$emit('ok')
            })
          } else {
            this.$emit('update', this.form, () => {
              this.$message.success('新增成功', 3)
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
