<template>
  <page-header-wrapper>
    <a-card :bordered="false">
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
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.selectUser.handleAuthUser()" v-hasPermi="['system:role:add']">
          <a-icon type="plus" />
          {{ $t('security.role.add.user') }}
        </a-button>
        <a-button
          type="danger"
          :loading="authing"
          :disabled="multiple"
          @click="cancelAuthUserAll"
          v-hasPermi="['system:role:remove']"
        >
          <a-icon type="delete" />
          {{ $t('security.role.cancel.batch.authorization') }}
        </a-button>
        <a-button type="primary" @click="back">
          <a-icon type="edit" />
          {{ $t('return') }}
        </a-button>
        <table-setting
          :style="{ float: 'right' }"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList"
        />
      </div>
      <select-user ref="selectUser" :roleId="queryParam.roleId" :statusOptions="statusOptions" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="userId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record" v-if="record.userId !== 1">
          <a @click="cancelAuthUser(record)" v-hasPermi="['system:role:remove']">
            <a-icon type="edit" />
            {{ $t('security.role.cancel.authorization') }}
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
import { allocatedUserList, authUserCancel, authUserCancelAll } from '@/api/system/role'
import CreateForm from './modules/CreateForm'
import CreateDataScopeForm from './modules/CreateDataScopeForm'
import SelectUser from './modules/SelectUser'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'AuthUser',
  components: {
    CreateForm,
    CreateDataScopeForm,
    SelectUser
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      authing: false,
      total: 0,
      // 状态数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        roleId: '',
        userName: undefined,
        phonenumber: undefined
      },
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
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '20%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {},
  mounted() {
    const roleId = this.$route.query && this.$route.query.roleId
    if (roleId) {
      this.queryParam.roleId = roleId
      this.getList()
      this.getDicts('sys_normal_disable').then(response => {
        this.statusOptions = response.data
      })
    }
  },
  computed: {},
  watch: {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询授权用户列表 */
    getList() {
      this.loading = true
      allocatedUserList(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
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
      this.ids = this.selectedRows.map(item => item.userId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    /** 取消授权按钮操作 */
    cancelAuthUser(row) {
      const that = this
      const userName = row.userName
      const roleId = this.queryParam.roleId
      this.$confirm({
        title: this.$t('security.role.confirm.cancel.user.role'),
        content: this.$t('security.role.currently.selected.user', { username: userName }),
        onOk() {
          const param = {
            userId: row.userId,
            roleId: roleId
          }
          return authUserCancel(param).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(that.$t('security.role.cancel.authorization.success'), 3)
          })
        },
        onCancel() {}
      })
    },
    /** 批量取消授权按钮操作 */
    cancelAuthUserAll() {
      const that = this
      const roleId = this.queryParam.roleId
      this.$confirm({
        title: this.$t('security.role.confirm.cancel.selected.user.role'),
        onOk() {
          const param = {
            roleId: roleId,
            userIds: that.ids
          }
          that.authing = true
          return authUserCancelAll(param)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(that.$t('security.role.cancel.authorization.success'), 3)
            })
            .finally(() => {
              that.authing = false
            })
        },
        onCancel() {}
      })
    },
    back() {
      this.$router.push({ path: '/security/role' })
    }
  }
}
</script>
