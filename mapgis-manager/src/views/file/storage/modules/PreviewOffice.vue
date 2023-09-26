<template>
  <file-preview :src="src" :file-type="fileType" :height="height" @goBack="onGoBack" v-show="visible"></file-preview>
</template>

<script>
import FilePreview from '@/components/FilePreview'

export default {
  components: { FilePreview },
  data() {
    return {
      src: '',
      fileType: '',
      height: '80vh',
      visible: false
    }
  },
  methods: {
    preview({ url, fileType }) {
      this.visible = true
      this.src = url
      this.fileType = fileType
    },
    onGoBack() {
      this.visible = false
      this.$emit('goBack')
    }
  }
}
</script>

<style></style>
