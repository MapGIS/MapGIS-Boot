import request from '@/utils/request'

// 查询服务器性能信息
export function getServerPerformance() {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/monitor/serverPerformance`,
    method: 'get'
  })
}
