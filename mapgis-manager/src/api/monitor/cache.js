import request from '@/utils/request'

// 查询缓存详细
export function getCache() {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/monitor/cache`,
    method: 'get'
  })
}
