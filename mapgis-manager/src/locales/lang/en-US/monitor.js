import online from './monitor/online'
import server from './monitor/server'
import performance from './monitor/performance'
import cache from './monitor/cache'

export default { ...online, ...server, ...performance, ...cache }
