import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: `${window._CONFIG['apiPathManagerPrefix']}/system/menu/getRouters`,
    method: 'get'
  })
}
