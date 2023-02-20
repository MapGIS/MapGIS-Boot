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
    <a-form-model ref="form" :model="form" :rules="rules" v-bind="formLayout">
      <a-form-model-item :label="$t('schedule.job.name')" prop="jobName">
        <a-input
          v-model="form.jobName"
          :placeholder="$t('please.prefix.input', { content: $t('schedule.job.name') })"
        />
      </a-form-model-item>
      <a-form-model-item :label="$t('schedule.job.group')" prop="jobGroup">
        <a-select :placeholder="$t('please.select')" v-model="form.jobGroup">
          <a-select-option v-for="(d, index) in jobGroupOptions" :key="index" :value="d.dictValue">
            {{ d.dictLabel }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item prop="invokeTarget">
        <span slot="label">
          {{ $t('schedule.job.invoke.method') }}&nbsp;
          <a-popover placement="topLeft">
            <template slot="content">
              <p>
                {{ $t('schedule.job.class.call.example') }}
                <code>com.zondy.mapgis.quartz.task.MapTask.mapParams('gis')</code>
              </p>
              <p>{{ $t('schedule.job.parameter.desc') }}</p>
            </template>
            <span slot="title">{{ $t('schedule.job.bean.call.example') }}<code>mapTask.mapParams('gis')</code></span>
            <a-icon type="question-circle-o" />
          </a-popover>
        </span>
        <a-input
          v-model="form.invokeTarget"
          :placeholder="$t('please.prefix.input', { content: $t('schedule.job.invoke.method') })"
        />
      </a-form-model-item>
      <a-form-model-item :label="$t('schedule.job.cron.expression')" prop="cronExpression">
        <a-input-search
          v-model="form.cronExpression"
          :placeholder="$t('please.prefix.input', { content: $t('schedule.job.cron.expression') })"
          @search="$refs.genCrontab.show(form.cronExpression)"
        >
          <a-button slot="enterButton">
            {{ $t('schedule.job.generate.expresson') }}
            <a-icon type="tool" />
          </a-button>
        </a-input-search>
      </a-form-model-item>
      <a-form-model-item :label="$t('schedule.job.whether.to.concurrent')" prop="concurrent">
        <a-radio-group v-model="form.concurrent" button-style="solid">
          <a-radio-button value="0">{{ $t('schedule.job.permit') }}</a-radio-button>
          <a-radio-button value="1">{{ $t('schedule.job.prohibit') }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item :label="$t('schedule.job.misfire.policy')" prop="misfirePolicy">
        <a-radio-group v-model="form.misfirePolicy" button-style="solid">
          <a-radio-button value="1">{{ $t('schedule.job.execute.immediately') }}</a-radio-button>
          <a-radio-button value="2">{{ $t('schedule.job.execute.once') }}</a-radio-button>
          <a-radio-button value="3">{{ $t('schedule.job.abandon.execute') }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item :label="$t('status')" prop="status">
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-radio>
        </a-radio-group>
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('ok') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
    <gen-crontab ref="genCrontab" @fill="crontabFill" />
  </pop-dialog>
</template>

<script>
import { getJob, addJob, updateJob } from '@/api/schedule/job'
import GenCrontab from './GenCrontab'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  components: {
    GenCrontab
  },
  props: {
    statusOptions: {
      type: Array,
      required: true
    },
    jobGroupOptions: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      submitLoading: false,
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
      open: false,
      openView: false,
      rules: {
        jobName: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('schedule.job.name') }),
            trigger: 'blur'
          }
        ],
        invokeTarget: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('schedule.job.invoke.method') }),
            trigger: 'blur'
          }
        ],
        cronExpression: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('schedule.job.cron.expression') }),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  methods: {
    handleView(row) {
      getJob(row.jobId).then(response => {
        this.form = response.data
        this.openView = true
      })
    },
    onCloseView() {
      this.openView = false
    },
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
        jobId: undefined,
        jobName: undefined,
        jobGroup: undefined,
        invokeTarget: undefined,
        cronExpression: undefined,
        misfirePolicy: '1',
        concurrent: '1',
        status: '0'
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.formTitle = this.$t('add.suffix', { content: this.$t('job') })
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      const jobId = row ? row.jobId : ids
      getJob(jobId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = this.$t('modify.suffix', { content: this.$t('job') })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.jobId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateJob(this.form)
              .then(response => {
                this.$message.success(modifyMessage, 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            const addMessage = this.$t('add.success')
            addJob(this.form)
              .then(response => {
                this.$message.success(addMessage, 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          }
        } else {
          return false
        }
      })
    },
    crontabFill(value) {
      this.form.cronExpression = value
    }
  }
}
</script>
