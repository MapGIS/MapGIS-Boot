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
              <a-form-item :label="$t('username')">
                <a-input
                  v-model="queryParam.userName"
                  :placeholder="$t('please.prefix.input', { content: $t('username') })"
                  allow-clear
                />
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
      <div class="table-operations" style="overflow: hidden">
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
        rowKey="tokenId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="loginTime" slot-scope="text, record">
          {{ parseTime(record.loginTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a-popconfirm
            :ok-text="$t('yes')"
            :cancel-text="$t('no')"
            @confirm="confirmHandleForceLogout(record)"
            @cancel="cancelHandleForceLogout(record)"
          >
            <span slot="title">
              {{ $t('monitor.online.forced.exit.user', { username: record.userName }) }}
            </span>
            <a v-hasPermi="['system:online:forceLogout']"> {{ $t('monitor.online.forced.exit') }} </a>
          </a-popconfirm>
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
import { list, forceLogout } from '@/api/system/online'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Online',
  components: {},
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      loading: false,
      total: 0,
      // 非多个禁用
      multiple: true,
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined
      },
      columns: [
        {
          title: this.$t('monitor.online.session.id'),
          dataIndex: 'tokenId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('login.name'),
          dataIndex: 'userName',
          align: 'center'
        },
        {
          title: this.$t('department.name'),
          dataIndex: 'deptName',
          align: 'center'
        },
        {
          title: this.$t('login.address'),
          dataIndex: 'ipaddr',
          ellipsis: true,
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
          title: this.$t('login.time'),
          dataIndex: 'loginTime',
          width: 180,
          scopedSlots: { customRender: 'loginTime' },
          align: 'center'
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
  },
  computed: {},
  watch: {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true
      list(this.queryParam).then(response => {
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
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined
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
    /** 强退按钮操作 */
    confirmHandleForceLogout(row) {
      forceLogout(row.tokenId)
        .then(() => {
          this.getList()
          this.$message.success(this.$t('monitor.online.has.forced.exited'), 3)
        })
        .catch(function () {
          this.$message.error(this.$t('exception.occurred'), 3)
        })
    },
    cancelHandleForceLogout(row) {}
  }
}
</script>
