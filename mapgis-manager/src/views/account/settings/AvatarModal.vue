<template>
  <a-modal
    :title="$t('account.center.modify.avatar')"
    :visible="visible"
    :maskClosable="false"
    :width="800"
    :footer="null"
    @cancel="cancelHandel"
  >
    <a-row>
      <a-col :xs="24" :md="12" :style="{ height: '350px' }">
        <vue-cropper
          ref="cropper"
          :img="options.img"
          :info="true"
          :autoCrop="options.autoCrop"
          :autoCropWidth="options.autoCropWidth"
          :autoCropHeight="options.autoCropHeight"
          :fixedBox="options.fixedBox"
          @realTime="realTime"
        >
        </vue-cropper>
      </a-col>
      <a-col :xs="24" :md="12" :style="{ height: '350px' }">
        <div class="avatar-upload-preview">
          <img :src="previews.url" :style="previews.img" />
        </div>
      </a-col>
    </a-row>
    <br />
    <a-row>
      <a-col :lg="2" :md="2">
        <a-upload name="file" :beforeUpload="beforeUpload" :showUploadList="false">
          <a-button icon="upload">{{ $t('account.center.select.picture') }}</a-button>
        </a-upload>
      </a-col>
      <a-col :lg="{ span: 1, offset: 2 }" :md="2">
        <a-button icon="plus" @click="changeScale(1)" />
      </a-col>
      <a-col :lg="{ span: 1, offset: 1 }" :md="2">
        <a-button icon="minus" @click="changeScale(-1)" />
      </a-col>
      <a-col :lg="{ span: 1, offset: 1 }" :md="2">
        <a-button icon="undo" @click="rotateLeft" />
      </a-col>
      <a-col :lg="{ span: 1, offset: 1 }" :md="2">
        <a-button icon="redo" @click="rotateRight" />
      </a-col>
      <a-col :lg="{ span: 2, offset: 6 }" :md="2">
        <a-button type="primary" :loading="uploading" @click="finish('blob')" icon="save">{{ $t('save') }}</a-button>
      </a-col>
    </a-row>
  </a-modal>
</template>
<script>
import store from '@/store'
import { uploadAvatar } from '@/api/system/user'
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      visible: false,
      id: null,
      fileList: [],
      uploading: false,
      options: {
        // img: 'https://zos.alipayobjects.com/rmsportal/jkjgkEfvpUPVyRjUImniVslZfWPnJuuZ.png',
        img: '',
        autoCrop: true,
        autoCropWidth: 200,
        autoCropHeight: 200,
        fixedBox: true
      },
      previews: {}
    }
  },
  computed: {
    ...mapGetters(['avatar'])
  },
  methods: {
    edit(id) {
      this.visible = true
      this.id = id
      this.options.img = this.avatar
      /* 获取原始头像 */
    },
    close() {
      this.id = null
      this.visible = false
    },
    cancelHandel() {
      this.close()
    },
    changeScale(num) {
      num = num || 1
      this.$refs.cropper.changeScale(num)
    },
    rotateLeft() {
      this.$refs.cropper.rotateLeft()
    },
    rotateRight() {
      this.$refs.cropper.rotateRight()
    },
    beforeUpload(file) {
      const reader = new FileReader()
      // 把Array Buffer转化为blob 如果是base64不需要
      // 转化为base64
      reader.readAsDataURL(file)
      reader.onload = () => {
        this.options.img = reader.result
      }
      // 转化为blob
      // reader.readAsArrayBuffer(file)

      return false
    },

    // 上传图片（点击上传按钮）
    finish(type) {
      const _this = this
      this.uploading = true
      const formData = new FormData()
      // 输出
      if (type === 'blob') {
        this.$refs.cropper.getCropBlob(data => {
          const img = window.URL.createObjectURL(data)
          this.model = true
          this.modelSrc = img
          formData.append('avatarfile', data, this.fileName)
          uploadAvatar(formData).then(response => {
            this.open = false
            let imgUrl = window._CONFIG['domainURL'] + response.imgUrl
            if (response.imgUrl.startsWith('http') || response.imgUrl.startsWith('https')) {
              imgUrl = response.imgUrl
            }
            store.commit('SET_AVATAR', imgUrl)
            _this.$message.success(_this.$t('account.center.upload.success'))
            _this.$emit('ok', imgUrl)
            _this.visible = false
          })
        })
      } else {
        this.$refs.cropper.getCropData(data => {
          this.model = true
          this.modelSrc = data
        })
      }
      this.uploading = false
    },
    realTime(data) {
      this.previews = data
    }
  }
}
</script>

<style lang="less" scoped>
.avatar-upload-preview {
  position: absolute;
  top: 50%;
  transform: translate(50%, -50%);
  width: 180px;
  height: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
  }
}
</style>
