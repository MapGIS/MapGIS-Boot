<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('keyword')">
                <a-input
                  v-model="queryParam.keyword"
                  :placeholder="$t('please.prefix.input', { content: $t('keyword') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('type')">
                <a-select :placeholder="$t('type')" v-model="queryParam.logId" style="width: 100%">
                  <a-select-option v-for="(d, index) in logIdOptions" :key="index" :value="d.value">{{
                    d.label
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('level')">
                  <a-select
                    :placeholder="$t('log.systemlog.log.level')"
                    v-model="queryParam.level"
                    style="width: 100%"
                    allow-clear
                  >
                    <a-select-option v-for="(l, index) in levelOptions" :key="index" :value="l.value">{{
                      l.label
                    }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item :label="$t('time')">
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
        <a-button type="primary" @click="handleExport" v-hasPermi="['system:systemlog:export']">
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
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data-source="list"
        :pagination="false"
        :bordered="tableBordered"
        @change="handleTableChange"
        :row-class-name="tableRowClassName"
      >
        <template slot="message" slot-scope="text">
          {{ text.length > 1000 ? text.substr(0, 1000) + '...' : text }}
          <a-button
            v-if="text.length > 1000"
            type="link"
            style="float: right; border: none; box-shadow: none; background-color: rgba(225, 225, 225, 0)"
            size="small"
            @click="messageDetail(text)"
          >
            {{ $t('more') }}
          </a-button>
        </template>
      </a-table>
      <a-modal
        :title="$t('log.systemlog.log.detail')"
        :visible="detailVisible"
        :maskClosable="false"
        :footer="null"
        :width="900"
        @cancel="detailVisible = false"
      >
        <pre style="white-space: pre-wrap; line-height: 20px">{{ longLogMessage }}</pre>
      </a-modal>
      <div>
        <a-row style="height: 30px; margin-top: 15px">
          <a-col :span="12">
            <div v-if="list != null">
              <span style="font-size: 14px">{{ $t('log.systemlog.log.get.total', { total: list.length }) }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <a-button style="float: right" :disabled="!hasMore" type="primary" size="small" @click="getEarlyLogs">
              {{ $t('log.systemlog.log.get.earlier') }}
            </a-button>
          </a-col>
        </a-row>
      </div>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { getIds, list } from '@/api/system/systemlog'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Systemlog',
  components: {},
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      // 高级搜索 展开/关闭
      advanced: false,
      loading: false,
      position: '',
      hasMore: false,
      detailVisible: false,
      longLogMessage: '',
      // 日期范围
      dateRange: [],
      queryParam: {
        logId: undefined,
        position: undefined,
        count: 100,
        keyword: undefined,
        level: undefined
      },
      columns: [
        {
          title: this.$t('id'),
          dataIndex: 'id',
          width: '8%',
          align: 'center'
        },
        {
          title: this.$t('date'),
          dataIndex: 'createTime',
          align: 'center'
        },
        {
          title: this.$t('level'),
          dataIndex: 'level',
          width: '8%',
          align: 'center'
        },
        {
          title: this.$t('id.suffix', { content: this.$t('process') }),
          dataIndex: 'pid',
          align: 'center',
          visible: false
        },
        {
          title: this.$t('thread.name'),
          dataIndex: 'thread',
          ellipsis: true,
          align: 'center',
          visible: false
        },
        {
          title: this.$t('log.systemlog.log.class'),
          dataIndex: 'logger',
          align: 'center'
        },
        {
          title: this.$t('log.info'),
          dataIndex: 'message',
          align: 'left',
          scopedSlots: { customRender: 'message' },
          width: 500
        }
      ],
      logIdOptions: [],
      levelOptions: [
        {
          label: 'DEBUG',
          value: 'DEBUG'
        },
        {
          label: 'INFO',
          value: 'INFO'
        },
        {
          label: 'WARN',
          value: 'WARN'
        },
        {
          label: 'ERROR',
          value: 'ERROR'
        }
      ]
    }
  },
  filters: {},
  async created() {
    await getIds().then(response => {
      response.data.forEach(id => {
        this.logIdOptions.push({ label: id, value: id })
      })

      if (this.logIdOptions.length > 0) {
        this.queryParam.logId = this.logIdOptions[0].value
      }
    })
    this.getList()
  },
  computed: {},
  watch: {},
  methods: {
    handleTableChange(pagination, filters, sorter) {
      const sort = this.tableSorter(sorter)
      this.queryParam.orderByColumn = sort.orderByColumn
      this.queryParam.isAsc = sort.isAsc
      this.getList()
    },
    /** 查询登录日志列表 */
    getList(position) {
      this.loading = true
      this.queryParam.position = position
      if (this.dateRange !== null && this.dateRange !== '' && this.dateRange.length === 2) {
        this.queryParam.beginTime = this.dateRange[0]
        this.queryParam.endTime = this.dateRange[1]
      } else {
        this.queryParam.beginTime = undefined
        this.queryParam.endTime = undefined
      }
      list(this.queryParam).then(response => {
        this.list = response.data.logs
        this.list = this.list.map((item, index) => {
          return { id: index + 1, ...item }
        })
        this.hasMore = response.data.hasMore
        this.position = response.data.position
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.queryParam = {
        logId: undefined,
        position: undefined,
        count: 100,
        keyword: undefined,
        level: undefined
      }

      if (this.logIdOptions.length > 0) {
        this.queryParam.logId = this.logIdOptions[0].value
      }

      this.handleQuery()
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
            `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/export`,
            { logId: that.queryParam.logId },
            `systemlog_${new Date().getTime()}.zip`
          )
        },
        onCancel() {}
      })
    },
    /** 日志配置 */
    handleConfig() {},
    tableRowClassName(row) {
      const level = row.level
      if (level.toUpperCase() === 'WARN') {
        return 'log-warning-row'
      } else if (level.toUpperCase() === 'ERROR') {
        return 'log-error-row'
      }
      return ''
    },
    getEarlyLogs() {
      this.getList(this.position)
    },
    messageDetail(message) {
      this.longLogMessage = message
      this.detailVisible = true
    }
  }
}
</script>

<style lang="less">
.log-warning-row {
  color: rgb(255, 166, 0);
}

.log-error-row {
  color: #f56c6c;
}
</style>
