<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('message.notice.title')">
                <a-input v-model="queryParam.noticeTitle" :placeholder="$t('please.input')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('operation.user')">
                <a-input v-model="queryParam.createBy" :placeholder="$t('please.input')" allow-clear />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('message.notice.type')">
                  <a-select
                    :placeholder="$t('please.select')"
                    v-model="queryParam.noticeType"
                    style="width: 100%"
                    allow-clear
                  >
                    <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{
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
        <a-button type="primary" @click="handleAdd()" v-hasPermi="['system:notice:add']">
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="handleUpdate(undefined, ids)"
          v-hasPermi="['system:notice:edit']"
        >
          <a-icon type="edit" />{{ $t('modify') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:notice:remove']">
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
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="noticeId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="noticeType" slot-scope="text, record">
          {{ typeFormat(record) }}
        </span>
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="handleUpdate(record, undefined)" v-hasPermi="['system:notice:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" v-hasPermi="['system:notice:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:notice:remove']">
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
import { listNotice, delNotice } from '@/api/system/notice'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Notice',
  components: {},
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
      typeOptions: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined,
        createBy: undefined,
        status: undefined
      },
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('notice') }),
          dataIndex: 'noticeId',
          align: 'center'
        },
        {
          title: this.$t('message.notice.title'),
          dataIndex: 'noticeTitle',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('message.notice.type'),
          dataIndex: 'noticeType',
          scopedSlots: { customRender: 'noticeType' },
          align: 'center'
        },
        {
          title: this.$t('status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: this.$t('operation.user'),
          dataIndex: 'createBy',
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
    this.getDicts('sys_notice_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_notice_type').then(response => {
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
    /** 查询公告列表 */
    getList() {
      this.loading = true
      listNotice(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 公告状态字典翻译
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 公告类型字典翻译
    typeFormat(row) {
      return this.selectDictLabel(this.typeOptions, row.noticeType)
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
        noticeTitle: undefined,
        createBy: undefined,
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
      this.ids = this.selectedRows.map(item => item.noticeId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const noticeIds = row.noticeId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + noticeIds + this.$t('is.data'),
        onOk() {
          return delNotice(noticeIds).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
          })
        },
        onCancel() {}
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({
        name: 'NoticeForm',
        params: {
          id: undefined,
          formTitle: this.$t('add.suffix', { content: this.$t('notice') })
        }
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      const noticeId = row ? row.noticeId : ids
      this.$router.push({
        name: 'NoticeForm',
        params: {
          id: noticeId,
          formTitle: this.$t('modify.suffix', { content: this.$t('notice') })
        }
      })
    }
  }
}
</script>
