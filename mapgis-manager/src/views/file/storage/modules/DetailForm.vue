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
    <a-descriptions :column="1" size="middle" bordered>
      <a-descriptions-item :label="$t('file.storage.file.name')">{{ form.name }}</a-descriptions-item>
      <a-descriptions-item :label="$t('file.storage.file.engine')">{{ form.engine }}</a-descriptions-item>
      <a-descriptions-item :label="$t('file.storage.file.bucket')">{{ form.bucket }}</a-descriptions-item>
      <a-descriptions-item :label="$t('file.storage.file.size')">{{ form.size }}</a-descriptions-item>
      <a-descriptions-item :label="$t('file.storage.file.suffix')">{{ form.suffix }}</a-descriptions-item>
      <a-descriptions-item :label="$t('file.storage.file.upload.time')">{{ form.createTime }}</a-descriptions-item>
    </a-descriptions>
    <a-form-model ref="form" :model="form" layout="vertical" style="padding-top: 24px">
      <a-form-model-item :label="$t('file.storage.file.url')" prop="url">
        <span> {{ form.url }} </span>
      </a-form-model-item>
      <a-form-model-item :label="$t('file.storage.file.download.url')" prop="downloadUrl">
        <span> {{ form.downloadUrl }} </span>
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
import { getFile } from '@/api/file/storage'
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
        engine: null,

        bucket: null,

        name: null,

        suffix: null,

        size: null,

        url: null,

        downloadUrl: null
      },
      open: false
    }
  },
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
        engine: null,

        bucket: null,

        name: null,

        suffix: null,

        size: null,

        url: null,

        downloadUrl: null
      }
    },
    /** 查看按钮操作 */
    handleLook(row) {
      this.reset()
      const fileId = row.fileId
      getFile(fileId).then(response => {
        this.form = response.data
        this.form.downloadUrl = this.form.url
        if (this.form.engine === 'LOCAL') {
          this.form.downloadUrl = `${window.location.protocol}//${window.location.host}${window._CONFIG['domainURL']}${this.form.downloadUrl}`
        }
        this.open = true
        this.formTitle = this.$t('look')
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.open = false
      this.$emit('ok')
    }
  }
}
</script>
