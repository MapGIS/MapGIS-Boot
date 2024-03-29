<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('log.operlog.system.module')">
                <a-input
                  v-model="queryParam.title"
                  :placeholder="$t('please.prefix.input', { content: $t('log.operlog.system.module') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('operation.user')">
                <a-input
                  v-model="queryParam.operName"
                  :placeholder="$t('please.prefix.input', { content: $t('operation.user') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('type')">
                  <a-select
                    :placeholder="$t('operation.type')"
                    v-model="queryParam.businessType"
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
                <a-form-item :label="$t('status')">
                  <a-select
                    :placeholder="$t('operation.status')"
                    v-model="queryParam.status"
                    style="width: 100%"
                    allow-clear
                  >
                    <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
                      d.dictLabel
                    }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('operation.time')">
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
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:operlog:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="danger" @click="handleClean" v-hasPermi="['system:operlog:remove']">
          <a-icon type="delete" />{{ $t('clear') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:operlog:export']">
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
      <view-form ref="viewForm" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="operId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        :bordered="tableBordered"
        @change="handleTableChange"
      >
        <span slot="businessType" slot-scope="text, record">
          {{ typeFormat(record) }}
        </span>
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.viewForm.handleView(record)" v-hasPermi="['system:operlog:query']">
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
import { list, delOperlog, cleanOperlog } from '@/api/system/operlog'
import ViewForm from './modules/ViewForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Operlog',
  components: {
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
      loading: false,
      // 非多个禁用
      multiple: true,
      total: 0,
      // 状态数据字典
      statusOptions: [],
      typeOptions: [],
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      },
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('log') }),
          dataIndex: 'operId',
          align: 'center'
        },
        {
          title: this.$t('log.operlog.system.module'),
          dataIndex: 'title',
          align: 'center'
        },
        {
          title: this.$t('operation.type'),
          dataIndex: 'businessType',
          scopedSlots: { customRender: 'businessType' },
          align: 'center'
        },
        {
          title: this.$t('request.method'),
          dataIndex: 'requestMethod',
          align: 'center'
        },
        {
          title: this.$t('operation.user'),
          dataIndex: 'operName',
          align: 'center',
          sorter: true
        },
        {
          title: this.$t('operation.address'),
          dataIndex: 'operIp',
          align: 'center'
        },
        {
          title: this.$t('operation.status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: this.$t('operation.cost.time'),
          dataIndex: 'costTime',
          align: 'center',
          sorter: true
        },
        {
          title: this.$t('operation.time'),
          dataIndex: 'operTime',
          align: 'center',
          sorter: true
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
    this.getDicts('sys_oper_type').then(response => {
      this.typeOptions = response.data
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
    handleTableChange(pagination, filters, sorter) {
      const sort = this.tableSorter(sorter)
      this.queryParam.orderByColumn = sort.orderByColumn
      this.queryParam.isAsc = sort.isAsc
      this.getList()
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 操作日志状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 操作日志类型字典翻译
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType)
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
        title: undefined,
        operName: undefined,
        businessType: undefined,
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
      this.ids = this.selectedRows.map(item => item.operId)
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const operIds = row.operId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + operIds + this.$t('is.data'),
        onOk() {
          return delOperlog(operIds).then(() => {
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
        content: this.$t('log.operlog.tip.clear'),
        onOk() {
          return cleanOperlog().then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/operlog/export`,
            {
              ...that.queryParam
            },
            `operlog_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
