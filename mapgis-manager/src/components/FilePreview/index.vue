<template>
  <div>
    <a-space class="go-back-button">
      <a-button type="primary" :href="src" icon="download" target="_blank"> </a-button>
      <a-button type="primary" icon="rollback" @click="$emit('goBack')">{{ $t('return') }}</a-button>
    </a-space>
    <a-card :bordered="false" :body-style="{ padding: '0px' }">
      <vue-office-docx
        v-if="fileType.toLowerCase() === 'doc' || fileType.toLowerCase() === 'docx'"
        :src="src"
        :style="realHeight"
        @rendered="renderedHandler"
      />
      <vue-office-excel
        v-else-if="fileType.toLowerCase() === 'xls' || fileType.toLowerCase() === 'xlsx'"
        :src="src"
        :style="realHeight"
        @rendered="renderedHandler"
        @error="errorHandler"
      />
      <vue-office-pdf
        v-else-if="fileType.toLowerCase() === 'pdf'"
        :src="src"
        @rendered="renderedHandler"
        @error="errorHandler"
      />
      <a-result v-else status="warning" title="不支持预览的文件类型" />
    </a-card>
  </div>
</template>

<script>
import { message } from 'ant-design-vue'
// 引入VueOfficeDocx组件
import VueOfficeDocx from '@vue-office/docx'
// 引入相关样式
import '@vue-office/docx/lib/index.css'
// 引入VueOfficeExcel组件
import VueOfficeExcel from '@vue-office/excel'
// 引入相关样式
import '@vue-office/excel/lib/index.css'
// 引入VueOfficePdf组件
import VueOfficePdf from '@vue-office/pdf'

export default {
  components: {
    VueOfficeDocx,
    VueOfficeExcel,
    VueOfficePdf
  },
  props: {
    src: {
      type: String,
      default: '',
      required: true
    },
    // 文件类型
    fileType: {
      type: String,
      default: '',
      required: false
    },
    height: {
      type: String,
      default: '80vh'
    }
  },
  computed: {
    realHeight() {
      return `height: ${this.height}`
    }
  },
  methods: {
    // 渲染完成
    renderedHandler() {},
    // 渲染失败
    errorHandler() {
      message.warning('渲染失败，请尝试重新打开！')
    }
  }
}
</script>

<style lang="less" scoped>
.go-back-button {
  position: absolute;
  float: right;
  right: 10px;
  z-index: 999;
}
</style>
