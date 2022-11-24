<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="关键字">
                <a-input v-model="queryParam.keyword" placeholder="请输入关键字" allow-clear />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="类型">
                <a-select placeholder="类型" v-model="queryParam.logId" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in logIdOptions" :key="index" :value="d.value">{{
                    d.label
                  }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="级别">
                  <a-select placeholder="日志级别" v-model="queryParam.level" style="width: 100%" allow-clear>
                    <a-select-option v-for="(l, index) in levelOptions" :key="index" :value="l.value">{{
                      l.label
                    }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="时间">
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
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
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
          <a-icon type="download" />导出
        </a-button>
        <a-button type="primary" @click="logConfigShow = true" v-hasPermi="['system:systemlog:config']">
          <a-icon type="setting" />日志配置
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
            {{ '更多' }}
          </a-button>
        </template>
      </a-table>
      <a-modal
        title="日志详情"
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
              <span style="font-size: 14px">{{ '总获取到' }} {{ list.length }} {{ '条日志' }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <a-button style="float: right" :disabled="!hasMore" type="primary" size="small" @click="getEarlyLogs">
              {{ '获取更早的' }}
            </a-button>
          </a-col>
        </a-row>
      </div>
    </a-card>
    <a-modal title="日志配置" :visible="logConfigShow" :footer="null" @cancel="logConfigShow = false">
      <log-config @ok="logConfigShow = false" />
    </a-modal>
  </page-header-wrapper>
</template>

<script>
import { list } from '@/api/system/systemlog'
import { tableMixin } from '@/store/table-mixin'
import LogConfig from './modules/LogConfig.vue'

export default {
  name: 'Systemlog',
  components: { LogConfig },
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
        logId: window._CONFIG['productName'],
        position: undefined,
        count: 100,
        keyword: undefined,
        level: undefined
      },
      columns: [
        {
          title: '编号',
          dataIndex: 'id',
          width: '8%',
          align: 'center'
        },
        {
          title: '日期',
          dataIndex: 'createTime',
          align: 'center'
        },
        {
          title: '级别',
          dataIndex: 'level',
          width: '8%',
          align: 'center'
        },
        {
          title: '进程ID',
          dataIndex: 'pid',
          align: 'center',
          visible: false
        },
        {
          title: '线程名',
          dataIndex: 'thread',
          ellipsis: true,
          align: 'center',
          visible: false
        },
        {
          title: '日志类',
          dataIndex: 'logger',
          align: 'center'
        },
        {
          title: '日志信息',
          dataIndex: 'message',
          align: 'left',
          scopedSlots: { customRender: 'message' },
          width: 500
        }
      ],
      logIdOptions: [
        {
          label: window._CONFIG['productName'],
          value: window._CONFIG['productName']
        }
      ],
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
      ],
      logConfigShow: false
    }
  },
  filters: {},
  created() {
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
      this.handleQuery()
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          that.download(
            `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/export`,
            null,
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
