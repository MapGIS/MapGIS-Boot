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
      <a-form-model-item label="用户组名称" prop="userGroupName">
        <a-input v-model="form.userGroupName" placeholder="请输入用户组名称" />
      </a-form-model-item>
      <a-form-model-item label="成员" prop="userIds">
        <a-select mode="multiple" v-model="form.userIds" placeholder="请选择用户组成员">
          <a-select-option v-for="user in userList" :key="user.userId" :value="user.userId">
            {{ user.userName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="关联角色" prop="roleIds">
        <a-select mode="multiple" v-model="form.roleIds" placeholder="请选择用户组关联角色">
          <a-select-option v-for="role in roleList" :key="role.roleId" :value="role.roleId">
            {{ role.roleName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="描述" prop="remark">
        <a-input v-model="form.remark" placeholder="请输入内容" type="textarea" allow-clear />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm"> 保存 </a-button>
          <a-button type="dashed" @click="cancel"> 取消 </a-button>
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
        userGroupName: [{ required: true, message: '用户组名称不能为空', trigger: 'blur' }]
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
      this.formTitle = '添加'
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
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.userGroupId !== undefined && this.form.userGroupId !== null) {
            updateUsergroup(this.form)
              .then(response => {
                this.$message.success('修改成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            addUsergroup(this.form)
              .then(response => {
                this.$message.success('新增成功', 3)
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