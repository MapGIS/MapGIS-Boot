<template>
  <div>
    <a-form-model ref="basicInfoForm" :model="info" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item :label="$t('dev.gen.table.name')" prop="tableName">
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.table.name') })"
              v-model="info.tableName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item :label="$t('dev.gen.table.comment')" prop="tableComment">
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.table.comment') })"
              v-model="info.tableComment"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item :label="$t('dev.gen.entity.class.name')" prop="className">
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.entity.class.name') })"
              v-model="info.className"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item :label="$t('author')" prop="functionAuthor">
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('author') })"
              v-model="info.functionAuthor"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="24" :pull="3">
          <a-form-model-item :label="$t('remark')">
            <a-textarea placeholder="textarea with clear icon" allow-clear v-model="info.remark" />
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
export default {
  name: 'BasticInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      visible: false,
      loading: false,
      // 模态框数据
      data: {},
      rules: {
        tableName: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.table.name') }),
            trigger: 'blur'
          }
        ],
        tableComment: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.table.comment') }),
            trigger: 'blur'
          }
        ],
        className: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.entity.class.name') }),
            trigger: 'blur'
          }
        ],
        functionAuthor: [
          { required: true, message: this.$t('please.prefix.input', { content: this.$t('author') }), trigger: 'blur' }
        ]
      },
      labelCol: {
        xs: { span: 12 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 }
      },
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: []
    }
  },
  methods: {
    // 关闭模态框
    close() {
      this.visible = false
    },
    // 打开抽屉(由外面的组件调用)
    show(data) {
      if (data) {
        this.data = data
      }
      this.visible = true
    }
  }
}
</script>
