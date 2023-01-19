<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('role.name')">
                <a-input v-model="queryParam.roleName" :placeholder="$t('please.input')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('security.role.permission.character')">
                <a-input v-model="queryParam.roleKey" :placeholder="$t('please.input')" allow-clear />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('create.time')">
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:role:add']">
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['system:role:edit']"
        >
          <a-icon type="edit" />{{ $t('modify') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:role:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:role:export']">
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
      <create-form ref="createForm" @ok="getList" />

      <!-- 分配角色数据权限对话框 -->
      <create-data-scope-form ref="createDataScopeForm" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="roleId"
        :columns="columns"
        :data-source="list"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:role:edit']">
            <template v-if="record.isSys == 0">
              <a-icon type="edit" />
              修改
            </template>
            <template v-else>
              <a-icon type="search" />
              查看
            </template>
          </a>
          <template v-if="record.isSys == 0">
            <a-divider type="vertical" v-hasPermi="['system:role:remove']" />
          </template>
          <a @click="handleDelete(record)" v-hasPermi="['system:role:remove']" v-if="record.isSys == 0">
            <a-icon type="delete" />
            删除
          </a>
          <a-divider type="vertical" v-hasPermi="['system:role:edit']" />
          <a-dropdown v-hasPermi="['system:role:edit']">
            <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              <a-icon type="double-right" />
              更多
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="$refs.createDataScopeForm.handleDataScope(record)">
                  <a-icon type="lock" />
                  数据权限
                </a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleAuthUser(record)">
                  <a-icon type="user-add" />
                  分配用户
                </a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
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
import { listRole, delRole } from '@/api/system/role'
import CreateForm from './modules/CreateForm'
import CreateDataScopeForm from './modules/CreateDataScopeForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Role',
  components: {
    CreateForm,
    CreateDataScopeForm
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
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        roleKey: undefined
      },
      columns: [
        {
          title: '角色编号',
          dataIndex: 'roleId',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('role.name'),
          dataIndex: 'roleName',
          width: '18%',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '权限标识',
          dataIndex: 'roleKey',
          width: '12%',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '排序',
          dataIndex: 'roleSort',
          width: '5%',
          align: 'center'
        },
        {
          title: '角色描述',
          dataIndex: 'remark',
          align: 'left'
        },
        {
          title: this.$t('create.time'),
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          align: 'center',
          visible: false
        },
        {
          title: this.$t('operation'),
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
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询角色列表 */
    getList() {
      this.loading = true
      listRole(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
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
        roleName: undefined,
        roleKey: undefined
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
      this.ids = this.selectedRows.map(item => item.roleId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const roleIds = row.roleId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + roleIds + this.$t('is.data'),
        onOk() {
          return delRole(roleIds).then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/role/export`,
            {
              ...that.queryParam
            },
            `role_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    },
    /** 分配用户操作 */
    handleAuthUser(row) {
      const roleId = row.roleId
      this.$router.push({
        path: '/security/role/authUser',
        query: { roleId: roleId }
      })
    }
  }
}
</script>
