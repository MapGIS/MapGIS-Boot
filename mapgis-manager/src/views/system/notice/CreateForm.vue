<template>
  <page-header-wrapper @back="back">
    <template v-slot:breadcrumb>
      {{ formTitle }}
    </template>
    <template v-slot:title>
      {{ formTitle }}
    </template>
    <template v-slot:content> </template>
    <template v-slot:extraContent> </template>
    <template v-slot:extra>
      <a-space>
        <a-button type="primary" @click="handleSubmit"> {{ $t('publish') }} </a-button>
        <a-button type="dashed" @click="back">{{ $t('cancel') }}</a-button>
      </a-space>
    </template>
    <a-card :bordered="false">
      <a-row :gutter="12">
        <a-col :span="24">
          <a-form-model ref="baseForm" :model="form" :rules="baseRules" :wrapper-col="wrapperCol">
            <a-form-model-item prop="noticeTitle">
              <a-input
                size="large"
                v-model="form.noticeTitle"
                :placeholder="$t('please.prefix.input', { content: $t('message.notice.title') })"
              />
            </a-form-model-item>
            <a-form-model-item prop="noticeContent">
              <editor ref="noticeContentEditor" v-model="form.noticeContent" />
            </a-form-model-item>
          </a-form-model>
        </a-col>
      </a-row>
    </a-card>
    <a-drawer width="30%" :label-col="4" :wrapper-col="14" :visible="open" @close="onClose">
      <a-divider orientation="left">
        <b>{{ formTitle }}</b>
      </a-divider>
      <a-form-model ref="form" :model="form" :rules="rules">
        <a-form-model-item :label="$t('message.notice.type')" prop="noticeType">
          <a-select :placeholder="$t('please.select')" v-model="form.noticeType">
            <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
              d.dictLabel
            }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :label="$t('status')" prop="status">
          <a-radio-group v-model="form.status" button-style="solid">
            <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
              d.dictLabel
            }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <div class="bottom-control">
          <a-space>
            <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('publish') }}</a-button>
            <a-button type="dashed" @click="onClose">{{ $t('cancel') }}</a-button>
          </a-space>
        </div>
      </a-form-model>
    </a-drawer>
  </page-header-wrapper>
</template>

<script>
import { getNotice, addNotice, updateNotice } from '@/api/system/notice'
import Editor from '@/components/Editor'

export default {
  name: 'NoticeForm',
  components: {
    Editor
  },
  data() {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 24 },
      submitLoading: false,
      total: 0,
      id: undefined,
      formTitle: '',
      // 状态数据字典
      statusOptions: [],
      typeOptions: [],
      // 表单参数
      form: {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: '',
        status: '0'
      },
      baseRules: {
        noticeTitle: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('message.notice.title') }),
            trigger: 'blur'
          }
        ]
      },
      rules: {
        noticeType: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('message.notice.type') }),
            trigger: 'change'
          }
        ]
      },
      open: false
    }
  },
  filters: {},
  created() {
    this.getDicts('sys_notice_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_notice_type').then(response => {
      this.typeOptions = response.data
    })
  },
  computed: {},
  watch: {},
  mounted() {
    this.id = this.$route.params.id
    this.formTitle = this.$route.params.formTitle
    if (this.id) {
      this.handleUpdate(this.id)
    } else {
      this.handleAdd()
    }
  },
  methods: {
    // 表单重置
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeContent: '',
        status: '0'
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.formTitle = this.$t('add.suffix', { content: this.$t('notice') })
    },
    /** 修改按钮操作 */
    handleUpdate(id) {
      this.reset()
      const noticeId = id
      getNotice(noticeId).then(response => {
        this.form = response.data
        this.formTitle = this.$t('modify.suffix', { content: this.$t('notice') })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.noticeId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateNotice(this.form)
              .then(response => {
                this.$message.success(modifyMessage, 3)
                this.back()
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            const addMessage = this.$t('add.success')
            addNotice(this.form)
              .then(response => {
                this.$message.success(addMessage, 3)
                this.back()
              })
              .finally(() => {
                this.submitLoading = false
              })
          }
        } else {
          return false
        }
      })
    },
    back() {
      this.$router.push('/message/notice')
    },
    onClose() {
      this.open = false
    },
    handleSubmit() {
      this.$refs.baseForm.validate(valid => {
        if (valid) {
          this.open = true
        } else {
          return false
        }
      })
    }
  }
}
</script>
