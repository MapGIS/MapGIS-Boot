<template>
  <a-modal
    :title="title"
    :visible="open"
    :confirm-loading="uploading"
    @cancel="uploadFileHandleCancel"
    @ok="uploadFileHandleChange"
  >
    <a-spin :tip="$t('uploading...')" :spinning="uploading">
      <a-upload-dragger
        name="file"
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
    </a-spin>
  </a-modal>
</template>

<script>
import { uploadFile } from '@/api/file/storage'

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
    /** 上传文件窗体关闭 */
    uploadFileHandleCancel(e) {
      this.open = false
      this.fileList = []
      // 关闭后刷新列表
      this.$emit('ok')
    },
    /** 上传文件窗体开启 */
    uploadFileHandleOpen(e) {
      this.open = true
    },
    beforeUpload(file) {
      this.fileList = [file]
      return false
    },
    /** 上传文件 */
    uploadFileHandleChange() {
      this.uploading = true
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      uploadFile(formData)
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
    handleRemove() {
      this.fileList = []
      this.uploading = false
    }
  }
}
</script>
