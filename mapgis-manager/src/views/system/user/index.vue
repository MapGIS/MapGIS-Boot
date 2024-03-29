<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <a-row :gutter="24">
        <a-col v-if="enableDepartment" :span="4">
          <!-- 部门树 -->
          <dept-tree ref="deptTree" :deptOptions="deptOptions" @select="clickDeptNode" />
        </a-col>
        <a-col :span="enableDepartment ? 20 : 24">
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
                  <a-form-item :label="$t('role')">
                    <a-select
                      :placeholder="$t('please.prefix.select', { content: $t('role') })"
                      v-model="queryParam.roleId"
                      style="width: 100%"
                      allow-clear
                    >
                      <a-select-option v-for="(r, index) in roleOptions" :key="index" :value="r.roleId">{{
                        r.roleName
                      }}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <template v-if="advanced">
                  <a-col :md="8" :sm="24">
                    <a-form-item :label="$t('status')" prop="status">
                      <a-select
                        :placeholder="$t('please.prefix.select', { content: $t('status') })"
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
            <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:user:add']">
              <a-icon type="plus" />{{ $t('add') }}
            </a-button>
            <a-button
              type="primary"
              :disabled="single"
              @click="$refs.createForm.handleUpdate(undefined, ids)"
              v-hasPermi="['system:user:edit']"
            >
              <a-icon type="edit" />{{ $t('modify') }}
            </a-button>
            <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:user:remove']">
              <a-icon type="delete" />{{ $t('delete') }}
            </a-button>
            <a-button
              type="dashed"
              @click="$refs.importExcel.importExcelHandleOpen()"
              v-hasPermi="['system:user:import']"
            >
              <a-icon type="import" />{{ $t('import') }}
            </a-button>
            <a-button type="primary" @click="handleExport" v-hasPermi="['system:user:export']">
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
          <!-- 创建/编辑用户,单独封装了组件 -->
          <create-form
            ref="createForm"
            :deptOptions="deptOptions"
            :statusOptions="statusOptions"
            :sexOptions="sexOptions"
            @ok="getList"
            @select-tree="getDeptTree"
          />
          <!-- 修改密码抽屉 -->
          <reset-password ref="resetPassword" />
          <!-- 分配角色模态框 -->
          <auth-role ref="authRole" @ok="getList" />
          <!-- 上传文件 -->
          <import-excel ref="importExcel" @ok="getList" />
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
            <span slot="roleNames" slot-scope="text, record">
              {{ parseRoles(record.roles, record.userGroups) }}
            </span>
            <span slot="status" slot-scope="text, record">
              <a-popconfirm
                :ok-text="$t('yes')"
                :cancel-text="$t('no')"
                @confirm="confirmHandleStatus(record)"
                @cancel="cancelHandleStatus(record)"
                :disabled="record.userId === 1"
              >
                <span slot="title">
                  {{
                    $t('security.user.confirm.switch.state', {
                      operation: record.status === '1' ? $t('enable').toLowerCase() : $t('disable').toLowerCase(),
                      username: record.nickName
                    })
                  }}
                </span>
                <a-switch
                  :checked-children="$t('on')"
                  :un-checked-children="$t('off')"
                  :checked="record.status == 0"
                  :disabled="record.userId === 1"
                />
              </a-popconfirm>
            </span>
            <span slot="createTime" slot-scope="text, record">
              {{ parseTime(record.createTime) }}
            </span>
            <span slot="operation" slot-scope="text, record" v-if="record.userId !== 1">
              <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:user:edit']">
                <a-icon type="edit" />
                {{ $t('modify') }}
              </a>
              <a-divider type="vertical" v-hasPermi="['system:user:remove']" />
              <a @click="handleDelete(record)" v-hasPermi="['system:user:remove']">
                <a-icon type="delete" />
                {{ $t('delete') }}
              </a>
              <a-divider type="vertical" v-hasPermi="['system:user:resetPwd', 'system:user:edit']" />
              <a-dropdown v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                <a class="ant-dropdown-link" @click="e => e.preventDefault()">
                  <a-icon type="double-right" />
                  {{ $t('more') }}
                </a>
                <a-menu slot="overlay">
                  <a-menu-item v-hasPermi="['system:user:resetPwd']">
                    <a @click="$refs.resetPassword.handleResetPwd(record)">
                      <a-icon type="key" />
                      {{ $t('security.user.modify.password') }}
                    </a>
                  </a-menu-item>
                  <a-menu-item v-hasPermi="['system:user:edit']">
                    <a @click="$refs.authRole.handleAuthRole(record)">
                      <a-icon type="check-circle" />
                      {{ $t('security.user.assign.roles') }}
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
        </a-col>
      </a-row>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listUser, delUser, changeUserStatus, getUser, deptTreeSelect } from '@/api/system/user'
import AuthRole from './modules/AuthRole'
import ResetPassword from './modules/ResetPassword'
import CreateForm from './modules/CreateForm'
import ImportExcel from './modules/ImportExcel'
import DeptTree from './modules/DeptTree'
import { tableMixin } from '@/store/table-mixin'
import { mapGetters } from 'vuex'

export default {
  name: 'User',
  components: {
    AuthRole,
    ResetPassword,
    CreateForm,
    ImportExcel,
    DeptTree
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
      // 角色选项
      roleOptions: [],
      // 状态数据字典
      statusOptions: [],
      sexOptions: [],
      // 部门树选项
      deptOptions: [
        {
          id: 0,
          label: '',
          children: []
        }
      ],
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      columns: [
        {
          title: this.$t('id.suffix', { content: this.$t('user') }),
          dataIndex: 'userId',
          width: '8%',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('username'),
          dataIndex: 'userName',
          align: 'center'
        },
        {
          title: this.$t('user.nickname'),
          dataIndex: 'nickName',
          align: 'center'
        },
        {
          title: this.$t('department'),
          dataIndex: 'dept.deptName',
          scopedSlots: { customRender: 'dept.deptName' },
          align: 'center'
        },
        {
          title: this.$t('role'),
          dataIndex: 'roles',
          scopedSlots: { customRender: 'roleNames' },
          align: 'center'
        },
        {
          title: this.$t('create.time'),
          dataIndex: 'createTime',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('update.time'),
          dataIndex: 'updateTime',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('security.user.last.login.address'),
          dataIndex: 'loginIp',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('security.user.last.login.time'),
          dataIndex: 'loginDate',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('user.info'),
          dataIndex: 'remark',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          width: '8%',
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '25%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {
    this.getList()
    this.getDeptTree()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_user_sex').then(response => {
      this.sexOptions = response.data
    })
    getUser().then(response => {
      this.roleOptions = response.roles
    })
  },
  computed: {
    ...mapGetters(['enableDepartment'])
  },
  watch: {
    enableDepartment: {
      immediate: true,
      handler(nv) {
        if (nv) {
          if (
            !this.columns.find(column => {
              return column.dataIndex === 'dept.deptName'
            })
          ) {
            this.columns.splice(3, 0, {
              title: this.$t('department'),
              dataIndex: 'dept.deptName',
              scopedSlots: { customRender: 'dept.deptName' },
              align: 'center'
            })
          }
        } else {
          this.columns = this.columns.filter(column => {
            return column.dataIndex !== 'dept.deptName'
          })
        }
      }
    }
  },
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data
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
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
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
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    clickDeptNode(deptId) {
      this.queryParam.deptId = deptId
      this.handleQuery()
    },
    /* 用户状态修改 */
    confirmHandleStatus(row) {
      row.status = row.status === '0' ? '1' : '0'
      changeUserStatus(row.userId, row.status)
        .then(() => {
          const successMessage = row.status === '1' ? this.$t('enable.success') : this.$t('disable.success')
          this.$message.success(successMessage, 3)
        })
        .catch(function () {
          const exceptionMessage = row.status === '1' ? this.$t('enable.exception') : this.$t('disable.exception')
          this.$message.error(exceptionMessage, 3)
        })
    },
    cancelHandleStatus(row) {},
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const userIds = row.userId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + userIds + this.$t('is.data'),
        onOk() {
          return delUser(userIds).then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/user/export`,
            {
              ...that.queryParam
            },
            `user_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    },
    parseRoles(roles, userGroups) {
      const roleNames = roles.map(role => role.roleName)
      const roleNameSet = new Set(roleNames)
      if (userGroups) {
        userGroups.forEach(userGroup => {
          if (userGroup.roles) {
            userGroup.roles.forEach(role => {
              roleNameSet.add(role.roleName)
            })
          }
        })
      }
      return Array.from(roleNameSet).join(',')
    }
  }
}
</script>
