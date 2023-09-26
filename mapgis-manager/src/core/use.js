import Vue from 'vue'

// base library
import Antd from 'ant-design-vue'
import VueCropper from 'vue-cropper'
import 'ant-design-vue/dist/antd.less'

// ext library
import VueClipboard from 'vue-clipboard2'
import MultiTab from '@/components/MultiTab'
import PageLoading from '@/components/PageLoading'
import PermissionHelper from '@/core/permission/permission'
// import '@/components/use'
import './directives/action'
import HevueImgPreview from 'hevue-img-preview'

VueClipboard.config.autoSetContainer = true

Vue.use(Antd)
Vue.use(MultiTab)
Vue.use(PageLoading)
Vue.use(VueClipboard)
Vue.use(PermissionHelper)
Vue.use(VueCropper)
Vue.use(HevueImgPreview)

process.env.NODE_ENV !== 'production' && console.warn('[antd-pro] WARNING: Antd now use fulled imported.')
