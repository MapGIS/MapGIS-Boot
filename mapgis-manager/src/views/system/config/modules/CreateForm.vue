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
      <a-form-model-item :label="$t('config.name')" prop="configName">
        <a-input v-model="form.configName" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item label="参数键名" prop="configKey">
        <a-input v-model="form.configKey" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item label="参数键值" prop="configValue">
        <a-input v-model="form.configValue" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item label="系统内置" prop="configType">
        <a-select placeholder="是否内置" v-model="form.configType">
          <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('remark')" prop="remark">
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
import { getConfig, addConfig, updateConfig } from '@/api/system/config'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    typeOptions: {
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
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'Y',
        remark: undefined
      },
      open: false,
      rules: {
        configName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
        configKey: [{ required: true, message: '参数键名不能为空', trigger: 'blur' }],
        configValue: [{ required: true, message: '参数键值不能为空', trigger: 'blur' }]
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
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        configType: 'Y',
        remark: undefined
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.formTitle = '添加参数'
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      const configId = row ? row.configId : ids
      getConfig(configId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改参数'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.configId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateConfig(this.form)
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
            addConfig(this.form)
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
