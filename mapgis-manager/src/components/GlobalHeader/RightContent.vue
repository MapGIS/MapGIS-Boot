<template>
  <div :class="wrpCls">
    <a-space size="0">
      <screenfull :class="prefixCls" />
      <notice-icon v-hasPermi="['system:notice:list']" :class="prefixCls" />
      <select-lang :class="prefixCls" style="vertical-align: middle" />
      <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
    </a-space>
  </div>
</template>

<script>
import AvatarDropdown from './AvatarDropdown'
import NoticeIcon from '@/components/NoticeIcon'
import Screenfull from '@/components/Screenfull'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'RightContent',
  components: {
    AvatarDropdown,
    NoticeIcon,
    Screenfull,
    SelectLang
  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action'
    },
    isMobile: {
      type: Boolean,
      default: () => false
    },
    topMenu: {
      type: Boolean,
      required: true
    },
    theme: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      showMenu: true,
      currentUser: {}
    }
  },
  computed: {
    wrpCls() {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${this.isMobile || !this.topMenu ? 'light' : this.theme}`]: true
      }
    }
  },
  mounted() {
    setTimeout(() => {
      this.currentUser = {
        name: 'MapGIS'
      }
    }, 1500)
  },
  methods: {}
}
</script>
