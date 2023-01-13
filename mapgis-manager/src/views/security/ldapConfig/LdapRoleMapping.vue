<template>
  <a-card :bordered="false">
    <!-- 操作 -->
    <div class="table-operations">
      <a-button type="primary" @click="handleAdd()" v-hasPermi="['system:config:edit']">
        <a-icon type="plus" />{{ $t('add') }}
      </a-button>
      <a-button
        type="primary"
        :disabled="single"
        @click="handleUpdate(undefined, externalRoles)"
        v-hasPermi="['system:config:edit']"
      >
        <a-icon type="edit" />{{ $t('modify') }}
      </a-button>
      <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:edit']">
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
    <!-- 增加修改 -->
    <create-form
      ref="createForm"
      :systemRoles="systemRoles"
      @add="handleAddCallback"
      @update="handleUpdateCallback"
      @ok="getList"
    />
    <!-- 数据展示 -->
    <a-table
      :loading="loading"
      :size="tableSize"
      rowKey="externalRole"
      :columns="columns"
      :data-source="list"
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange
      }"
      :pagination="false"
      :bordered="tableBordered"
    >
      <span slot="systemRoleIds" slot-scope="text, record" v-hasPermi="['system:user:query']">
        {{ parseSystemRoleIds(record.systemRoleIds) }}
      </span>
      <span slot="operation" slot-scope="text, record">
        <a @click="handleUpdate(record, undefined)" v-hasPermi="['system:config:edit']">
          <a-icon type="edit" />{{ $t('modify') }}
        </a>
        <a-divider type="vertical" v-hasPermi="['system:config:edit']" />
        <a @click="handleDelete(record)" v-hasPermi="['system:config:edit']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a>
      </span>
    </a-table>
  </a-card>
</template>

<script>
import { getConfigByKey, updateConfig } from '@/api/system/config'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'
import { getUser } from '@/api/system/user'

export default {
  name: 'Post',
  components: {
    CreateForm
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
      externalRoles: [],
      loading: false,
      columns: [
        {
          title: 'LDAP群组',
          dataIndex: 'externalRole',
          width: '25%',
          align: 'center'
        },
        {
          title: '映射角色',
          dataIndex: 'systemRoleIds',
          scopedSlots: { customRender: 'systemRoleIds' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '15%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      configInfo: {},
      configValue: {},
      systemRoles: []
    }
  },
  created() {
    getUser().then(response => {
      this.getList()
      const roleList = response.roles
      this.systemRoles = roleList.map(role => {
        return {
          key: role.roleId.toString(),
          title: role.roleName,
          roleId: role.roleId,
          roleName: role.roleName
        }
      })
    })
  },
  computed: {},
  watch: {},
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true
      getConfigByKey('security.ldap').then(response => {
        this.configInfo = response.data
        this.configValue = JSON.parse(this.configInfo.configValue)
        this.list = this.configValue.roleMapping || []
        this.loading = false
      })
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.externalRoles = this.selectedRows.map(item => item.externalRole)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const externalRoles = row.externalRole ? [row.externalRole] : this.externalRoles
      const roleMapping = this.list.filter(item => {
        return !externalRoles.includes(item.externalRole)
      })
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: '当前选中LDAP群组为' + externalRoles + this.$t('is.data'),
        onOk() {
          that.configInfo.configValue = JSON.stringify({ ...that.configValue, roleMapping })
          return updateConfig(that.configInfo).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
          })
        },
        onCancel() {}
      })
    },
    handleAdd() {
      this.$refs.createForm.handleAdd(
        this.list.map(item => {
          return item.externalRole
        })
      )
    },
    handleUpdate(row, externalRoles) {
      const externalRole = row ? row.externalRole : externalRoles[0]

      const roleMappingItem = this.list.find(item => {
        return item.externalRole === externalRole
      })

      this.$refs.createForm.handleUpdate(
        roleMappingItem,
        this.list.map(item => {
          return item.externalRole
        })
      )
    },
    handleAddCallback(roleMappingItem, resolve) {
      this.list.push(roleMappingItem)
      this.configInfo.configValue = JSON.stringify({ ...this.configValue, roleMapping: this.list })
      updateConfig(this.configInfo).then(() => {
        resolve()
      })
    },
    handleUpdateCallback(roleMappingItem, resolve) {
      const findItem = this.list.find(item => {
        return item.externalRole === roleMappingItem.externalRole
      })
      if (findItem) {
        findItem.systemRoleIds = roleMappingItem.systemRoleIds
      }

      this.configInfo.configValue = JSON.stringify({ ...this.configValue, roleMapping: this.list })
      updateConfig(this.configInfo).then(() => {
        resolve()
      })
    },
    parseSystemRoleIds(systemRoleIds) {
      return systemRoleIds
        .map(roleId => {
          const findRole = this.systemRoles.find(role => {
            return role.roleId === roleId
          })
          return findRole ? findRole.roleName : undefined
        })
        .filter(roleName => roleName)
        .join(',')
    }
  }
}
</script>
