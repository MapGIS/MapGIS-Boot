<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('department.name')">
                <a-input v-model="queryParam.deptName" :placeholder="$t('please.input')" allow-clear />
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
        <a-button type="primary" @click="$refs.createForm.handleAdd()" v-hasPermi="['system:dept:add']">
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
      <create-form ref="createForm" :deptOptions="deptOptions" @ok="getList" @select-tree="getTreeselect" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="deptId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.createForm.handleUpdate(record)" v-hasPermi="['system:dept:edit']">
            <a-icon type="edit" />{{ $t('modify') }}
          </a>
          <a-divider type="vertical" v-hasPermi="['system:dept:add']" />
          <a @click="$refs.createForm.handleAdd(record)" v-hasPermi="['system:dept:add']">
            <a-icon type="plus" />{{ $t('add') }}
          </a>
          <a-divider type="vertical" v-if="record.parentId != 0" v-hasPermi="['system:dept:remove']" />
          <a @click="handleDelete(record)" v-if="record.parentId != 0" v-hasPermi="['system:dept:remove']">
            <a-icon type="delete" />{{ $t('delete') }}
          </a>
        </span>
      </a-table>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listDept, delDept, listDeptExcludeChild } from '@/api/system/dept'
import CreateForm from './modules/CreateForm'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Dept',
  components: {
    CreateForm
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      // 部门树选项
      deptOptions: [],
      loading: false,
      queryParam: {
        deptName: undefined
      },
      columns: [
        {
          title: this.$t('department.name'),
          dataIndex: 'deptName'
        },
        {
          title: this.$t('order'),
          dataIndex: 'orderNum',
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
          width: '30%',
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
    /** 查询部门列表 */
    getList() {
      this.loading = true
      listDept(this.queryParam).then(response => {
        this.list = this.handleTree(response.data, 'deptId')
        this.loading = false
      })
    },
    /** 查询菜单下拉树结构 */
    getTreeselect(row) {
      if (!row) {
        listDept().then(response => {
          this.deptOptions = this.handleTree(response.data, 'deptId')
        })
      } else {
        listDeptExcludeChild(row.deptId).then(response => {
          this.deptOptions = this.handleTree(response.data, 'deptId')
        })
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        deptName: undefined
      }
      this.handleQuery()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const deptId = row.deptId
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + deptId + this.$t('is.data'),
        onOk() {
          return delDept(deptId).then(() => {
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
