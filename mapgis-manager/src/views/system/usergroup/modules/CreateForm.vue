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
    <a-form-model ref="form" :model="form" :rules="rules" v-bind="formLayout">
      <a-form-model-item :label="$t('usergroup.name')" prop="userGroupName">
        <a-input
          v-model="form.userGroupName"
          :placeholder="$t('please.prefix.input', { content: $t('usergroup.name') })"
        />
      </a-form-model-item>
      <a-form-model-item :label="$t('member')" prop="userIds">
        <a-select
          mode="multiple"
          v-model="form.userIds"
          :placeholder="$t('please.prefix.select', { content: $t('security.usergroup.member') })"
        >
          <a-select-option v-for="user in userList" :key="user.userId" :value="user.userId">
            {{ user.userName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('security.usergroup.associated.roles')" prop="roleIds">
        <a-select
          mode="multiple"
          v-model="form.roleIds"
          :placeholder="$t('please.prefix.select', { content: $t('security.usergroup.usergroup.associated.roles') })"
        >
          <a-select-option v-for="role in roleList" :key="role.roleId" :value="role.roleId">
            {{ role.roleName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('description')" prop="remark">
        <a-input v-model="form.remark" :placeholder="$t('please.input.content')" type="textarea" allow-clear />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('ok') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { listUser } from '@/api/system/user'
import { listRole } from '@/api/system/role'
import { getUsergroup, addUsergroup, updateUsergroup } from '@/api/system/usergroup'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {},
  components: {},
  data() {
    return {
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        userGroupId: null,
        userGroupName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        roleIds: [],
        userIds: []
      },
      userList: [],
      roleList: [],
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        userGroupName: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('usergroup.name') }),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {},
  created() {
    this.getUserList()
    this.getRoleList()
  },
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询用户列表 */
    getUserList() {
      const queryParam = {
        pageNum: 1,
        pageSize: 1000
      }
      const that = this
      listUser(queryParam).then(response => {
        that.userList = response.rows
      })
    },
    /** 查询角色列表 */
    getRoleList() {
      const queryParam = {
        pageNum: 1,
        pageSize: 1000
      }
      const that = this
      listRole(queryParam).then(response => {
        that.roleList = response.rows
      })
    },
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
      this.formType = 1
      this.form = {
        userGroupId: null,
        userGroupName: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        roleIds: [],
        userIds: []
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.formType = 1
      this.open = true
      this.formTitle = this.$t('add')
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      this.formType = 2
      const userGroupId = row ? row.userGroupId : ids
      getUsergroup(userGroupId).then(response => {
        this.form = response.data
        this.form.roleIds = []
        this.form.roles.forEach(role => {
          this.form.roleIds.push(role.roleId)
        })
        this.form.userIds = []
        this.form.users.forEach(user => {
          this.form.userIds.push(user.userId)
        })
        this.open = true
        this.formTitle = this.$t('modify')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.userGroupId !== undefined && this.form.userGroupId !== null) {
            const modifyMessage = this.$t('modify.success')
            updateUsergroup(this.form)
              .then(response => {
                this.$message.success(modifyMessage, 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            const addMessage = this.$t('add.success')
            addUsergroup(this.form)
              .then(response => {
                this.$message.success(addMessage, 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>
