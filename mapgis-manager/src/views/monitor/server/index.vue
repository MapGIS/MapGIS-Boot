<template>
  <page-header-wrapper>
    <a-spin tip="数据加载中..." :spinning="!show">
      <div v-if="!show" style="height: 500px; width: 100%" />
    </a-spin>
    <a-space v-if="show" direction="vertical" style="width: 100%">
      <a-card :bordered="false">
        <div class="server-base-info" v-if="data.sys">
          <a-icon type="setting" style="margin-right: 5px" />
          <span> 系统：{{ data.sys.os }} </span>
          <span> IP：{{ data.sys.ip }} </span>
          <span> 项目已不间断运行：{{ data.sys.day }} </span>
          <a-icon type="sync" class="icon-refresh" @click="init" />
        </div>
      </a-card>
      <a-card title="状态" :bordered="false">
        <a-row :gutter="24">
          <a-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">CPU使用率</div>
            <a-tooltip placement="top">
              <div slot="title" style="font-size: 12px">
                <div style="padding: 3px">
                  {{ data.cpu.name }}
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.package }}
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.core }}
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.logic }}
                </div>
              </div>
              <div class="content">
                <a-progress type="dashboard" :percent="parseFloat(data.cpu.used)" />
              </div>
            </a-tooltip>
            <div class="footer">{{ data.cpu.coreNumber }} 核心</div>
          </a-col>
          <a-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">内存使用率</div>
            <a-tooltip placement="top">
              <div slot="title" style="font-size: 12px">
                <div style="padding: 3px">总量：{{ data.memory.total }}</div>
                <div style="padding: 3px">已使用：{{ data.memory.used }}</div>
                <div style="padding: 3px">空闲：{{ data.memory.available }}</div>
              </div>
              <div class="content">
                <a-progress type="dashboard" :percent="parseFloat(data.memory.usageRate)" />
              </div>
            </a-tooltip>
            <div class="footer">{{ data.memory.used }} / {{ data.memory.total }}</div>
          </a-col>
          <a-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">交换区使用率</div>
            <a-tooltip placement="top">
              <div slot="title" style="font-size: 12px">
                <div style="padding: 3px">总量：{{ data.swap.total }}</div>
                <div style="padding: 3px">已使用：{{ data.swap.used }}</div>
                <div style="padding: 3px">空闲：{{ data.swap.available }}</div>
              </div>
              <div class="content">
                <a-progress type="dashboard" :percent="parseFloat(data.swap.usageRate)" />
              </div>
            </a-tooltip>
            <div class="footer">{{ data.swap.used }} / {{ data.swap.total }}</div>
          </a-col>
          <a-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">磁盘使用率</div>
            <div class="content">
              <a-tooltip placement="top">
                <div slot="title" style="font-size: 12px">
                  <div style="padding: 3px">总量：{{ data.disk.total }}</div>
                  <div style="padding: 3px">空闲：{{ data.disk.available }}</div>
                </div>
                <div class="content">
                  <a-progress type="dashboard" :percent="parseFloat(data.disk.usageRate)" />
                </div>
              </a-tooltip>
            </div>
            <div class="footer">{{ data.disk.used }} / {{ data.disk.total }}</div>
          </a-col>
        </a-row>
      </a-card>

      <div>
        <a-row :gutter="6">
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <a-card title="CPU使用率监控" :bordered="false">
              <div>
                <v-chart :options="cpuInfo" />
              </div>
            </a-card>
          </a-col>
          <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <a-card title="内存使用率监控" :bordered="false">
              <div>
                <v-chart :options="memoryInfo" />
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>
    </a-space>
  </page-header-wrapper>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/polar'
import { getServer } from '@/api/monitor/server'

export default {
  name: 'Server',
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
      }
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
        this.cpuInfo.series[0].data.push(parseFloat(this.data.cpu.used))
        this.memoryInfo.series[0].data.push(parseFloat(this.data.memory.usageRate))
      })
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
.cpu,
.memory,
.swap,
.disk {
  width: 20%;
  float: left;
  padding-bottom: 20px;
  margin-right: 5%;
}
.title {
  text-align: center;
  font-size: 15px;
  font-weight: 500;
  color: #999;
  margin-bottom: 16px;
}
.footer {
  text-align: center;
  font-size: 15px;
  font-weight: 500;
  color: #999;
  margin-top: -5px;
  margin-bottom: 10px;
}
.content {
  text-align: center;
  margin-top: 5px;
  margin-bottom: 5px;
}
</style>
