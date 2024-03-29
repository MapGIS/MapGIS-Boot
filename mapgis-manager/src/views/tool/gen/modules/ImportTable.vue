<template>
  <a-modal
    ref="createModal"
    :title="$t('dev.gen.import.table')"
    :width="900"
    :visible="visible"
    :confirm-loading="submitLoading"
    @cancel="close"
    @ok="confirm"
  >
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item :label="$t('dev.gen.table.name')">
              <a-input v-model="queryParam.tableName" :placeholder="$t('please.input')" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item :label="$t('dev.gen.table.comment')">
              <a-input v-model="queryParam.tableComment" :placeholder="$t('please.input')" allow-clear />
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button @click="handleQuery" type="primary">{{ $t('query') }}</a-button>
              <a-button @click="resetQuery" style="margin-left: 8px">{{ $t('reset') }}</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="page-header-content">
      <a-card :bordered="false" class="content">
        <a-table
          :loading="loading"
          :size="tableSize"
          rowKey="tableName"
          :columns="columns"
          :data-source="list"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange
          }"
          :scroll="{ y: tableHeight }"
          :pagination="false"
          :bordered="tableBordered"
        >
          <span slot="createTime" slot-scope="text, record">
            {{ parseTime(record.createTime) }}
          </span>
          <span slot="updateTime" slot-scope="text, record">
            {{ parseTime(record.updateTime) }}
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
    </div>
  </a-modal>
</template>
<script>
import { listDbTable, importTable } from '@/api/tool/gen'
import { tableMixin } from '@/store/table-mixin'
export default {
  name: 'ImportTable',
  props: {},
  mixins: [tableMixin],
  data() {
    return {
      // 表格数据
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 500 + 'px',
      // 选中表数组
      tableNames: [],
      loading: false,
      total: 0,
      // 当前控件配置:
      submitLoading: false,
      visible: false,
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      },
      // 表格属性
      columns: [
        {
          title: this.$t('dev.gen.table.name'),
          dataIndex: 'tableName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('dev.gen.table.comment'),
          dataIndex: 'tableComment',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('create.time'),
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          align: 'center'
        },
        {
          title: this.$t('update.time'),
          dataIndex: 'updateTime',
          scopedSlots: { customRender: 'updateTime' },
          align: 'center'
        }
      ]
    }
  },
  created() {
    this.getList()
  },
  computed: {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    // 查询表数据
    getList() {
      this.loading = true
      listDbTable(this.queryParam).then(res => {
        if (res.code === 200) {
          this.list = res.rows
          this.total = res.total
          this.loading = false
        }
      })
    },
    // 关闭模态框
    close() {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      }
    },
    // 打开抽屉(由外面的组件调用)
    show() {
      this.visible = true
      this.getList()
    },
    // 确认导入
    confirm() {
      this.submitLoading = true
      importTable({ tables: this.tableNames.join(',') })
        .then(res => {
          this.$message.success(res.msg)
          this.visible = false
          this.$emit('ok')
        })
        .finally(() => {
          this.submitLoading = false
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
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
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
      this.tableNames = this.selectedRows.map(item => item.tableName)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    }
  }
}
</script>
