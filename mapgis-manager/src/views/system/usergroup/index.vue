<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="用户组名称" prop="userGroupName">
                <a-input v-model="queryParam.userGroupName" placeholder="请输入用户组名称" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="创建时间">
                <a-range-picker
                  style="width: 100%"
                  v-model="daterangeCreateTime"
                  valueFormat="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:usergroup:add']">
          <a-icon type="plus" />新增
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['system:usergroup:edit']"
        >
          <a-icon type="edit" />修改
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:usergroup:remove']">
          <a-icon type="delete" />删除
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:usergroup:export']">
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
        rowKey="userGroupId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="roles" slot-scope="text, record">
          {{ getRoles(record.roles) }}
        </span>
        <span slot="users" slot-scope="text, record">
          {{ getUsers(record.users) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a-divider type="vertical" v-hasPermi="['system:usergroup:edit']" />
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:usergroup:edit']">
            <a-icon type="edit" />修改
          </a>
          <a-divider type="vertical" v-hasPermi="['system:usergroup:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:usergroup:remove']"> <a-icon type="delete" />删除 </a>
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
import { listUsergroup, delUsergroup } from '@/api/system/usergroup'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Usergroup',
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
      // 创建时间时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParam: {
        userGroupName: null,
        createTime: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '用户组编号',
          dataIndex: 'userGroupId',
          ellipsis: true,
          align: 'center',
          visible: false
        },
        {
          title: '用户组名称',
          dataIndex: 'userGroupName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '成员',
          dataIndex: 'users',
          scopedSlots: { customRender: 'users' },
          align: 'center',
          width: 330
        },
        {
          title: '关联角色',
          dataIndex: 'roles',
          scopedSlots: { customRender: 'roles' },
          align: 'center'
        },
        {
          title: '描述',
          dataIndex: 'remark',
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
    /** 查询用户组列表 */
    getList() {
      this.loading = true
      this.queryParam.params = {}
      if (
        this.daterangeCreateTime !== null &&
        this.daterangeCreateTime !== '' &&
        this.daterangeCreateTime.length !== 0
      ) {
        this.queryParam.params['beginCreateTime'] = this.daterangeCreateTime[0]
        this.queryParam.params['endCreateTime'] = this.daterangeCreateTime[1]
      }
      listUsergroup(this.queryParam).then(response => {
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
      this.daterangeCreateTime = []
      this.queryParam = {
        userGroupName: undefined,
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
      this.ids = this.selectedRows.map(item => item.userGroupId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this
      const userGroupIds = row.userGroupId || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + userGroupIds + '的数据',
        onOk() {
          return delUsergroup(userGroupIds).then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/usergroup/export`,
            {
              ...that.queryParam
            },
            `usergroup_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    },
    getRoles(roles) {
      return roles.map(t => t.roleName).join(',')
    },
    getUsers(users) {
      return users.map(t => t.userName).join(',')
    }
  }
}
</script>
