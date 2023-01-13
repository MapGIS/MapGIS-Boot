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
      <a-form-model-item label="微应用名称" prop="name">
        <a-input v-model="form.name" placeholder="请输入微应用名称" />
      </a-form-model-item>
      <a-form-model-item label="微应用入口" prop="entry">
        <a-input v-model="form.entry" placeholder="请输入微应用入口" />
      </a-form-model-item>
      <a-form-model-item label="微应用路由" prop="activeRule">
        <a-input v-model="form.activeRule" placeholder="请输入微应用路由" />
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
import { getMicroApp, addMicroApp, updateMicroApp } from '@/api/system/microApp'
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
        microAppId: null,

        name: null,

        entry: null,

        activeRule: null,

        createBy: null,

        createTime: null,

        updateBy: null,

        updateTime: null
      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        name: [{ required: true, message: '微应用名称不能为空', trigger: 'blur' }],

        entry: [{ required: true, message: '微应用入口不能为空', trigger: 'blur' }],

        activeRule: [{ required: true, message: '微应用路由不能为空', trigger: 'blur' }]
      }
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
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
      this.formType = 1
      this.form = {
        microAppId: null,

        name: null,

        entry: null,

        activeRule: null,

        createBy: null,

        createTime: null,

        updateBy: null,

        updateTime: null
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
      const microAppId = row ? row.microAppId : ids
      getMicroApp(microAppId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = this.$t('modify')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.microAppId !== undefined && this.form.microAppId !== null) {
            const modifyMessage = this.$t('modify.success')
            updateMicroApp(this.form)
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
            addMicroApp(this.form)
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
