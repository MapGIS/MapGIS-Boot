import request from '@/utils/request'

// 查询Http访问日志列表
export function listAssesslog(query) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog/list`,
    method: 'get',
    params: query
  })
}

// 查询Http访问日志详细
export function getAssesslog(accessId) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog/` + accessId,
    method: 'get'
  })
}

// 新增Http访问日志
export function addAssesslog(data) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog`,
    method: 'post',
    data: data
  })
}

// 修改Http访问日志
export function updateAssesslog(data) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog`,
    method: 'put',
    data: data
  })
}

// 删除Http访问日志
export function delAssesslog(accessId) {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/assesslog/` + accessId,
    method: 'delete'
  })
}
