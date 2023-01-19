import config from './security/config'
import user from './security/user'
import role from './security/role'

export default { ...config, ...user, ...role }
