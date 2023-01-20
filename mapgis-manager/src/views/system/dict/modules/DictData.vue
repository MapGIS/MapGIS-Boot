<template>
  <div>
    <create-data-form ref="createDataForm" :statusOptions="statusOptions" :dictType="dictType" @ok="getList" />
    <a-card :bordered="false" class="dict-data-background">
      <div class="table-operations">
        <a-button
          type="primary"
          size="small"
          @click="$refs.createDataForm.handleAdd()"
          v-hasPermi="['system:dict:add']"
          ghost
        >
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="danger"
          :disabled="multiple"
          @click="handleDelete"
          size="small"
          v-hasPermi="['system:dict:remove']"
          ghost
        >
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <table-setting
          :style="{ float: 'right' }"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList"
        />
      </div>
      <a-table
        :loading="loading"
        rowKey="dictCode"
        :size="tableSize"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :scroll="{ y: 300 }"
        :pagination="false"
      >
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createDataForm.handleUpdate(record)" v-hasPermi="['system:dict:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)" v-hasPermi="['system:dict:remove']">
            <a-icon type="delete" />{{ $t('delete') }}
          </a>
        </span>
      </a-table>
    </a-card>
  </div>
</template>

<script>
import { listData, delData } from '@/api/system/dict/data'
import CreateDataForm from './CreateDataForm'

export default {
  name: 'DictData',
  props: {
    dictType: {
      type: String,
      required: true
    },
    dictId: {
      type: Number,
      required: true
    }
  },
  components: {
    CreateDataForm
  },
  data() {
    return {
      list: [],
      tableSize: 'small',
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
      queryParam: {
        dictName: undefined,
        dictType: undefined,
        status: undefined
      },
      columns: [
        {
          title: '字典编码',
          dataIndex: 'dictCode',
          align: 'center'
        },
        {
          title: '字典标签',
          dataIndex: 'dictLabel',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '字典键值',
          dataIndex: 'dictValue',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '字典排序',
          dataIndex: 'dictSort',
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
          title: this.$t('remark'),
          dataIndex: 'remark',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('create.time'),
          dataIndex: 'createTime',
          ellipsis: true,
          scopedSlots: { customRender: 'createTime' },
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
    this.queryParam.dictType = this.dictType
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {
    getList() {
      this.loading = true
      listData(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 状态字典翻译
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
        dictName: undefined,
        dictType: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.dictCode)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const dictCodes = row.dictCode || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: '当前选中字典编码为' + dictCodes + this.$t('is.data'),
        onOk() {
          return delData(dictCodes).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
          })
        },
        onCancel() {}
      })
    }
  }
}
</script>

<style lang="less" scoped>
.dict-data-background {
  background: @background-color-light;
  border: 1px solid @shadow-color;
}
</style>
