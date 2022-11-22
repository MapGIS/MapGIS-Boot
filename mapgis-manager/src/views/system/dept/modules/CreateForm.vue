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
      <a-form-model-item label="上级部门" prop="parentId">
        <a-tree-select
          v-model="form.parentId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="deptOptions"
          placeholder="请选择"
          :replaceFields="{
            children: 'children',
            title: 'deptName',
            key: 'deptId',
            value: 'deptId'
          }"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item>
      <a-form-model-item label="部门名称" prop="deptName">
        <a-input v-model="form.deptName" placeholder="请输入" />
      </a-form-model-item>
      <a-form-model-item label="排序" prop="orderNum">
        <a-input-number v-model="form.orderNum" :min="0" style="width: 100%" />
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
import { getDept, addDept, updateDept } from '@/api/system/dept'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    deptOptions: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: 0
      },
      open: false,
      rules: {
        parentId: [{ required: true, message: '上级部门不能为空', trigger: 'blur' }],
        deptName: [{ required: true, message: '部门名称不能为空', trigger: 'blur' }],
        orderNum: [{ required: true, message: '排序不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
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
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: 0
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row !== undefined) {
        this.form.parentId = row.deptId
      }
      this.open = true
      this.formTitle = '添加部门'
      this.$emit('select-tree')
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const deptId = row.deptId
      getDept(deptId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改部门'
      })
      this.$emit('select-tree', row)
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.deptId !== undefined) {
            updateDept(this.form)
              .then(response => {
                this.$message.success('修改成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            addDept(this.form)
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
