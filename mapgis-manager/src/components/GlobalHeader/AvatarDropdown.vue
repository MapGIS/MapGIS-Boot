<template>
  <a-dropdown v-if="nickname" placement="bottomRight">
    <span class="ant-pro-account-avatar">
      <a-avatar size="small" :src="avatar" class="antd-pro-global-header-index-avatar" />
      <span>{{ nickname }}</span>
    </span>
    <template v-slot:overlay>
      <a-menu class="ant-pro-drop-down menu" :selected-keys="[]">
        <a-menu-item v-if="menu" key="center" @click="handleToCenter">
          <a-icon type="user" />
          {{ $t('user.center') }}
        </a-menu-item>
        <a-menu-divider v-if="menu" />
        <a-menu-item key="logout" @click="handleLogout">
          <a-icon type="logout" />
          {{ $t('logout') }}
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
  <span v-else @click="login">
    <span>{{ $t('login') }}</span>
  </span>
</template>

<script>
import { Modal } from 'ant-design-vue'
import { mapGetters } from 'vuex'

export default {
  name: 'AvatarDropdown',
  props: {
    menu: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters(['avatar', 'nickname'])
  },
  methods: {
    handleToCenter() {
      this.$router.push({ path: '/account/center' })
    },
    handleLogout(e) {
      Modal.confirm({
        title: this.$t('tip'),
        content: this.$t('confirm.logout'),
        onOk: () => {
          return this.$store.dispatch('logout').then(() => {
            if (!this.$store.getters.casInfo.enabled) {
              location.href = '/'
            }
          })
        },
        onCancel() {}
      })
    },
    login() {
      this.$router.push('/user/login')
    }
  }
}
</script>

<style lang="less" scoped>
.ant-pro-drop-down {
  /deep/ .action {
    margin-right: 8px;
  }
  /deep/ .ant-dropdown-menu-item {
    min-width: 160px;
  }
}
</style>
