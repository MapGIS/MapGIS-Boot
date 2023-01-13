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
      <a-form-model-item label="岗位名称" prop="postName">
        <a-input v-model="form.postName" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item label="岗位编码" prop="postCode">
        <a-input v-model="form.postCode" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item label="显示顺序" prop="postSort">
        <a-input-number v-model="form.postSort" :min="0" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item label="备注" prop="remark">
        <a-input v-model="form.remark" :placeholder="$t('please.input')" type="textarea" allow-clear />
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('save') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { getPost, addPost, updatePost } from '@/api/system/post'
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
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        remark: undefined
      },
      open: false,
      rules: {
        postName: [{ required: true, message: '岗位编码不能为空', trigger: 'blur' }],
        postCode: [{ required: true, message: '岗位名称不能为空', trigger: 'blur' }],
        postSort: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }]
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
        postId: undefined,
        postCode: undefined,
        postName: undefined,
        postSort: 0,
        remark: undefined
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.formTitle = '添加岗位'
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      const postId = row ? row.postId : ids
      getPost(postId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改岗位'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.postId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updatePost(this.form)
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
            addPost(this.form)
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
