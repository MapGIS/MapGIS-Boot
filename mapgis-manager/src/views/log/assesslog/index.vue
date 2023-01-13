<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="客户端IP" prop="ipaddr">
                <a-input v-model="queryParam.ipaddr" placeholder="请输入客户端IP" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="开始时间">
                <a-range-picker
                  style="width: 100%"
                  v-model="daterangeAccessTime"
                  valueFormat="YYYY-MM-DD"
                  format="YYYY-MM-DD"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="返回状态码" prop="responseStatus">
                  <a-input v-model="queryParam.responseStatus" placeholder="请输入返回状态码" allow-clear />
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
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:assesslog:export']">
          <a-icon type="download" />{{ $t('log.accesslog.export.harfile') }}
        </a-button>
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
        rowKey="accessId"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="accessTime" slot-scope="text, record">
          {{ parseTime(record.accessTime) }}
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
import { listAssesslog } from '@/api/system/assesslog'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Assesslog',
  components: {},
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      // 高级搜索 展开/关闭
      advanced: false,
      loading: false,
      total: 0,
      // 开始时间时间范围
      daterangeAccessTime: [],
      // 查询参数
      queryParam: {
        ipaddr: null,
        url: null,
        responseStatus: null,
        accessTime: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: '开始时间',
          dataIndex: 'accessTime',
          scopedSlots: { customRender: 'accessTime' },
          ellipsis: true,
          width: '15%',
          align: 'center'
        },
        {
          title: '客户端IP',
          dataIndex: 'ipaddr',
          ellipsis: true,
          width: '12%',
          align: 'center'
        },
        {
          title: '请求URL',
          dataIndex: 'url',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '请求参数',
          dataIndex: 'queryString',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '返回状态码',
          dataIndex: 'responseStatus',
          ellipsis: true,
          width: '8%',
          align: 'center'
        },
        {
          title: '耗时(毫秒)',
          dataIndex: 'time',
          ellipsis: true,
          width: '8%',
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
    /** 查询Http访问日志列表 */
    getList() {
      this.loading = true
      this.queryParam.params = {}
      if (
        this.daterangeAccessTime !== null &&
        this.daterangeAccessTime !== '' &&
        this.daterangeAccessTime.length !== 0
      ) {
        this.queryParam.params['beginTime'] = this.daterangeAccessTime[0]
        this.queryParam.params['endTime'] = this.daterangeAccessTime[1]
      }
      listAssesslog(this.queryParam).then(response => {
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
      this.daterangeAccessTime = []
      this.queryParam = {
        ipaddr: undefined,
        url: undefined,
        responseStatus: undefined,
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
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 导出按钮操作 */
    handleExport() {
      const that = this
      this.$confirm({
        title: this.$t('confirm.export'),
        content: this.$t('export.condition.data.description'),
        onOk() {
          that.download(
            `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog/export`,
            {
              ...that.queryParam
            },
            `assesslog_${new Date().getTime()}.har`,
            false
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>
