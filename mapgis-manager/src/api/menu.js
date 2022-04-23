import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/xxx/rest/manager/system/menu/getRouters',
    method: 'get'
  })
}
