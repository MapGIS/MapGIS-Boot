<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('menu.name')">
                <a-input v-model="queryParam.menuName" :placeholder="$t('please.input')" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('status')">
                <a-select
                  :placeholder="$t('please.select')"
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:menu:add']">
          <a-icon type="plus" />{{ $t('add') }}
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
        :menuOptions="menuOptions"
        :statusOptions="statusOptions"
        :visibleOptions="visibleOptions"
        @ok="getList"
        @select-tree="getTreeselect"
      />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="menuId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="icon" slot-scope="text">
          <a-icon :component="allIcon[text + 'Icon']" v-if="allIcon[text + 'Icon']" />
          <a-icon :type="text" v-if="!allIcon[text + 'Icon']" />
        </span>
        <span slot="status" slot-scope="text, record">
          {{ statusFormat(record) }}
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record)" v-hasPermi="['system:menu:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" v-hasPermi="['system:menu:add']" />
          <a @click="$refs.createForm.handleAdd(record)" v-hasPermi="['system:menu:add']">
            <a-icon type="plus" />{{ $t('add') }}
          </a>
          <a-divider type="vertical" v-if="record.menuId != 0" v-hasPermi="['system:menu:remove']" />
          <a @click="handleDelete(record)" v-if="record.menuId != 0" v-hasPermi="['system:menu:remove']">
            <a-icon type="delete" />{{ $t('delete') }}
          </a>
        </span>
      </a-table>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listMenu, delMenu } from '@/api/system/menu'
import CreateForm from './modules/CreateForm'
import allIcon from '@/core/icons'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Menu',
  components: {
    CreateForm
  },
  mixins: [tableMixin],
  data() {
    return {
      allIcon,
      iconVisible: false,
      list: [],
      // 部门树选项
      menuOptions: [],
      loading: false,
      // 状态数据字典
      statusOptions: [],
      visibleOptions: [],
      queryParam: {
        menuName: undefined,
        visible: undefined
      },
      columns: [
        {
          title: this.$t('menu.name'),
          dataIndex: 'menuName',
          ellipsis: true,
          width: '15%'
        },
        {
          title: this.$t('icon'),
          dataIndex: 'icon',
          scopedSlots: { customRender: 'icon' },
          width: '5%',
          align: 'center'
        },
        {
          title: this.$t('order'),
          dataIndex: 'orderNum',
          width: '5%',
          align: 'center'
        },
        {
          title: this.$t('security.role.permission.identification'),
          dataIndex: 'perms',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('security.menu.component.path'),
          dataIndex: 'component',
          scopedSlots: { customRender: 'component' },
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
          ellipsis: true,
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
  created() {
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_show_hide').then(response => {
      this.visibleOptions = response.data
    })
  },
  computed: {},
  watch: {},
  methods: {
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      listMenu(this.queryParam).then(response => {
        this.list = this.handleTree(response.data, 'menuId')
        this.loading = false
      })
    },
    // 字典状态字典翻译
    visibleFormat(row) {
      if (row.menuType === 'F') {
        return ''
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },
    statusFormat(row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        memuName: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = []
        const menu = { menuId: 0, menuName: this.$t('directory.home'), children: [] }
        menu.children = this.handleTree(response.data, 'menuId')
        this.menuOptions.push(menu)
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const menuId = row.menuId
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + menuId + this.$t('is.data'),
        onOk() {
          return delMenu(menuId).then(() => {
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
