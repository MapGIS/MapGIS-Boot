import request from '@/utils/request'

// 查询缓存详细
export function getCache() {
  return request({
    url: '/xxx/rest/manager/monitor/cache',
    method: 'get'
  })
}
