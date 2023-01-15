<template>
  <page-header-wrapper>
    <a-spin tip="Loading..." :spinning="!show">
      <div v-if="!show" style="height: 500px; width: 100%" />
    </a-spin>
    <a-space v-if="show" direction="vertical" style="width: 100%">
      <a-card :bordered="false">
        <div class="server-base-info" v-if="data.sys">
          <a-icon type="setting" style="margin-right: 5px" />
          <span> {{ `${$t('system')}： ${data.sys.osFullInfo}` }}</span>
          <span> {{ `IP：${data.sys.ip}` }} </span>
          <span> {{ `${$t('monitor.server.running.desc')}：${data.jvm.runTime}` }} </span>
          <a-icon type="sync" class="icon-refresh" @click="onRefreshServerInfo" />
        </div>
      </a-card>

      <a-row :gutter="[10, 10]">
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card
            :title="$t('monitor.server.cpu.monitor')"
            :bordered="false"
            class="monitor-server-center-row-col-card"
          >
            <a-tooltip>
              <template #title>
                <div>{{ `${$t('monitor.server.cpu.system.usage')}：${data.cpu.sys}%` }}</div>
                <div>{{ `${$t('monitor.server.cpu.user.usage')}：${data.cpu.user}%` }}</div>
                <div>{{ `${$t('monitor.server.cpu.total.usage')}：${data.cpu.total}%` }}</div>
                <div>{{ `${$t('monitor.server.cpu.wait.ratio')}：${data.cpu.wait}%` }}</div>
                <div>{{ `${$t('monitor.server.cpu.idle.ratio')}：${data.cpu.free}%` }}</div>
              </template>
              <a-progress type="dashboard" :stroke-color="getProgressColor(data.cpu.total)" :percent="data.cpu.total" />
            </a-tooltip>
            <div>{{ $t('monitor.server.cpu.total.usage') }}</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card
            :title="$t('monitor.server.memory.info')"
            :bordered="false"
            class="monitor-server-center-row-col-card"
          >
            <a-tooltip>
              <template #title>
                <div>{{ `${$t('monitor.server.memory.total')}：${data.memory.total}` }}</div>
                <div>{{ `${$t('monitor.server.memory.used')}：${data.memory.used}` }}</div>
                <div>{{ `${$t('monitor.server.memory.free')}： ${data.memory.free}` }}</div>
                <div>{{ `${$t('monitor.server.memory.usage')}： ${data.memory.usage}%` }}</div>
              </template>
              <a-progress
                type="dashboard"
                :stroke-color="getProgressColor(data.memory.usage)"
                :percent="data.memory.usage"
              />
            </a-tooltip>
            <div>{{ $t('monitor.server.memory.usage') }}</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card :title="$t('monitor.server.disk.info')" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>{{ `${$t('monitor.server.disk.usage')}：${data.disk.total}` }}</div>
                <div>{{ `${$t('monitor.server.disk.used')}：${data.disk.used}` }}</div>
                <div>{{ `${$t('monitor.server.disk.free')}：${data.disk.free}` }}</div>
                <div>{{ `${$t('monitor.server.disk.usage')}：${data.disk.usage}%` }}</div>
              </template>
              <a-progress
                type="dashboard"
                :stroke-color="getProgressColor(data.disk.usage)"
                :percent="data.disk.usage"
              />
            </a-tooltip>
            <div>{{ $t('monitor.server.disk.usage') }}</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card
            :title="$t('monitor.server.jvm.monitor')"
            :bordered="false"
            class="monitor-server-center-row-col-card"
          >
            <a-tooltip>
              <template #title>
                <div>{{ `${$t('monitor.server.jvm.memory.total')}：${data.jvm.total}` }}</div>
                <div>{{ `${$t('monitor.server.jvm.memory.used')}：${data.jvm.used}` }}</div>
                <div>{{ `${$t('monitor.server.jvm.memory.free')}：${data.jvm.free}` }}</div>
                <div>{{ `${$t('monitor.server.jvm.memory.usage')}：${data.jvm.usage}%` }}</div>
              </template>
              <a-progress type="dashboard" :stroke-color="getProgressColor(data.jvm.usage)" :percent="data.jvm.usage" />
            </a-tooltip>
            <div>{{ $t('monitor.server.jvm.memory.usage') }}</div>
          </a-card>
        </a-col>
      </a-row>

      <div style="float: right">
        <a-range-picker
          :show-time="{ format: 'HH:mm:ss' }"
          format="YYYY-MM-DD HH:mm:ss"
          :placeholder="[$t('start.time'), $t('end.time')]"
          :allowClear="false"
          v-model="defaultTimeRange"
          @ok="onTimeRangeOk"
        />
        <a-select
          style="width: 160px; padding-left: 8px"
          @change="handleTimeRangeSelectChange"
          v-model="defaultTimeRangeItem"
        >
          <a-icon slot="suffixIcon" type="history" />
          <a-select-option v-for="range in timeRanges" :value="range.value" :key="range.value">
            {{ range.label }}
          </a-select-option>
        </a-select>
      </div>
      <div>
        <a-row :gutter="[10, 10]">
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card :bordered="false">
              <div id="serverCpuMoniter" style="width: 100%; max-height: 800px; min-height: 250px" />
            </a-card>
          </a-col>
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card :bordered="false">
              <div id="serverMemoryMoniter" style="width: 100%; max-height: 800px; min-height: 250px" />
            </a-card>
          </a-col>
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card :bordered="false">
              <div id="serverDiskMoniter" style="width: 100%; max-height: 800px; min-height: 250px" />
            </a-card>
          </a-col>
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card :bordered="false">
              <div id="serverNetworkMoniter" style="width: 100%; max-height: 800px; min-height: 250px" />
            </a-card>
          </a-col>
        </a-row>
      </div>

      <a-card :title="$t('monitor.server.cpu.monitor')" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item :label="$t('monitor.server.cpu.name')">
            {{ data.cpu.name }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.cpu.number')">
            {{ data.cpu.package }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.cpu.physical.number')">
            {{ data.cpu.physical }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.cpu.logical.number')">
            {{ data.cpu.logical }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card :title="$t('monitor.server.jvm.info')" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item :label="$t('monitor.server.jvm.name')">
            {{ data.jvm.name }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.version')">
            {{ data.jvm.version }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.start.time')">
            {{ data.jvm.startTime }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.run.time')">
            {{ data.jvm.runTime }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.java.version')">
            {{ data.jvm.javaVersion }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.java.path')">
            {{ data.jvm.javaPath }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.server.path')" :span="2">
            {{ data.sys.userDir }}
          </a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.jvm.run.args')" :span="2">
            {{ data.jvm.inputArgs }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card :title="$t('monitor.server.server.info')" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item :label="$t('monitor.server.server.name')">{{ data.sys.name }}</a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.server.os')">{{ data.sys.os }}</a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.server.ip')">{{ data.sys.ip }}</a-descriptions-item>
          <a-descriptions-item :label="$t('monitor.server.server.arch')">{{ data.sys.arch }}</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card :title="$t('monitor.server.disk.status')" :bordered="false">
        <a-table
          :size="tableSize"
          rowKey="dir"
          :columns="fileColumns"
          :data-source="data.files"
          :pagination="false"
          :bordered="tableBordered"
        >
          <span slot="total" slot-scope="text">
            {{ text }}
          </span>
          <span slot="free" slot-scope="text">
            {{ text }}
          </span>
          <span slot="used" slot-scope="text">
            {{ text }}
          </span>
          <span slot="usage" slot-scope="text">
            <div :style="{ color: text > 80 ? 'red' : '' }">
              <a-icon type="warning" style="color: #ffcc00" v-if="text > 80" />
              {{ text }}<code>%</code>
            </div>
          </span>
        </a-table>
      </a-card>
    </a-space>
  </page-header-wrapper>
</template>

<script>
import * as echarts from 'echarts'
import { getServer, getServerRange } from '@/api/system/monitor'
import { tableMixin } from '@/store/table-mixin'
import moment from 'moment'
import { mapState } from 'vuex'

export default {
  name: 'Server',
  mixins: [tableMixin],
  data() {
    return {
      intervalTime: 3000,
      intervalTimeRangeTime: 30000,
      cpuEchart: '',
      memoryEchart: '',
      diskEChart: '',
      networkEChart: '',
      cpuRecords: [],
      memoryRecords: [],
      diskRecords: {
        readData: [],
        writeData: []
      },
      networkRecords: {
        sendData: [],
        receiveData: []
      },
      show: false,
      isEchartsInited: false,
      monitor: null,
      timeRangeMonitor: null,
      data: {},
      dataTimeRange: {},
      fileColumns: [
        {
          title: this.$t('monitor.server.disk.status.file.system.mountpoint'),
          dataIndex: 'dir',
          ellipsis: true
        },
        {
          title: this.$t('monitor.server.disk.status.file.system.type'),
          dataIndex: 'type'
        },
        {
          title: this.$t('monitor.server.disk.status.file.system.name'),
          dataIndex: 'name',
          ellipsis: true
        },
        {
          title: this.$t('monitor.server.disk.status.total'),
          dataIndex: 'total',
          scopedSlots: { customRender: 'total' }
        },
        {
          title: this.$t('monitor.server.disk.status.free'),
          dataIndex: 'free',
          scopedSlots: { customRender: 'free' }
        },
        {
          title: this.$t('monitor.server.disk.status.used'),
          dataIndex: 'used',
          scopedSlots: { customRender: 'used' }
        },
        {
          title: this.$t('monitor.server.disk.status.usage'),
          dataIndex: 'usage',
          scopedSlots: { customRender: 'usage' }
        }
      ],
      defaultTimeRange: [],
      defaultTimeRangeItem: 'recent1h',
      beginTime: '',
      endTime: '',
      timeRanges: [
        {
          label: this.$t('monitor.server.recent.5m'),
          value: 'recent5m'
        },
        {
          label: this.$t('monitor.server.recent.15m'),
          value: 'recent15m'
        },
        {
          label: this.$t('monitor.server.recent.30m'),
          value: 'recent30m'
        },
        {
          label: this.$t('monitor.server.recent.1h'),
          value: 'recent1h'
        },
        {
          label: this.$t('monitor.server.recent.3h'),
          value: 'recent3h'
        },
        {
          label: this.$t('monitor.server.recent.6h'),
          value: 'recent6h'
        },
        {
          label: this.$t('monitor.server.recent.12h'),
          value: 'recent12h'
        },
        {
          label: this.$t('monitor.server.recent.24h'),
          value: 'recent24h'
        },
        {
          label: this.$t('monitor.server.today'),
          value: 'today'
        },
        {
          label: this.$t('monitor.server.recent.2d'),
          value: 'recent2d'
        },
        {
          label: this.$t('monitor.server.recent.7d'),
          value: 'recent7d'
        },
        {
          label: this.$t('monitor.server.recent.1month'),
          value: 'recent1month'
        },
        {
          label: this.$t('monitor.server.recent.3month'),
          value: 'recent3month'
        }
      ]
    }
  },
  computed: {
    ...mapState({ theme: state => state.app.theme })
  },
  watch: {
    theme(val) {
      if (this.theme === 'dark') {
        this.color = '#1f1f1f'
      } else if (this.theme === 'light') {
        this.color = '#1f1f1f'
      } else if (this.theme === 'night') {
        this.color = '#ffffff'
      }
      this.isEchartsInited = false
      this.getServerInfoTimeRange()
    }
  },
  destroyed() {
    clearInterval(this.monitor)
    if (this.timeRangeMonitor) {
      clearInterval(this.timeRangeMonitor)
    }
  },
  mounted() {
    if (this.theme === 'dark') {
      this.color = '#1f1f1f'
    } else if (this.theme === 'light') {
      this.color = '#1f1f1f'
    } else if (this.theme === 'night') {
      this.color = '#ffffff'
    }
    this.getServerInfo()
    this.handleTimeRangeSelectChange(this.defaultTimeRangeItem)
    this.monitor = window.setInterval(() => {
      this.getServerInfo()
    }, this.intervalTime)
  },
  methods: {
    initEcharts() {
      if (!this.show || this.isEchartsInited) {
        return
      }

      const container1 = document.getElementById('serverCpuMoniter')
      const container2 = document.getElementById('serverMemoryMoniter')
      const container3 = document.getElementById('serverDiskMoniter')
      const container4 = document.getElementById('serverNetworkMoniter')

      if (container1 != null && container2 != null && container3 != null && container4 != null) {
        container1.style.height = (window.innerHeight - 300) * 0.5 + 'px'
        container2.style.height = (window.innerHeight - 300) * 0.5 + 'px'
        container3.style.height = (window.innerHeight - 300) * 0.5 + 'px'
        container4.style.height = (window.innerHeight - 300) * 0.5 + 'px'
      }
      const ec = echarts
      this.cpuEchart = ec.init(container1)
      this.memoryEChart = ec.init(container2)
      this.diskEChart = ec.init(container3)
      this.networkEChart = ec.init(container4)
      this.initEchartsCpuModule(this.cpuEchart)
      this.initEchartsMemoryModule(this.memoryEChart)
      this.initEchartsDiskModule(this.diskEChart)
      this.initEchartsNetworkModule(this.networkEChart)

      this.isEchartsInited = true
    },
    onRefreshServerInfo() {
      this.getServerInfo()
      this.getServerInfoTimeRange()
    },
    getServerInfo() {
      const self = this
      getServer().then(response => {
        self.data = response.data
        self.show = true

        self.$nextTick(() => {
          // 监控信息获取完毕后初始化Echarts
          if (!this.isEchartsInited) {
            self.getServerInfoTimeRange()
          }
        })
      })
    },
    getServerInfoTimeRange() {
      // 刷新时间
      this.refreshTimeRange(this.defaultTimeRangeItem)
      const self = this
      getServerRange({ beginTime: this.beginTime, endTime: this.endTime }).then(response => {
        self.dataTimeRange = response.data
        self.initEcharts()
        if (!this.isEchartsInited) {
          return false
        }

        // 渲染图表
        self.cpuRecords = []
        for (let i = 0; i < self.dataTimeRange.cpu.length; i++) {
          self.cpuRecords.push({
            name: self.dataTimeRange.cpu[i].time,
            value: [self.dataTimeRange.cpu[i].time, self.dataTimeRange.cpu[i].value.toFixed(2)]
          })
        }
        self.memoryRecords = []
        for (let i = 0; i < self.dataTimeRange.memory.length; i++) {
          self.memoryRecords.push({
            name: self.dataTimeRange.memory[i].time,
            value: [self.dataTimeRange.memory[i].time, self.dataTimeRange.memory[i].value.toFixed(2)]
          })
        }
        self.diskRecords = { readData: [], writeData: [] }
        for (let i = 0; i < self.dataTimeRange.diskRead.length; i++) {
          self.diskRecords.readData.push({
            name: self.dataTimeRange.diskRead[i].time,
            value: [self.dataTimeRange.diskRead[i].time, self.dataTimeRange.diskRead[i].value.toFixed(2)]
          })
        }
        for (let i = 0; i < self.dataTimeRange.diskWrite.length; i++) {
          self.diskRecords.writeData.push({
            name: self.dataTimeRange.diskWrite[i].time,
            value: [self.dataTimeRange.diskWrite[i].time, self.dataTimeRange.diskWrite[i].value.toFixed(2)]
          })
        }
        self.networkRecords = { sendData: [], receiveData: [] }
        for (let i = 0; i < self.dataTimeRange.networkUp.length; i++) {
          self.networkRecords.sendData.push({
            name: self.dataTimeRange.networkUp[i].time,
            value: [self.dataTimeRange.networkUp[i].time, self.dataTimeRange.networkUp[i].value.toFixed(2)]
          })
        }
        for (let i = 0; i < self.dataTimeRange.networkDown.length; i++) {
          self.networkRecords.receiveData.push({
            name: self.dataTimeRange.networkDown[i].time,
            value: [self.dataTimeRange.networkDown[i].time, self.dataTimeRange.networkDown[i].value.toFixed(2)]
          })
        }
        self.cpuEchart.setOption({
          series: [
            {
              data: self.cpuRecords
            }
          ]
        })
        self.memoryEChart.setOption({
          series: [
            {
              data: self.memoryRecords
            }
          ]
        })
        self.diskEChart.setOption({
          series: [
            {
              data: self.diskRecords.readData
            },
            {
              data: self.diskRecords.writeData
            }
          ]
        })
        self.networkEChart.setOption({
          series: [
            {
              data: self.networkRecords.sendData
            },
            {
              data: self.networkRecords.receiveData
            }
          ]
        })
      })
    },
    // 获取颜色 30以下绿色，30-80蓝色， 80往上红色
    getProgressColor(value) {
      const values = Number(value)
      if (values <= 30) {
        return '#49aa19'
      } else if ((values > 30) & (values <= 80)) {
        return '#1890fe'
      } else if (values > 80) {
        return '#e60000'
      }
    },
    onTimeRangeOk(value) {
      this.formatTimeRange(value)
      this.defaultTimeRangeItem = ''
      if (this.timeRangeMonitor) {
        clearInterval(this.timeRangeMonitor)
        this.timeRangeMonitor = null
      }
    },
    handleTimeRangeSelectChange(value) {
      this.getServerInfoTimeRange()
      if (this.timeRangeMonitor === null) {
        this.timeRangeMonitor = window.setInterval(() => {
          this.getServerInfoTimeRange()
        }, this.intervalTimeRangeTime)
      }
    },
    formatTimeRange(timeRange) {
      this.beginTime = timeRange[0].format('YYYY-MM-DD HH:mm:ss')
      this.endTime = timeRange[1].format('YYYY-MM-DD HH:mm:ss')
    },
    refreshTimeRange(timeRangeItem) {
      if (timeRangeItem !== '') {
        const timeRange = this[timeRangeItem]()
        this.formatTimeRange(timeRange)
        this.defaultTimeRange = timeRange
      }
    },
    recent5m() {
      return [moment().subtract(5, 'm'), moment()]
    },
    recent15m() {
      return [moment().subtract(15, 'm'), moment()]
    },
    recent30m() {
      return [moment().subtract(30, 'm'), moment()]
    },
    recent1h() {
      return [moment().subtract(1, 'h'), moment()]
    },
    recent3h() {
      return [moment().subtract(3, 'h'), moment()]
    },
    recent6h() {
      return [moment().subtract(6, 'h'), moment()]
    },
    recent12h() {
      return [moment().subtract(12, 'h'), moment()]
    },
    recent24h() {
      return [moment().subtract(24, 'h'), moment()]
    },
    today() {
      return [moment().startOf('day'), moment()]
    },
    recent2d() {
      return [moment().startOf('day').subtract(2, 'day'), moment()]
    },
    recent7d() {
      return [moment().startOf('day').subtract(1, 'weeks'), moment()]
    },
    recent1month() {
      return [moment().startOf('day').subtract(1, 'month'), moment()]
    },
    recent3month() {
      return [moment().startOf('day').subtract(3, 'month'), moment()]
    },
    // cpu折线图
    initEchartsCpuModule(chartObject) {
      const self = this
      const lineColor = '#03a9f4'
      self.cpuRecords = []
      const option = {
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        title: {
          text: this.$t('monitor.server.cpu.usage'),
          left: 'center',
          textStyle: {
            fontSize: 15,
            color: this.color
          }
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (params.length > 0 && params[0]) {
              // cpu实时数据
              const totalTips = params[0]
              const totalValue = totalTips.value ? totalTips.value[1] + '%' : ''
              return self.$t('monitor.server.time.usage', { time: totalTips.name.toLocaleString(), usage: totalValue })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: false,
              readOnly: false
            },
            magicType: {
              show: false,
              type: ['line', 'bar']
            },
            restore: {
              show: false
            },
            saveAsImage: {
              show: true
            }
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'time',
            splitLine: {
              show: false
            },
            scale: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            max: '100',
            axisLabel: {
              formatter: function (value, index) {
                return value + '%'
              },
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            },
            splitLine: {
              show: false
            },
            scale: true
          }
        ],
        series: [
          {
            name: this.$t('monitor.server.usage'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor,
              opacity: 1
            },
            areaStyle: {
              color: lineColor,
              opacity: 0.5
            },
            data: self.cpuRecords,
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.maximum')
                }
              ],
              itemStyle: {
                color: lineColor
              },
              symbolSize: 40
            }
          }
        ]
      }
      chartObject.setOption(option)
    },
    // 缓存折线图
    initEchartsMemoryModule(chartObject) {
      const self = this
      const lineColor = '#4CAF50'
      self.memoryRecords = []
      const option = {
        title: {
          text: this.$t('monitor.server.memory.usage'),
          subtext: '',
          left: 'center',
          textStyle: {
            fontSize: 15,
            color: this.color
          }
        },
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (params.length > 0 && params[0]) {
              // cpu实时数据
              const totalTips = params[0]
              const totalValue = totalTips.value ? totalTips.value[1] + '%' : ''
              return self.$t('monitor.server.time.usage', { time: totalTips.name.toLocaleString(), usage: totalValue })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: false,
              readOnly: false
            },
            magicType: {
              show: false,
              type: ['line', 'bar']
            },
            restore: {
              show: false
            },
            saveAsImage: {
              show: true
            }
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'time',
            splitLine: {
              show: false
            },
            scale: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            max: '100',
            axisLabel: {
              formatter: function (value, index) {
                return value + '%'
              },
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            },
            splitLine: {
              show: false
            },
            scale: true
          }
        ],
        series: [
          {
            name: this.$t('monitor.server.usage'),
            type: 'line',
            data: self.memoryRecords,
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor,
              opacity: 1
            },
            areaStyle: {
              color: lineColor,
              opacity: 0.5
            },
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.maximum')
                }
              ],
              itemStyle: {
                color: lineColor
              },
              symbolSize: 40
            }
          }
        ]
      }
      chartObject.setOption(option)
    },
    // 磁盘折线图
    initEchartsDiskModule(chartObject) {
      const self = this
      const lineColor = '#03a9f4'
      const lineColor2 = '#4CAF50'
      self.diskRecords = {
        readData: [],
        writeData: []
      }
      const option = {
        title: {
          text: this.$t('monitor.server.disk.read.write.speed'),
          subtext: '',
          left: 'center',
          textStyle: {
            fontSize: 15,
            color: this.color
          }
        },
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (params.length > 1 && params[0] && params[1]) {
              // 读取数据
              const readTips = params[0]
              const readValue = readTips.value ? readTips.value[1] + 'Kb/s' : ''
              // 写入数据
              const writeTips = params[1]
              const writeValue = writeTips.value ? writeTips.value[1] + 'Kb/s' : ''
              return self.$t('monitor.server.disk.time.usage', {
                time: writeTips.name.toLocaleString(),
                read: readValue,
                write: writeValue
              })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        color: ['#03a9f4', '#4CAF50'],
        legend: {
          data: [this.$t('monitor.server.disk.read.speed'), this.$t('monitor.server.disk.write.speed')],
          y: 'top',
          orient: 'vertical',
          right: '24px',
          textStyle: {
            color: this.color
          }
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: false,
              readOnly: false
            },
            magicType: {
              show: false,
              type: ['line', 'bar']
            },
            restore: {
              show: false
            },
            saveAsImage: {
              show: true
            }
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'time',
            splitLine: {
              show: false
            },
            scale: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            axisLabel: {
              formatter: function (value, index) {
                return value.toFixed(0)
              },
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            },
            splitLine: {
              show: false
            },
            scale: true
          }
        ],
        series: [
          // 读取速度
          {
            name: this.$t('monitor.server.disk.read.speed'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor,
              opacity: 1
            },
            areaStyle: {
              color: lineColor,
              opacity: 0.5
            },
            data: self.diskRecords.readData,
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.maximum')
                }
              ],
              itemStyle: {
                color: lineColor
              },
              symbolSize: 40
            }
          },
          // 写入速度
          {
            name: this.$t('monitor.server.disk.write.speed'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor2,
              opacity: 1
            },
            areaStyle: {
              color: lineColor2,
              opacity: 0.5
            },
            data: self.diskRecords.writeData,
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.maximum')
                }
              ],
              itemStyle: {
                color: lineColor2
              },
              symbolSize: 40
            }
          }
        ]
      }
      chartObject.setOption(option)
    },
    // 网络折线图
    initEchartsNetworkModule(chartObject) {
      const self = this
      const lineColor = '#03a9f4'
      const lineColor2 = '#4CAF50'
      self.networkRecords = {
        sendData: [],
        receiveData: []
      }
      // 设置参数
      const option = {
        title: {
          text: this.$t('monitor.server.network.trans.rate'),
          subtext: '',
          left: 'center',
          textStyle: {
            fontSize: 15,
            color: this.color
          }
        },
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          formatter: function (params) {
            if (params.length > 1 && params[0] && params[1]) {
              // 发送数据
              const sendTips = params[0]
              const sendValue = sendTips.value ? sendTips.value[1] + 'Kb/s' : ''
              // 接收数据
              const receiveTips = params[1]
              const receiveValue = receiveTips.value ? receiveTips.value[1] + 'Kb/s' : ''
              return self.$t('monitor.server.network.time.usage', {
                time: sendTips.name.toLocaleString(),
                send: sendValue,
                receive: receiveValue
              })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        color: ['#03a9f4', '#4CAF50'],
        legend: {
          data: [this.$t('monitor.server.network.send.rate'), this.$t('monitor.server.network.receive.rate')],
          y: 'top',
          orient: 'vertical',
          right: '24px',
          textStyle: {
            color: this.color
          }
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true
            },
            dataView: {
              show: false,
              readOnly: false
            },
            magicType: {
              show: false,
              type: ['line', 'bar']
            },
            restore: {
              show: false
            },
            saveAsImage: {
              show: true
            }
          }
        },
        calculable: true,
        xAxis: [
          {
            type: 'time',
            splitLine: {
              show: false
            },
            scale: true,
            axisLabel: {
              show: true,
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            min: 0,
            axisLabel: {
              formatter: function (value, index) {
                return value.toFixed(0)
              },
              textStyle: {
                color: this.color
              }
            },
            axisLine: {
              lineStyle: {
                color: this.color
              }
            },
            splitLine: {
              show: false
            },
            scale: true
          }
        ],
        series: [
          // 发送速率
          {
            name: this.$t('monitor.server.network.send.rate'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor,
              opacity: 1
            },
            areaStyle: {
              color: lineColor,
              opacity: 0.5
            },
            data: self.networkRecords.sendData,
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.network.send.maximum')
                }
              ],
              itemStyle: {
                color: lineColor
              },
              symbolSize: 40
            }
          },
          // 接收速率
          {
            name: this.$t('monitor.server.network.receive.rate'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            sampling: 'average',
            lineStyle: {
              color: lineColor2,
              opacity: 1
            },
            areaStyle: {
              color: lineColor2,
              opacity: 0.5
            },
            data: self.networkRecords.receiveData,
            markPoint: {
              data: [
                {
                  type: 'max',
                  name: this.$t('monitor.server.network.send.maximum')
                }
              ],
              itemStyle: {
                color: lineColor2
              },
              symbolSize: 40
            }
          }
        ]
      }
      chartObject.setOption(option)
    }
  }
}
</script>

<style lang="less" scoped>
.server-base-info {
  color: @text-color;
  font-size: 13px;
  line-height: 1.15;
  span {
    margin-right: 28px;
  }
  .icon-refresh {
    float: right;
    cursor: pointer;
    margin-left: 40px;
  }
}
.monitor-server-center-row-col-card {
  text-align: center;
}
</style>
