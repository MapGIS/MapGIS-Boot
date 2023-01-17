<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('login.address')">
                <a-input
                  v-model="queryParam.ipaddr"
                  :placeholder="$t('please.prefix.input', { content: $t('login.address') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('status')">
                <a-select
                  :placeholder="$t('please.prefix.select', { content: $t('status') })"
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
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('login.name')">
                  <a-input v-model="queryParam.loginName" style="width: 100%" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('login.time')">
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
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:logininfor:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="danger" @click="handleClean" v-hasPermi="['system:logininfor:remove']">
          <a-icon type="delete" />{{ $t('clear') }}
        </a-button>
        <a-button type="primary" @click="handleUnlock" v-hasPermi="['system:logininfor:unlock']" :disabled="single">
          <a-icon type="unlock" />{{ $t('unlock') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:logininfor:export']">
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
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="infoId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        @change="handleTableChange"
        :bordered="tableBordered"
      >
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
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
import { list, delLogininfor, cleanLogininfor, unlockLogininfor } from '@/api/system/logininfor'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Logininfor',
  components: {},
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      loading: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 选择用户名
      selectName: '',
      // 选择IP
      selectIp: '',
      ids: [],
      total: 0,
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: null,
        userName: undefined,
        status: undefined
      },
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('log') }),
          dataIndex: 'infoId',
          align: 'center'
        },
        {
          title: this.$t('username'),
          dataIndex: 'userName',
          align: 'center'
        },
        {
          title: this.$t('login.address'),
          dataIndex: 'ipaddr',
          align: 'center'
        },
        {
          title: this.$t('browser'),
          dataIndex: 'browser',
          align: 'center'
        },
        {
          title: this.$t('os'),
          dataIndex: 'os',
          align: 'center'
        },
        {
          title: this.$t('login.status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: this.$t('operation.info'),
          dataIndex: 'msg',
          align: 'center'
        },
        {
          title: this.$t('login.time'),
          dataIndex: 'loginTime',
          align: 'center',
          sorter: true
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
    // 执行状态字典翻译
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
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
        ipaddr: null,
        userName: undefined,
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
      this.ids = this.selectedRows.map(item => item.infoId)
      this.multiple = !selectedRowKeys.length
      this.single = selectedRowKeys.length !== 1
      this.selectName = selectedRows.map(item => item.userName)
      this.selectIp = selectedRows.map(item => item.ipaddr)
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const infoIds = row.infoId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + infoIds + this.$t('is.data'),
        onOk() {
          return delLogininfor(infoIds).then(() => {
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
        content: this.$t('log.logininfo.tip.clear'),
        onOk() {
          return cleanLogininfor().then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(that.$t('clear.success'), 3)
          })
        },
        onCancel() {}
      })
    },
    /** 解锁按钮操作 */
    handleUnlock() {
      const that = this
      const username = this.selectName
      const ip = this.selectIp
      const successMessage = this.$t('log.logininfo.lock.success', { username: username })
      this.$confirm({
        title: this.$t('log.logininfor.confirm.unlock.user', { username: username }),
        onOk() {
          return unlockLogininfor(username, ip)
            .then(() => {
              that.$message.success(successMessage, 3)
            })
            .catch(() => {})
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/logininfor/export`,
            {
              ...that.queryParam
            },
            `logininfor_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
