import accesslog from './log/accesslog'
import logininfor from './log/logininfor'
import operlog from './log/operlog'
import systemlog from './log/systemlog'
import logconfig from './log/logconfig'

export default { ...accesslog, ...logininfor, ...operlog, ...systemlog, ...logconfig }
