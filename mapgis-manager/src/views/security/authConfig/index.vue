<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="登录平台" prop="type">
                <a-input v-model="queryParam.type" placeholder="请输入登录平台" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('login.name')" prop="name">
                <a-input v-model="queryParam.name" placeholder="请输入登录名称" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:authConfig:add']">
          <a-icon type="plus" />{{ $t('add') }}
        </a-button>
        <a-button
          type="primary"
          :disabled="single"
          @click="$refs.createForm.handleUpdate(undefined, ids)"
          v-hasPermi="['system:authConfig:edit']"
        >
          <a-icon type="edit" />{{ $t('modify') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:authConfig:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:authConfig:export']">
          <a-icon type="download" />{{ $t('export') }}
        </a-button>
        <!-- 暂时不提供第三方登录角色配置，默认就是第三方用户角色（roleId为2） -->
        <a-button type="primary" @click="thirdUseConfigShow = true" v-hasPermi="['system:config:query']" v-if="false">
          <a-icon type="setting" />第三方登录角色配置
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
      <create-form ref="createForm" :statusOptions="statusOptions" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="configId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record, undefined)" v-hasPermi="['system:authConfig:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" v-hasPermi="['system:authConfig:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:authConfig:remove']">
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
    <a-modal
      title="第三方登录角色配置"
      :visible="thirdUseConfigShow"
      :footer="null"
      @cancel="thirdUseConfigShow = false"
    >
      <third-user-config />
    </a-modal>
  </page-header-wrapper>
</template>

<script>
import { listAuthConfig, delAuthConfig } from '@/api/system/authConfig'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'
import ThirdUserConfig from './modules/ThirdUserConfig'

export default {
  name: 'AuthConfig',
  components: {
    CreateForm,
    ThirdUserConfig
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
      // 状态字典
      statusOptions: [],
      // 查询参数
      queryParam: {
        type: null,
        name: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '登录平台',
          dataIndex: 'type',
          width: '10%',
          align: 'center'
        },
        {
          title: this.$t('login.name'),
          dataIndex: 'name',
          width: '15%',
          align: 'center'
        },
        {
          title: '客户端ID',
          dataIndex: 'clientId',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '回调地址',
          dataIndex: 'redirectUri',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('status'),
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          width: '6%',
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '12%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ],
      thirdUseConfigShow: false
    }
  },
  filters: {},
  created() {
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
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
    /** 查询第三方登录配置列表 */
    getList() {
      this.loading = true
      listAuthConfig(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        type: undefined,
        name: undefined,
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
      this.ids = this.selectedRows.map(item => item.configId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const configIds = row.configId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + configIds + this.$t('is.data'),
        onOk() {
          return delAuthConfig(configIds).then(() => {
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/authConfig/export`,
            {
              ...that.queryParam
            },
            `authConfig_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
