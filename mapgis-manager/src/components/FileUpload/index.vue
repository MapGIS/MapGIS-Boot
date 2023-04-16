<template>
  <div class="file-upload clearfix">
    <a-upload
      :action="uploadImgUrl"
      :list-type="type == 'image' ? 'picture-card' : 'picture'"
      :accept="type == 'image' ? 'image/png, image/jpeg' : ''"
      :show-upload-list="false"
      :headers="headers"
      :before-upload="beforeUpload"
      @change="handleChange"
    >
      <img v-if="realValue && type == 'image'" :src="realValue" />
      <div v-if="type == 'file'">
        <a-button> <a-icon type="upload" /> 上传 </a-button>
      </div>
      <div v-if="realValue == '' || realValue == null">
        <span v-if="type == 'image'">
          <a-icon :type="loading ? 'loading' : 'plus'" />
          <div class="ant-upload-text">上传</div>
        </span>
      </div>
    </a-upload>
    <span v-if="realValue && type == 'file'">
      <br />
      <a :href="realValue" target="_blank">{{ realValue }}</a>
    </span>
  </div>
</template>

<script>
import storage from 'store'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export default {
  name: 'ImageUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'image'
    },
    count: {
      type: Number,
      default: 1
    }
  },
  components: {},
  data() {
    return {
      loading: false,
      open: false,
      uploadImgUrl: window._CONFIG['domainURL'] + window._CONFIG['apiPathManagerPrefix'] + '/file/upload',
      headers: {
        Authorization: 'Bearer ' + storage.get(ACCESS_TOKEN)
      }
    }
  },
  computed: {
    realValue() {
      return window._CONFIG['domainURL'] + this.value
    }
  },
  mounted() {},
  methods: {
    handleCancel() {
      this.previewVisible = false
    },
    async handleChange(info) {
      if (info.file.status === 'uploading') {
        this.loading = true
        return
      }
      if (info.file.status === 'done') {
        if (info.file.response.code !== 200) {
          this.$message.error('上传失败' + info.file.response.msg)
          this.loading = false
          return
        }
        this.loading = false
        this.$emit('input', info.file.response.data.url)
      }
    },
    beforeUpload(file) {
      // 文件类型(file.type)、大小限制(file.size)
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$message.error('图片大小限制 2MB!')
      }
      return isLt2M
    }
  }
}
</script>
<style lang="less" scoped>
img {
  width: 128px;
  height: 128px;
}
</style>
