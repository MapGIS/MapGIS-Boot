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
      <a-form-model-item :label="$t('dict.name')" prop="dictName">
        <a-input v-model="form.dictName" placeholder="请输入字典名称" />
      </a-form-model-item>
      <a-form-model-item label="字典类型" prop="dictType">
        <a-input v-model="form.dictType" placeholder="请输入字典类型" />
      </a-form-model-item>
      <a-form-model-item :label="$t('status')" prop="status">
        <a-select :placeholder="$t('please.select')" v-model="form.status">
          <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('remark')" prop="remark">
        <a-input v-model="form.remark" :placeholder="$t('please.input.content')" type="textarea" allow-clear />
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
import { getType, addType, updateType } from '@/api/system/dict/type'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    statusOptions: {
      type: Array,
      required: true
    }
  },
  components: {},
  data() {
    return {
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: '0',
        remark: undefined
      },
      open: false,
      rules: {
        dictName: [{ required: true, message: '字典名称不能为空', trigger: 'blur' }],
        dictType: [{ required: true, message: '字典类型不能为空', trigger: 'blur' }]
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
        dictId: undefined,
        dictName: undefined,
        dictType: undefined,
        status: '0',
        remark: undefined
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.formTitle = '添加字典类型'
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      const dictId = row ? row.dictId : ids
      getType(dictId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改字典类型'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.dictId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateType(this.form)
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
            addType(this.form)
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
