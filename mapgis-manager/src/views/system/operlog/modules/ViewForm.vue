<template>
  <a-drawer width="35%" placement="right" :closable="false" :visible="openView" @close="onCloseView">
    <a-descriptions :title="$t('operation.info')" layout="vertical">
      <a-descriptions-item :label="$t('log.operlog.operation.module')">
        {{ form.title }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.login.info')">
        {{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.request.url')">
        {{ form.operUrl }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.operation.method')" span="2">
        <div style="word-break: break-all">{{ form.method }}</div>
      </a-descriptions-item>
      <a-descriptions-item :label="$t('request.method')">
        {{ form.requestMethod }}
      </a-descriptions-item>
    </a-descriptions>
    <a-descriptions :title="$t('log.operlog.interface.info')" layout="vertical">
      <a-descriptions-item :label="$t('request.params')" span="3">
        {{ form.operParam }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.return.params')" span="3">
        {{ form.jsonResult }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('operation.status')" span="2">
        <a-badge v-if="form.status === 0" status="processing" :text="$t('normal')" />
        <a-badge v-if="form.status === 1" status="error" :text="$t('failed')" />
      </a-descriptions-item>
      <a-descriptions-item :label="$t('operation.time')">
        {{ parseTime(form.operTime) }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.exception.info')" span="3" v-if="form.status === 1">
        {{ form.errorMsg }}
      </a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script>
export default {
  name: 'ViewForm',
  props: {},
  data() {
    return {
      // 表单参数
      form: {},
      openView: false
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  methods: {
    handleView(row) {
      this.openView = true
      this.form = row
    },
    onCloseView() {
      this.openView = false
    }
  }
}
</script>

<style lang="less" scoped>
:deep(.ant-descriptions-item-content) {
  word-break: break-all;
}
</style>
