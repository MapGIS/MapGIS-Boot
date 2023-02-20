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
      <a-form-model-item :label="$t('dev.dict.type')" prop="dictType">
        <a-input v-model="form.dictType" :disabled="true" />
      </a-form-model-item>
      <a-form-model-item :label="$t('dev.dict.label')" prop="dictLabel">
        <a-input v-model="form.dictLabel" :placeholder="$t('please.prefix.input', { content: $t('dev.dict.label') })" />
      </a-form-model-item>
      <a-form-model-item :label="$t('dev.dict.value')" prop="dictValue">
        <a-input v-model="form.dictValue" :placeholder="$t('please.prefix.input', { content: $t('dev.dict.value') })" />
      </a-form-model-item>
      <a-form-model-item :label="$t('dev.dict.order')" prop="dictSort">
        <a-input-number v-model="form.dictSort" :min="0" style="width: 100%" />
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
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('ok') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { getData, addData, updateData } from '@/api/system/dict/data'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateDataForm',
  mixins: [formMixin],
  props: {
    dictType: {
      type: String,
      required: true
    },
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
        dictCode: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        dictSort: 0,
        status: '0',
        remark: undefined
      },
      open: false,
      rules: {
        dictLabel: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('dev.dict.label') }),
            trigger: 'blur'
          }
        ],
        dictValue: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('dev.dict.value') }),
            trigger: 'blur'
          }
        ],
        dictSort: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('dev.dict.order') }),
            trigger: 'blur'
          }
        ]
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
        dictCode: undefined,
        dictLabel: undefined,
        dictValue: undefined,
        dictSort: 0,
        status: '0',
        remark: undefined
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.formTitle = this.$t('add.suffix', { content: this.$t('dev.dict.data') })
      this.form.dictType = this.dictType
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const dictCode = row.dictCode
      getData(dictCode).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = this.$t('modify.suffix', { content: this.$t('dev.dict.data') })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.dictCode !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateData(this.form)
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
            addData(this.form)
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
