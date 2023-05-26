<template>
  <page-header-wrapper>
    <a-card class="server-performance-monitor" :bordered="false">
      <a-row :gutter="[20, 0]">
        <a-col :span="6">
          <div class="text-card" style="background: #e2dcff">
            <div class="text-header">
              {{ todayRequestCount }}
            </div>
            <div class="text-body">{{ $t('monitor.performance.today.total.visits') }}</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="text-card" style="background: #e0f7eb">
            <div class="text-header">
              {{ totalRequestCount }}
            </div>
            <div class="text-body">{{ $t('monitor.performance.cumulative.total.visits') }}</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="text-card" style="background: #ecf9d9">
            <div class="text-header">
              {{ realtimeConcurrency }}
            </div>
            <div class="text-body">{{ $t('monitor.performance.realtime.concurrency') }}</div>
          </div>
        </a-col>
        <a-col :span="6">
          <div class="text-card" style="background: #faf2dc">
            <div class="text-header">{{ avgResponseTime }}<span style="font-size: 16px">ms</span></div>
            <div class="text-body">{{ $t('monitor.performance.average.response.time') }}</div>
          </div>
        </a-col>
      </a-row>
      <a-row style="margin-top: 40px; margin-bottom: 30px" :gutter="16">
        <a-col :span="12">
          <a-card>
            <a-row>
              <div id="avgResponseTimeMoniter" style="width: 100%; max-height: 800px; height: 350px" />
            </a-row>
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card>
            <a-row>
              <div id="concurrencyMoniter" style="width: 100%; max-height: 800px; height: 350px" />
            </a-row>
          </a-card>
        </a-col>
      </a-row>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import * as echarts from 'echarts'
import { getServerPerformance } from '@/api/system/monitor'
import { mapState } from 'vuex'

export default {
  name: 'ServerPerformance',
  data() {
    return {
      totalRequestCount: 0,
      todayRequestCount: 0,
      realtimeConcurrency: 0,
      avgResponseTime: '-',
      avgResponseTimeRecords: [],
      concurrencyRequestRecords: [],
      avgResponseTimeChart: '',
      concurrencyRequestChart: '',
      timer: '',
      color: '#1f1f1f'
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
      this.init()
    }
  },
  destroyed() {
    this.closeTimer()
  },
  mounted() {
    if (this.theme === 'dark') {
      this.color = '#1f1f1f'
    } else if (this.theme === 'light') {
      this.color = '#1f1f1f'
    } else if (this.theme === 'night') {
      this.color = '#ffffff'
    }
    this.fetchData()
    this.init()
  },
  methods: {
    fetchData() {
      const self = this
      getServerPerformance().then(res => {
        const data = res.data
        self.updateByData(data, true)
      })
    },
    formatPercent(x, y) {
      if (y > 0) {
        return ((x / y) * 100).toFixed(0) + '%'
      }
      return 100 + '%'
    },
    responseTimeFormat(n) {
      if (!n) {
        return ''
      }
      if (n.toString().substring(n.toString().lastIndexOf('.')).length > 3) {
        return n.toFixed(3)
      }
      return n
    },
    timeFormat(time) {
      return this.dateFormat(new Date(time), 'yyyy-MM-dd hh:mm:ss')
    },
    dateFormat(date, fmt) {
      const o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'h+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        S: date.getMilliseconds() // 毫秒
      }
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, date.getFullYear() + '')
      }
      for (const k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
          fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length))
        }
      }
      return fmt
    },
    updateByData(data, isFirst) {
      this.todayRequestCount = data.todayRequestCount
      this.totalRequestCount = data.totalRequestCount
      const timeSerialData = data.requestSerials
      if (timeSerialData && timeSerialData.length > 0) {
        const last = timeSerialData[timeSerialData.length - 1]
        this.realtimeConcurrency = last.concurrency
        this.avgResponseTime = last.responseTime
      }

      if (isFirst) {
        const records = data.requestSerials
        this.avgResponseTimeRecords = records.map(t => {
          const time = this.timeFormat(t.time)
          return {
            name: time,
            value: [time, t.responseTime]
          }
        })
        this.avgResponseTimeChart &&
          this.avgResponseTimeChart.setOption({
            series: [
              {
                data: this.avgResponseTimeRecords
              }
            ]
          })

        this.concurrencyRequestRecords = records.map(t => {
          const time = this.timeFormat(t.time)
          return {
            name: time,
            value: [time, t.concurrency]
          }
        })
        this.concurrencyRequestChart &&
          this.concurrencyRequestChart.setOption({
            series: [
              {
                data: this.concurrencyRequestRecords
              }
            ]
          })
      }
    },
    init() {
      const ec = echarts
      this.avgResponseTimeChart = ec.init(document.getElementById('avgResponseTimeMoniter'))
      this.avgResponseTimeChart && this.initEchartsAvgResponseTime(this.avgResponseTimeChart)
      this.concurrencyRequestChart = ec.init(document.getElementById('concurrencyMoniter'))
      this.concurrencyRequestChart && this.initEchartsConcurrencyRequests(this.concurrencyRequestChart)
      this.startTimer()
    },
    closeTimer() {
      if (this.timer) {
        clearInterval(this.timer)
      }
    },
    startTimer() {
      this.closeTimer()
      this.timer = setInterval(() => {
        this.fetchData()
      }, 2000)
    },
    // 平均响应折线图
    initEchartsAvgResponseTime(chartObject) {
      const self = this
      self.avgResponseTimeRecords = []
      const lineColor = '#4CAF50'
      const option = {
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        title: {
          text: this.$t('monitor.performance.average.response.time') + '(ms)',
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
              const totalValue = totalTips.value ? totalTips.value[1] : ''
              return self.$t('monitor.performance.average.response.time.time', {
                time: totalTips.name.toLocaleString(),
                response: totalValue
              })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        legend: {
          data: [this.$t('monitor.performance.current.node')]
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
            // max: '100',
            axisLabel: {
              formatter: function (value, index) {
                return value
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
            name: this.$t('monitor.performance.average.response.time'),
            type: 'line',
            smooth: true,
            showSymbol: false,
            smoothMonotone: 'x',
            ampling: 'average',
            data: self.avgResponseTimeRecords,
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
    // 实时并发请求数折线图
    initEchartsConcurrencyRequests(chartObject) {
      const self = this
      self.concurrencyRequestRecords = []
      const lineColor = '#03a9f4'
      const option = {
        grid: {
          left: '2%',
          right: '3%',
          bottom: '1%',
          containLabel: true
        },
        title: {
          text: this.$t('monitor.performance.realtime.concurrency'),
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
              const totalValue = totalTips.value ? totalTips.value[1] : ''
              return self.$t('monitor.performance.realtime.concurrency.time', {
                time: totalTips.name.toLocaleString(),
                request: totalValue
              })
            }
          },
          axisPointer: {
            animation: false
          },
          confine: true
        },
        legend: {
          data: [this.$t('monitor.performance.current.node')]
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
            minInterval: 1,
            min: 0,
            axisLabel: {
              formatter: function (value, index) {
                return value
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
            name: this.$t('monitor.performance.realtime.concurrency'),
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
            data: self.concurrencyRequestRecords,
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
    }
  }
}
</script>

<style lang="less" scoped>
.server-performance-monitor {
  .text-card {
    height: 90px;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .text-header {
      text-align: center;
      font-weight: 700;
      font-size: 36px;
      height: 41px;
      line-height: 41px;
      color: @primary-color;
    }
    .text-body {
      font-size: 16px;
      text-align: center;
      color: @primary-color;
    }
  }
}
</style>
