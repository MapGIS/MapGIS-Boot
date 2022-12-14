<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="微应用名称" prop="name">
                <a-input v-model="queryParam.name" placeholder="请输入微应用名称" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="微应用入口" prop="entry">
                <a-input v-model="queryParam.entry" placeholder="请输入微应用入口" allow-clear />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="微应用路由" prop="activeRule">
                  <a-input v-model="queryParam.activeRule" placeholder="请输入微应用路由" allow-clear />
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'" />
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:microApp:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['system:microApp:edit']"
        >
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:microApp:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:microApp:export']">
          <a-icon type="download" />导出
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
      <create-form ref="createForm" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="microAppId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="operation" slot-scope="text, record">
          <a-divider type="vertical" v-hasPermi="['system:microApp:edit']" />
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:microApp:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-hasPermi="['system:microApp:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:microApp:remove']"> <a-icon type="delete" />删除 </a>
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
        :showTotal="total => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listMicroApp, delMicroApp } from '@/api/system/microApp'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'MicroApp',
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
      // 查询参数
      queryParam: {
        name: null,
        entry: null,
        activeRule: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '微应用编号',
          dataIndex: 'microAppId',
          width: '10%',
          align: 'center'
        },
        {
          title: '微应用名称',
          dataIndex: 'name',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '微应用入口',
          dataIndex: 'entry',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '微应用路由',
          dataIndex: 'activeRule',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '18%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {
    this.getList()
  },
  computed: {},
  watch: {},
  methods: {
    /** 查询微应用路由列表 */
    getList() {
      this.loading = true
      listMicroApp(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        name: undefined,
        entry: undefined,
        activeRule: undefined,
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
      this.ids = this.selectedRows.map(item => item.microAppId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this
      const microAppIds = row.microAppId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + microAppIds + '的数据',
        onOk() {
          return delMicroApp(microAppIds).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success('删除成功', 3)
          })
        },
        onCancel() {}
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          that.download(
            `${window._CONFIG['apiPathManagerPrefix']}/system/microApp/export`,
            {
              ...that.queryParam
            },
            `microApp_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
