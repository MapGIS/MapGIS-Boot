import request from '@/utils/request'

// 查询系统日志列表
export function list(query) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/list`,
    method: 'get',
    params: query
  })
}

// 获取日志配置
export function getConfig() {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/config`,
    method: 'get'
  })
}

// 修改日志配置
export function updateConfig(data) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/systemlog/config`,
    method: 'put',
    data: data
  })
}
