import request from '@/utils/request'

// 查询参数列表
export function listConfig(query) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/list`,
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getConfig(configId) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/` + configId,
    method: 'get'
  })
}

// 根据参数键名查询参数值
export function getConfigValueByKey(configKey) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/configKey/` + configKey,
    method: 'get'
  })
}

// 根据参数键名查询参数详细
export function getConfigByKey(configKey) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/configKey/` + configKey + '/info',
    method: 'get'
  })
}

// 新增参数配置
export function addConfig(data) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config`,
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateConfig(data) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config`,
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delConfig(configId) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/` + configId,
    method: 'delete'
  })
}

// 刷新参数缓存
export function refreshCache() {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/config/refreshCache`,
    method: 'delete'
  })
}

// 根据基本配置信息
export function getBaseConfig() {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/webConfig/base`,
    method: 'get'
  })
}
