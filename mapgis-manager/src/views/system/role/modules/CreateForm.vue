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
      <a-form-model-item :label="$t('role.name')" prop="roleName">
        <a-input v-model="form.roleName" :placeholder="$t('please.input')" :disabled="updateDisable" />
      </a-form-model-item>
      <a-form-model-item prop="roleKey">
        <span slot="label">
          {{ $t('security.role.role.identification') }}
          <a-tooltip>
            <template slot="title">
              {{ $t('security.role.role.define.description', { eg: '@PreAuthorize("@ss.hasRole(\'admin\')")' }) }}
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.roleKey" :placeholder="$t('please.input')" :disabled="updateDisable" />
      </a-form-model-item>
      <a-form-model-item :label="$t('order')" prop="roleSort">
        <a-input-number
          :placeholder="$t('please.input')"
          v-model="form.roleSort"
          :min="0"
          style="width: 100%"
          :disabled="updateDisable"
        />
      </a-form-model-item>
      <a-form-model-item :label="$t('security.role.role.description')" prop="remark">
        <a-input
          v-model="form.remark"
          :placeholder="$t('please.input')"
          type="textarea"
          allow-clear
          :disabled="updateDisable"
        />
      </a-form-model-item>
      <a-form-model-item :label="$t('security.role.menu.permission')">
        <a-checkbox @change="handleCheckedTreeExpand($event)" :disabled="updateDisable">
          {{ $t('security.role.expand.collapse') }}
        </a-checkbox>
        <a-checkbox @change="handleCheckedTreeNodeAll($event)" :disabled="updateDisable">
          {{ $t('security.role.select.all.none') }}
        </a-checkbox>
        <a-checkbox
          @change="handleCheckedTreeConnect($event)"
          :checked="form.menuCheckStrictly"
          :disabled="updateDisable"
        >
          {{ $t('security.role.parent.child.linkage') }}
        </a-checkbox>
        <a-tree
          v-model="menuCheckedKeys"
          checkable
          :checkStrictly="!form.menuCheckStrictly"
          :expanded-keys="menuExpandedKeys"
          :auto-expand-parent="autoExpandParent"
          :tree-data="menuOptions"
          @check="onCheck"
          @expand="onExpandMenu"
          :replaceFields="defaultProps"
          :disabled="updateDisable"
        />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm" :disabled="updateDisable">
            {{ $t('ok') }}
          </a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { getRole, addRole, updateRole } from '@/api/system/role'
import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {},
  components: {},
  data() {
    return {
      submitLoading: false,
      menuExpandedKeys: [],
      autoExpandParent: false,
      menuCheckedKeys: [],
      halfCheckedKeys: [],
      menuOptions: [],
      formTitle: '',
      // 表单参数
      form: {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        menuIds: [],
        menuCheckStrictly: true,
        remark: undefined
      },
      open: false,
      menuExpand: false,
      menuNodeAll: false,
      rules: {
        roleName: [
          { required: true, message: this.$t('not.empty.suffix', { content: this.$t('role.name') }), trigger: 'blur' }
        ],
        roleKey: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('security.role.role.identification') }),
            trigger: 'blur'
          }
        ],
        roleSort: [
          { required: true, message: this.$t('not.empty.suffix', { content: this.$t('order') }), trigger: 'blur' }
        ]
      },
      defaultProps: {
        children: 'children',
        title: 'label',
        key: 'id'
      }
    }
  },
  filters: {},
  created() {},
  computed: {
    updateDisable() {
      return this.form.isSys !== undefined && this.form.isSys === 1
    }
  },
  watch: {},
  methods: {
    onExpandMenu(expandedKeys) {
      this.menuExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect({ visible: '0', status: '0' }).then(response => {
        this.menuOptions = response.data
      })
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 全选与半选
      return this.menuCheckedKeys.concat(this.halfCheckedKeys)
    },
    getAllMenuNode(nodes) {
      if (!nodes || nodes.length === 0) {
        return []
      }
      nodes.forEach(node => {
        this.menuCheckedKeys.push(node.id)
        return this.getAllMenuNode(node.children)
      })
    },
    // 回显过滤
    selectNodefilter(nodes, parentIds) {
      if (!nodes || nodes.length === 0) {
        return
      }
      nodes.forEach(node => {
        // 父子关联模式且当前元素有父级
        const currentIndex = this.menuCheckedKeys.indexOf(node.id)
        // 当前节点存在,且父节点不存在，则说明父节点应是半选中状态
        // parentIds没有数据的时候认为是顶级菜单，不用给半选中状态
        if (currentIndex > -1 && parentIds && parentIds.length > 0) {
          parentIds.forEach(parentId => {
            if (this.halfCheckedKeys.indexOf(parentId) === -1) {
              this.halfCheckedKeys.push(parentId)
            }
          })
          parentIds = []
        }
        // 防重
        const isExist = this.halfCheckedKeys.indexOf(node.id)
        const isExistParentIds = parentIds.indexOf(node.id)
        const newParentIds = [...parentIds]
        if (isExist === -1 && isExistParentIds === -1 && currentIndex === -1) {
          newParentIds.push(node.id)
        }
        this.selectNodefilter(node.children, parentIds)
      })
    },
    handleCheckedTreeNodeAll(value) {
      if (value.target.checked) {
        this.menuCheckedKeys = []
        this.getAllMenuNode(this.menuOptions)
      } else {
        this.menuCheckedKeys = []
        this.halfCheckedKeys = []
      }
    },
    handleCheckedTreeExpand(value) {
      if (value.target.checked) {
        const treeList = this.menuOptions
        for (let i = 0; i < treeList.length; i++) {
          this.menuExpandedKeys.push(treeList[i].id)
        }
      } else {
        this.menuExpandedKeys = []
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value) {
      this.form.menuCheckStrictly = !this.form.menuCheckStrictly
    },
    /** 根据角色ID查询菜单树结构 */
    getRoleMenuTreeselect(roleId) {
      return roleMenuTreeselect(roleId).then(response => {
        this.menuOptions = response.menus
        return response
      })
    },
    onCheck(checkedKeys, info) {
      if (!this.form.menuCheckStrictly) {
        let currentCheckedKeys = []
        if (this.menuCheckedKeys.checked) {
          currentCheckedKeys = currentCheckedKeys.concat(this.menuCheckedKeys.checked)
        }
        if (this.menuCheckedKeys.halfChecked) {
          currentCheckedKeys = currentCheckedKeys.concat(this.menuCheckedKeys.halfChecked)
        }
        this.menuCheckedKeys = currentCheckedKeys
      } else {
        // 半选节点
        this.halfCheckedKeys = info.halfCheckedKeys
        this.menuCheckedKeys = checkedKeys
      }
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
      if (this.$refs.menu !== undefined) {
        this.menuCheckedKeys = []
        this.halfCheckedKeys = []
      }
      this.menuExpand = false
      this.menuNodeAll = false
      this.menuExpandedKeys = []
      this.autoExpandParent = false
      this.menuCheckedKeys = []
      this.halfCheckedKeys = []
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleKey: undefined,
        roleSort: 0,
        menuIds: [],
        menuCheckStrictly: true,
        remark: undefined
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.getMenuTreeselect()
      this.open = true
      this.formTitle = this.$t('add.suffix', { content: this.$t('role') })
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      const roleId = row ? row.roleId : ids
      const roleMenu = this.getRoleMenuTreeselect(roleId)
      getRole(roleId).then(response => {
        this.form = response.data
        this.open = true
        this.$nextTick(() => {
          roleMenu.then(res => {
            if (roleId === 1) {
              this.menuCheckedKeys = []
              this.getAllMenuNode(this.menuOptions)
            } else {
              this.menuCheckedKeys = res.checkedKeys
              // 过滤回显时的半选中node(父)
              if (this.form.menuCheckStrictly) {
                this.selectNodefilter(this.menuOptions, [])
              }
            }
          })
        })
        this.formTitle = this.$t('modify.suffix', { content: this.$t('role') })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.roleId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            this.form.menuIds = this.getMenuAllCheckedKeys()
            updateRole(this.form)
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
            this.form.menuIds = this.getMenuAllCheckedKeys()
            addRole(this.form)
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
