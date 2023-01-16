import online from './monitor/online'
import server from './monitor/server'
import performance from './monitor/performance'

export default { ...online, ...server, ...performance }
