import config from './security/config'
import user from './security/user'
import role from './security/role'
import usergroup from './security/usergroup'

export default { ...config, ...user, ...role, ...usergroup }
