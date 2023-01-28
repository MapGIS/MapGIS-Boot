import config from './security/config'
import user from './security/user'
import role from './security/role'
import usergroup from './security/usergroup'
import menu from './security/menu'
import dept from './security/dept'
import post from './security/post'
import oauth from './security/oauth'

export default { ...config, ...user, ...role, ...usergroup, ...menu, ...dept, ...post, ...oauth }
