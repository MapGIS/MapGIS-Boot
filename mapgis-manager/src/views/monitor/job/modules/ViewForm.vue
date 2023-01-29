<template>
  <a-drawer width="35%" placement="right" :closable="false" :visible="openView" @close="onCloseView">
    <a-descriptions :title="$t('schedule.job.detail')" layout="vertical" bordered>
      <a-descriptions-item :label="$t('id.suffix', { content: this.$t('job') })">
        {{ form.jobId }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.group')">
        {{ jobGroupFormat(form.jobGroup) }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.name')">
        {{ form.jobName }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('create.time')">
        {{ form.createTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.next.execute.time')" span="2">
        {{ parseTime(form.nextValidTime) }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.invoke.method')">
        {{ form.invokeTarget }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.cron.expression')" span="2">
        {{ form.cronExpression }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.status')">
        <a-badge v-if="form.status == 0" status="processing" :text="$t('running')" />
        <a-badge v-if="form.status == 1" status="warning" :text="$t('pause')" />
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.whether.to.concurrent')">
        <a-badge v-if="form.concurrent == 0" status="processing" :text="$t('schedule.job.permit')" />
        <a-badge v-if="form.concurrent == 1" status="warning" :text="$t('schedule.job.prohibit')" />
      </a-descriptions-item>
      <a-descriptions-item :label="$t('schedule.job.misfire.policy')">
        <a-badge v-if="form.misfirePolicy == 0" status="default" :text="$t('schedule.job.default.policy')" />
        <a-badge v-if="form.misfirePolicy == 1" status="Processing" :text="$t('schedule.job.execute.immediately')" />
        <a-badge v-if="form.misfirePolicy == 2" color="purple" :text="$t('schedule.job.execute.once')" />
        <a-badge v-if="form.misfirePolicy == 3" status="warning" :text="$t('schedule.job.abandon.execute')" />
      </a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script>
import { getJob } from '@/api/schedule/job'

export default {
  name: 'ViewForm',
  props: {
    jobGroupOptions: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      formTitle: '',
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
    jobGroupFormat(jobGroup) {
      return this.selectDictLabel(this.jobGroupOptions, jobGroup)
    },
    handleView(row) {
      getJob(row.jobId).then(response => {
        this.form = response.data
        this.openView = true
      })
    },
    onCloseView() {
      this.openView = false
    }
  }
}
</script>
