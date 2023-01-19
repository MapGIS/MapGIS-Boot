<template>
  <a-modal
    :title="title"
    :visible="open"
    :confirm-loading="uploading"
    @cancel="importExcelHandleCancel"
    @ok="importExcelHandleChange"
  >
    <a-spin :tip="$t('uploading...')" :spinning="uploading">
      <a-upload-dragger
        name="file"
        accept=".xlsx, .xls"
        :file-list="fileList"
        :multiple="false"
        :remove="handleRemove"
        :before-upload="beforeUpload"
      >
        <p class="ant-upload-drag-icon">
          <a-icon type="inbox" />
        </p>
        <p class="ant-upload-text">{{ $t('security.user.please.drag.file.to.upload') }}</p>
        <p class="ant-upload-hint">{{ $t('security.user.upload.types.desc') }}</p>
      </a-upload-dragger>
      <a-checkbox @change="handleCheckedUpdateSupport" :checked="updateSupport != 0">
        {{ $t('security.user.whether.to.update.existed.user.data') }}
      </a-checkbox>
      <a @click="importTemplate">{{ $t('security.user.download.template') }}</a>
    </a-spin>
  </a-modal>
</template>

<script>
import { importData } from '@/api/system/user'

export default {
  name: 'ImportExcel',
  props: {},
  components: {},
  data() {
    return {
      title: this.$t('security.user.user.import'),
      open: false,
      uploadStatus: '',
      fileList: [],
      // 是否禁用上传
      uploading: false,
      updateSupport: 0
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  methods: {
    /** 导入excel窗体关闭 */
    importExcelHandleCancel(e) {
      this.open = false
      this.fileList = []
      // 关闭后刷新列表
      this.$emit('ok')
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        `${window._CONFIG['apiPathManagerPrefix']}/system/user/importTemplate`,
        {
          ...this.queryParams
        },
        `user_template_${new Date().getTime()}.xlsx`
      )
    },
    /** 导入excel窗体开启 */
    importExcelHandleOpen(e) {
      this.open = true
    },
    beforeUpload(file) {
      this.fileList = [file]
      return false
    },
    /** 导入excel */
    importExcelHandleChange() {
      this.uploading = true
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      formData.append('updateSupport', this.updateSupport)
      importData(formData)
        .then(response => {
          this.fileList = []
          this.$message.success(response.msg)
          this.open = false
          this.$emit('ok')
        })
        .finally(() => {
          this.uploading = false
        })
    },
    handleCheckedUpdateSupport() {
      this.updateSupport = this.updateSupport === 0 ? 1 : 0
    },
    handleRemove() {
      this.fileList = []
      this.uploading = false
    }
  }
}
</script>
