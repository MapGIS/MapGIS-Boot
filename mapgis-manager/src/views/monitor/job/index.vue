<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="任务名称">
                <a-input v-model="queryParam.jobName" placeholder="请输入任务名称" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="任务组名">
                <a-input v-model="queryParam.jobGroup" placeholder="请选择任务组名" allow-clear />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="任务状态">
                  <a-select placeholder="请选择任务状态" v-model="queryParam.status" style="width: 100%" allow-clear>
                    <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
                      d.dictLabel
                    }}</a-select-option>
                  </a-select>
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['monitor:job:add']">
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['monitor:job:edit']"
        >
          <a-icon type="edit" />{{ $t('modify') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['monitor:job:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['monitor:job:export']">
          <a-icon type="download" />{{ $t('export') }}
        </a-button>
        <a-button type="dashed" @click="handleJobLog" v-hasPermi="['monitor:job:query']">
          <a-icon type="snippets" />日志
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
      <view-form ref="viewForm" :jobGroupOptions="jobGroupOptions" />
      <!-- 增加修改 -->
      <create-form ref="createForm" :statusOptions="statusOptions" :jobGroupOptions="jobGroupOptions" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="jobId"
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
          <a-popconfirm
            ok-text="是"
            cancel-text="否"
            @confirm="confirmHandleStatus(record)"
            @cancel="cancelHandleStatus(record)"
          >
            <span slot="title">
              确认<b>{{ record.status === '1' ? '开启' : '关闭' }} </b>{{ record.jobName }}的任务吗?
            </span>
            <a-switch checked-children="开" un-checked-children="关" :checked="record.status == 0" />
          </a-popconfirm>
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['monitor:job:edit']">
            <a-icon type="edit" />
            修改
          </a>
          <a-divider type="vertical" v-hasPermi="['monitor:job:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['monitor:job:remove']">
            <a-icon type="delete" />
            删除
          </a>
          <a-divider type="vertical" v-hasPermi="['system:job:edit', 'monitor:job:query']" />
          <a-dropdown v-hasPermi="['system:job:edit', 'monitor:job:query']">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              <a-icon type="double-right" />
              更多
            </a>
            <a-menu slot="overlay">
              <a-menu-item v-hasPermi="['monitor:job:edit']">
                <a-popconfirm
                  ok-text="是"
                  cancel-text="否"
                  @confirm="confirmHandleRun(record)"
                  @cancel="cancelHandleRun(record)"
                >
                  <span slot="title">确认执行一次{{ record.jobName }}的任务吗?</span>
                  <a>
                    <a-icon type="caret-right" />
                    执行一次
                  </a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-hasPermi="['monitor:job:query']">
                <a @click="$refs.viewForm.handleView(record)">
                  <a-icon type="eye" />
                  详细
                </a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
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
import { listJob, delJob, runJob, changeJobStatus } from '@/api/schedule/job'
import CreateForm from './modules/CreateForm'
import ViewForm from './modules/ViewForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Job',
  components: {
    CreateForm,
    ViewForm
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
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
      columns: [
        {
          title: '任务编号',
          dataIndex: 'jobId',
          align: 'center'
        },
        {
          title: '任务名称',
          dataIndex: 'jobName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '任务组名',
          dataIndex: 'jobGroup',
          scopedSlots: { customRender: 'jobGroup' },
          align: 'center'
        },
        {
          title: '调用目标字符串',
          dataIndex: 'invokeTarget',
          ellipsis: true,
          align: 'center'
        },
        {
          title: 'cron执行表达式',
          dataIndex: 'cronExpression',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '15%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {
    this.getList()
    this.getDicts('sys_job_status').then(response => {
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
    /** 查询定时任务列表 */
    getList() {
      this.loading = true
      listJob(this.queryParam).then(response => {
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
      this.ids = this.selectedRows.map(item => item.jobId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 任务日志列表查询 */
    handleJobLog() {
      this.$router.push({ path: '/monitor/job/log' })
    },
    /* 任务状态修改 */
    confirmHandleStatus(row) {
      const text = row.status === '1' ? '启用' : '停用'
      row.status = row.status === '0' ? '1' : '0'
      changeJobStatus(row.jobId, row.status)
        .then(() => {
          this.$message.success(text + '成功', 3)
        })
        .catch(function () {
          this.$message.error(text + '发生异常', 3)
        })
    },
    cancelHandleStatus(row) {},
    /* 立即执行一次 */
    confirmHandleRun(row) {
      runJob(row.jobId, row.jobGroup)
        .then(() => {
          this.$message.success('执行成功', 3)
        })
        .catch(function () {
          this.$message.error('发生异常', 3)
        })
    },
    cancelHandleRun(row) {},
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      this.ids = this.selectedRows.map(item => item.jobId)
      const jobIds = row.jobId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + jobIds + this.$t('is.data'),
        onOk() {
          return delJob(jobIds).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
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
            `${window._CONFIG['apiPathManagerPrefix']}/monitor/job/export`,
            {
              ...that.queryParam
            },
            `job_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
