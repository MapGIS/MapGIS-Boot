<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('schedule.job.name')">
                <a-input
                  v-model="queryParam.jobName"
                  :placeholder="$t('please.prefix.input', { content: $t('schedule.job.name') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('schedule.job.group')">
                <a-select
                  :placeholder="$t('please.prefix.select', { content: $t('schedule.job.group') })"
                  v-model="queryParam.jobGroup"
                  style="width: 100%"
                  allow-clear
                >
                  <a-select-option v-for="(d, index) in jobGroupOptions" :key="index" :value="d.dictValue">{{
                    d.dictLabel
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('schedule.job.execute.status')">
                  <a-select
                    :placeholder="$t('please.prefix.select', { content: $t('schedule.job.execute.status') })"
                    v-model="queryParam.status"
                    style="width: 100%"
                    allow-clear
                  >
                    <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
                      d.dictLabel
                    }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('schedule.job.execute.time')">
                  <a-range-picker
                    style="width: 100%"
                    v-model="dateRange"
                    valueFormat="YYYY-MM-DD"
                    format="YYYY-MM-DD"
                    allow-clear
                  />
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />{{ $t('query') }}</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery">
                  <a-icon type="redo" />{{ $t('reset') }}
                </a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? $t('collapse') : $t('expand') }}
                  <a-icon :type="advanced ? 'up' : 'down'" />
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['monitor:job:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="danger" @click="handleClean" v-hasPermi="['monitor:job:remove']">
          <a-icon type="delete" />{{ $t('clear') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['monitor:job:export']">
          <a-icon type="download" />{{ $t('export') }}
        </a-button>
        <table-setting
          :style="{ float: 'right' }"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList"
        />
      </div>
      <!-- 详细信息 -->
      <log-view-form ref="logViewForm" :jobGroupOptions="jobGroupOptions" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="jobLogId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="jobGroup" slot-scope="text, record">
          {{ jobGroupFormat(record) }}
        </span>
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.logViewForm.handleView(record)" v-hasPermi="['monitor:job:query']">
            <a-icon type="eye" />{{ $t('detail') }}
          </a>
        </span>
      </a-table>
      <!-- 分页 -->
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="totalItems"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listJobLog, delJobLog, cleanJobLog } from '@/api/schedule/jobLog'
import LogViewForm from './modules/LogViewForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'JobLog',
  components: {
    LogViewForm
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      total: 0,
      // 状态数据字典
      statusOptions: [],
      jobGroupOptions: [],
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // 高级搜索 展开/关闭
      advanced: false,
      loading: false,
      // 选中数组
      ids: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 非多个禁用
      multiple: true,
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('log') }),
          dataIndex: 'jobLogId',
          align: 'center'
        },
        {
          title: this.$t('log.operlog.system.module'),
          dataIndex: 'jobName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('operation.type'),
          dataIndex: 'jobGroup',
          scopedSlots: { customRender: 'jobGroup' },
          align: 'center'
        },
        {
          title: this.$t('schedule.job.invoke.method'),
          dataIndex: 'invokeTarget',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('log.info'),
          dataIndex: 'jobMessage',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('schedule.job.status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: this.$t('schedule.job.execute.time'),
          dataIndex: 'createTime',
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {
    this.getList()
    this.getDicts('sys_common_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_job_group').then(response => {
      this.jobGroupOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      listJobLog(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 执行状态字典翻译
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 任务组名字典翻译
    jobGroupFormat(row) {
      return this.selectDictLabel(this.jobGroupOptions, row.jobGroup)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.jobLogId)
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const jobLogIds = row.jobLogId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + jobLogIds + this.$t('is.data'),
        onOk() {
          return delJobLog(jobLogIds).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
          })
        },
        onCancel() {}
      })
    },
    /** 清空按钮操作 */
    handleClean() {
      const that = this
      this.$confirm({
        title: this.$t('confirm.clear'),
        content: this.$t('schedule.job.tip.clear'),
        onOk() {
          return cleanJobLog().then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(that.$t('clear.success'), 3)
          })
        },
        onCancel() {}
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const that = this
      this.$confirm({
        title: this.$t('confirm.export'),
        content: this.$t('export.condition.data.description'),
        onOk() {
          that.download(
            `${window._CONFIG['apiPathManagerPrefix']}/schedule/jobLog/export`,
            {
              ...that.queryParam
            },
            `job_log_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
