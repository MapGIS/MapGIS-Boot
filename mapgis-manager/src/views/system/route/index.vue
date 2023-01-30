<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('id.suffix', { content: this.$t('route') })" prop="routeId">
                <a-input
                  v-model="queryParam.routeId"
                  :placeholder="$t('please.prefix.input', { content: $t('id.suffix', { content: this.$t('route') }) })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('dev.route.service.url')" prop="uri">
                <a-input
                  v-model="queryParam.uri"
                  :placeholder="$t('please.prefix.input', { content: $t('dev.route.service.url') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('dev.route.status')" prop="status">
                  <a-select
                    :placeholder="$t('please.prefix.select', { content: $t('dev.route.status') })"
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:route:add']">
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['system:route:edit']"
        >
          <a-icon type="edit" />{{ $t('modify') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:route:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:route:export']">
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
      <!-- 增加修改 -->
      <create-form ref="createForm" :statusOptions="statusOptions" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="gatewayRouteId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a-divider type="vertical" v-hasPermi="['system:route:edit']" />
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:route:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" v-hasPermi="['system:route:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:route:remove']">
            <a-icon type="delete" />{{ $t('delete') }}
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
import { listRoute, delRoute } from '@/api/system/route'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Route',
  components: {
    CreateForm
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
      // 路由状态字典
      statusOptions: [],
      // 查询参数
      queryParam: {
        routeId: null,
        uri: null,
        status: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('route') }),
          dataIndex: 'routeId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('dev.route.service.url'),
          dataIndex: 'uri',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('order'),
          dataIndex: 'orderNum',
          width: '5%',
          align: 'center'
        },
        {
          title: this.$t('dev.route.status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          width: '10%',
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
    this.getDicts('sys_normal_disable').then(response => {
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
    /** 查询网关路由列表 */
    getList() {
      this.loading = true
      listRoute(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 路由状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        routeId: undefined,
        uri: undefined,
        status: undefined,
        pageNum: 1,
        pageSize: 10
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
      this.ids = this.selectedRows.map(item => item.gatewayRouteId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const gatewayRouteIds = row.gatewayRouteId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + gatewayRouteIds + this.$t('is.data'),
        onOk() {
          return delRoute(gatewayRouteIds).then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/route/export`,
            {
              ...that.queryParam
            },
            `route_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
