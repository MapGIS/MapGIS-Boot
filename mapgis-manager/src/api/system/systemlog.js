import request from '@/utils/request'

// 查询系统日志列表
export function list(query) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/list`,
    method: 'get',
    params: query
  })
}
