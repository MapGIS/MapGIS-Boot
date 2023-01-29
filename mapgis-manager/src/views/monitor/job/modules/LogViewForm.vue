<template>
  <a-drawer width="35%" placement="right" :closable="false" :visible="openView" @close="onCloseView">
    <a-descriptions :title="$t('schedule.job.log.detail')" layout="vertical" bordered :column="3">
      <a-descriptions-item :label="$t('id.suffix', { content: this.$t('log') })">
        {{ form.jobLogId }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.name')">
        {{ form.jobName }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.group')">
        {{ jobGroupFormat(form) }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.execute.status')">
        <a-badge v-if="form.status == 0" status="processing" :text="$t('normal')" />
        <a-badge v-if="form.status == 1" status="warning" :text="$t('failed')" />
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.execute.time')" span="2">
        {{ form.createTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.invoke.method')" span="3">
        {{ form.invokeTarget }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.info')" span="3">
        {{ form.jobMessage }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('log.operlog.exception.info')" span="3" v-if="form.status == 1">
        {{ form.exceptionInfo }}
      </a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script>
export default {
  name: 'LogViewForm',
  props: {
    jobGroupOptions: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      // 表单参数
      form: {
        jobId: undefined,
        jobName: undefined,
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: '1',
        concurrent: '1',
        status: '0'
      },
      openView: false
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  methods: {
    jobGroupFormat(row) {
      return this.selectDictLabel(this.jobGroupOptions, row.jobGroup)
    },
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
