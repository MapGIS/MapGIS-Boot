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
          <span> 项目已不间断运行：{{ data.jvm.runTime }} </span>
          <a-icon type="sync" class="icon-refresh" @click="init" />
        </div>
      </a-card>

      <a-row :gutter="[10, 10]">
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
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
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
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
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
          <a-card title="交换区信息" :bordered="false" class="monitor-server-center-row-col-card">
            <a-tooltip>
              <template #title>
                <div>交换区总量：{{ data.swap.total }}</div>
                <div>交换区已用：{{ data.swap.used }}</div>
                <div>交换区剩余：{{ data.swap.free }}</div>
                <div>交换区使用率：{{ data.swap.usage }}%</div>
              </template>
              <a-progress
                type="dashboard"
                :stroke-color="getProgressColor(data.swap.usage)"
                :percent="data.swap.usage"
              />
            </a-tooltip>
            <div>交换区使用率</div>
          </a-card>
        </a-col>
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
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
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
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
        <a-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4">
          <a-card title="网络信息" :bordered="false" class="monitor-server-center-row-col-card">
            <a-statistic
              title="上行速率"
              :value="data.network.uplink"
              :precision="2"
              :value-style="{ color: '#3f8600', fontSize: '22px' }"
            >
              <template #prefix>
                <a-icon type="arrow-up" />
              </template>
            </a-statistic>
            <a-statistic
              title="下行速率"
              :value="data.network.downlink"
              :precision="2"
              :value-style="{ color: '#cf1322', fontSize: '22px' }"
              style="margin-top: 24px"
            >
              <template #prefix>
                <a-icon type="arrow-down" />
              </template>
            </a-statistic>
          </a-card>
        </a-col>
      </a-row>

      <div>
        <a-row :gutter="6">
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card title="CPU使用率监控" :bordered="false">
              <div>
                <v-chart :options="cpuInfo" />
              </div>
            </a-card>
          </a-col>
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
            <a-card title="内存使用率监控" :bordered="false">
              <div>
                <v-chart :options="memoryInfo" />
              </div>
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
          <a-descriptions-item label="项目路径" :span="2">{{ data.sys.userDir }}</a-descriptions-item>
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
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/polar'
import 'echarts/lib/component/tooltip'
import { getServer } from '@/api/monitor/server'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Server',
  mixins: [tableMixin],
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      show: false,
      monitor: null,
      data: {},
      cpuInfo: {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          interval: 20
        },
        series: [
          {
            data: [],
            type: 'line',
            areaStyle: {
              normal: {
                color: 'rgb(32, 160, 255)' // 改变区域颜色
              }
            },
            itemStyle: {
              normal: {
                color: '#6fbae1',
                lineStyle: {
                  color: '#6fbae1' // 改变折线颜色
                }
              }
            }
          }
        ]
      },
      memoryInfo: {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          interval: 20
        },
        series: [
          {
            data: [],
            type: 'line',
            areaStyle: {
              normal: {
                color: 'rgb(32, 160, 255)' // 改变区域颜色
              }
            },
            itemStyle: {
              normal: {
                color: '#6fbae1',
                lineStyle: {
                  color: '#6fbae1' // 改变折线颜色
                }
              }
            }
          }
        ]
      },
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
          title: '盘符类型',
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
      ]
    }
  },
  created() {
    this.init()
    this.monitor = window.setInterval(() => {
      setTimeout(() => {
        this.init()
      }, 2)
    }, 3500)
  },
  destroyed() {
    clearInterval(this.monitor)
  },
  methods: {
    init() {
      getServer().then(response => {
        this.data = response.data
        this.show = true
        if (this.cpuInfo.xAxis.data.length >= 8) {
          this.cpuInfo.xAxis.data.shift()
          this.memoryInfo.xAxis.data.shift()
          this.cpuInfo.series[0].data.shift()
          this.memoryInfo.series[0].data.shift()
        }
        this.cpuInfo.xAxis.data.push(this.data.time)
        this.memoryInfo.xAxis.data.push(this.data.time)
        this.cpuInfo.series[0].data.push(parseFloat(this.data.cpu.total))
        this.memoryInfo.series[0].data.push(parseFloat(this.data.memory.usage))
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
    }
  }
}
</script>

<style lang="less" scoped>
.server-base-info {
  color: #666;
  font-size: 13px;
  line-height: 1.15;
  span {
    margin-right: 28px;
  }
  .icon-refresh {
    margin-right: 10px;
    float: right;
    cursor: pointer;
    margin-left: 40px;
  }
}
.monitor-server-center-row-col-card {
  text-align: center;
}
</style>
