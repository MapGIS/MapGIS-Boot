<template>
  <page-header-wrapper>
    <a-spin tip="数据加载中..." :spinning="!show">
      <div v-if="!show" style="height: 500px; width: 100%" />
    </a-spin>
    <a-space v-if="show" direction="vertical" style="width: 100%">
      <a-card :bordered="false">
        <div class="server-base-info" v-if="data.sys">
          <a-icon type="setting" style="margin-right: 5px" />
          <span> 系统：{{ data.sys.osFullInfo }} </span>
          <span> IP：{{ data.sys.ip }} </span>
          <span> 服务器已不间断运行：{{ data.jvm.runTime }} </span>
          <a-icon type="sync" class="icon-refresh" @click="onRefreshServerInfo" />
        </div>
      </a-card>

      <a-row :gutter="[10, 10]">
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card title="CPU监控" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>CPU系统使用率：{{ data.cpu.sys }}%</div>
                <div>CPU用户使用率：{{ data.cpu.user }}%</div>
                <div>CPU当前总使用率：{{ data.cpu.total }}%</div>
                <div>CPU当前等待率：{{ data.cpu.wait }}%</div>
                <div>CPU当前空闲率：{{ data.cpu.free }}%</div>
              </template>
              <a-progress type="dashboard" :stroke-color="getProgressColor(data.cpu.total)" :percent="data.cpu.total" />
            </a-tooltip>
            <div>CPU当前总使用率</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card title="内存信息" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>内存总量：{{ data.memory.total }}</div>
                <div>内存已用：{{ data.memory.used }}</div>
                <div>内存剩余：{{ data.memory.free }}</div>
                <div>内存使用率：{{ data.memory.usage }}%</div>
              </template>
              <a-progress
                type="dashboard"
                :stroke-color="getProgressColor(data.memory.usage)"
                :percent="data.memory.usage"
              />
            </a-tooltip>
            <div>内存使用率</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card title="磁盘信息" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>磁盘总量：{{ data.disk.total }}</div>
                <div>磁盘已用：{{ data.disk.used }}</div>
                <div>磁盘剩余：{{ data.disk.free }}</div>
                <div>磁盘使用率：{{ data.disk.usage }}%</div>
              </template>
              <a-progress
                type="dashboard"
                :stroke-color="getProgressColor(data.disk.usage)"
                :percent="data.disk.usage"
              />
            </a-tooltip>
            <div>磁盘使用率</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="12" :lg="6" :xl="6">
          <a-card title="JVM监控" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>JVM总分配内存：{{ data.jvm.total }}</div>
                <div>JVM已用内存：{{ data.jvm.used }}</div>
                <div>JVM剩余内存：{{ data.jvm.free }}</div>
                <div>JVM内存使用率：{{ data.jvm.usage }}%</div>
              </template>
              <a-progress type="dashboard" :stroke-color="getProgressColor(data.jvm.usage)" :percent="data.jvm.usage" />
            </a-tooltip>
            <div>JVM内存使用率</div>
          </a-card>
        </a-col>
      </a-row>

      <div style="float: right">
        <a-range-picker
          :show-time="{ format: 'HH:mm:ss' }"
          format="YYYY-MM-DD HH:mm:ss"
          :placeholder="['开始时间', '结束时间']"
          :allowClear="false"
          v-model="defaultTimeRange"
          @ok="onTimeRangeOk"
        />
        <a-select
          style="width: 120px; padding-left: 8px"
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

      <a-card title="CPU监控" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item label="CPU名称">{{ data.cpu.name }}</a-descriptions-item>
          <a-descriptions-item label="CPU数量">{{ data.cpu.package }}</a-descriptions-item>
          <a-descriptions-item label="CPU物理核心数">{{ data.cpu.physical }}</a-descriptions-item>
          <a-descriptions-item label="CPU逻辑核心数">{{ data.cpu.logical }}</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card title="JVM信息" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item label="JVM名称">{{ data.jvm.name }}</a-descriptions-item>
          <a-descriptions-item label="JVM版本">{{ data.jvm.version }}</a-descriptions-item>
          <a-descriptions-item label="JVM启动时间">{{ data.jvm.startTime }}</a-descriptions-item>
          <a-descriptions-item label="JVM运行时长">{{ data.jvm.runTime }}</a-descriptions-item>
          <a-descriptions-item label="Java版本">{{ data.jvm.javaVersion }}</a-descriptions-item>
          <a-descriptions-item label="Java安装路径">{{ data.jvm.javaPath }}</a-descriptions-item>
          <a-descriptions-item label="服务器路径" :span="2">{{ data.sys.userDir }}</a-descriptions-item>
          <a-descriptions-item label="运行参数" :span="2">{{ data.jvm.inputArgs }}</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card title="服务器信息" :bordered="false">
        <a-descriptions size="middle" :column="2" bordered>
          <a-descriptions-item label="服务器名称">{{ data.sys.name }}</a-descriptions-item>
          <a-descriptions-item label="服务器操作系统">{{ data.sys.os }}</a-descriptions-item>
          <a-descriptions-item label="服务器IP">{{ data.sys.ip }}</a-descriptions-item>
          <a-descriptions-item label="服务器架构">{{ data.sys.arch }}</a-descriptions-item>
        </a-descriptions>
      </a-card>

      <a-card title="磁盘状态" :bordered="false">
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
          title: '盘符路径',
          dataIndex: 'dir',
          ellipsis: true
        },
        {
          title: '文件系统',
          dataIndex: 'type'
        },
        {
          title: '盘符名称',
          dataIndex: 'name',
          ellipsis: true
        },
        {
          title: '总大小',
          dataIndex: 'total',
          scopedSlots: { customRender: 'total' }
        },
        {
          title: '可用大小',
          dataIndex: 'free',
          scopedSlots: { customRender: 'free' }
        },
        {
          title: '已用大小',
          dataIndex: 'used',
          scopedSlots: { customRender: 'used' }
        },
        {
          title: '已用百分比',
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
          label: '近5分钟',
          value: 'recent5m'
        },
        {
          label: '近15分钟',
          value: 'recent15m'
        },
        {
          label: '近30分钟',
          value: 'recent30m'
        },
        {
          label: '近1小时',
          value: 'recent1h'
        },
        {
          label: '近3小时',
          value: 'recent3h'
        },
        {
          label: '近6小时',
          value: 'recent6h'
        },
        {
          label: '近12小时',
          value: 'recent12h'
        },
        {
          label: '近24小时',
          value: 'recent24h'
        },
        {
          label: '当日',
          value: 'today'
        },
        {
          label: '近两天',
          value: 'recent2d'
        },
        {
          label: '近七天',
          value: 'recent7d'
        },
        {
          label: '近一月',
          value: 'recent1month'
        },
        {
          label: '近三月',
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
          text: 'CPU使用率',
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
              return '时刻' + '：' + totalTips.name.toLocaleString() + '  ' + '使用率' + '：' + totalValue
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
            name: '使用率',
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
                  name: '最大值'
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
          text: '内存使用率',
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
              return '时刻' + '：' + totalTips.name.toLocaleString() + '  ' + '使用率' + '：' + totalValue
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
            name: '使用率',
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
                  name: '最大值'
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
          text: '磁盘读写速度',
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
              return (
                '时刻' +
                '：' +
                writeTips.name.toLocaleString() +
                '  ' +
                '读取速度' +
                '：' +
                readValue +
                '  ' +
                '写入速度' +
                '：' +
                writeValue
              )
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        color: ['#03a9f4', '#4CAF50'],
        legend: {
          data: ['读取速度', '写入速度'],
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
            name: '读取速度',
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
                  name: '写入速度'
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
            name: '写入速度',
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
                  name: '最大值'
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
          text: '网络传输速率',
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
              return (
                '时刻' +
                '：' +
                sendTips.name.toLocaleString() +
                '  ' +
                '发送速率' +
                '：' +
                sendValue +
                '  ' +
                '接收速率' +
                '：' +
                receiveValue
              )
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        color: ['#03a9f4', '#4CAF50'],
        legend: {
          data: ['发送速率', '接收速率'],
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
            name: '发送速率',
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
                  name: '最大值'
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
            name: '接收速率',
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
                  name: '最大值'
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
