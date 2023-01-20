<template>
  <a-modal
    ref="authRole"
    :title="$t('security.role.select.user')"
    :width="900"
    :visible="visible"
    :confirm-loading="submitLoading"
    @cancel="close"
    @ok="confirm"
  >
    <div class="page-header-content">
      <a-card :bordered="false" class="content">
        <!-- 条件搜索 -->
        <div class="table-page-search-wrapper">
          <a-form layout="inline">
            <a-row :gutter="48">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('username')">
                  <a-input v-model="queryParam.userName" :placeholder="$t('please.input')" allow-clear />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <span class="table-page-search-submitButtons">
                  <a-button type="primary" @click="handleQuery"><a-icon type="search" />{{ $t('query') }}</a-button>
                  <a-button style="margin-left: 8px" @click="resetQuery">
                    <a-icon type="redo" />{{ $t('reset') }}
                  </a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <a-table
          :loading="loading"
          size="small"
          rowKey="userId"
          :columns="columns"
          :data-source="list"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange
          }"
          :scroll="{ y: tableHeight }"
          :pagination="false"
        >
          <span slot="status" slot-scope="text, record">
            {{ statusFormat(record) }}
          </span>
          <span slot="createTime" slot-scope="text, record">
            {{ parseTime(record.createTime) }}
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
import { unallocatedUserList, authUserSelectAll } from '@/api/system/role'

export default {
  name: 'AuthUser',
  props: {
    roleId: {
      type: String,
      required: true
    },
    statusOptions: {
      type: Array,
      required: true
    }
  },
  components: {},
  data() {
    return {
      // 表格数据
      list: [],
      user: {},
      selectedRowKeys: [],
      selectedRows: [],
      // 表格的高度
      tableHeight: document.documentElement.scrollHeight - 500 + 'px',
      // 选中表数组
      userIds: [],
      loading: false,
      total: 0,
      // 当前控件配置:
      submitLoading: false,
      visible: false,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        roleId: undefined,
        userName: undefined,
        phonenumber: undefined
      },
      // 表格属性
      columns: [
        {
          title: this.$t('username'),
          dataIndex: 'userName',
          align: 'center'
        },
        {
          title: this.$t('user.nickname'),
          dataIndex: 'nickName',
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
          title: this.$t('create.time'),
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          ellipsis: true,
          align: 'center'
        }
      ]
    }
  },
  created() {},
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
      unallocatedUserList(this.queryParam).then(response => {
        this.list = response.rows
        // 过滤掉userId = 1的用户
        this.list = this.list.filter(user => {
          return user.userId !== 1
        })
        this.total = response.total
        this.loading = false
      })
    },
    // 关闭模态框
    close() {
      this.visible = false
      this.selectedRowKeys = []
      this.selectedRows = []
    },
    // 打开(由外面的组件调用)
    handleAuthUser() {
      this.queryParam.roleId = this.roleId
      this.visible = true
      this.getList()
    },
    // 确认
    confirm() {
      const param = {
        roleId: this.queryParam.roleId,
        userIds: this.userIds
      }
      this.submitLoading = true
      authUserSelectAll(param)
        .then(res => {
          this.$message.success(res.msg)
          this.visible = false
          this.$emit('ok')
        })
        .finally(() => {
          this.submitLoading = false
        })
    },
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
        roleId: this.queryParam.roleId,
        userName: undefined,
        phonenumber: undefined
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
      this.userIds = this.selectedRows.map(item => item.userId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    }
  }
}
</script>
