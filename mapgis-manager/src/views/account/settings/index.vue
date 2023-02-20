<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="account-settings-info-main" :class="{ mobile: isMobile }">
        <div class="account-settings-info-left">
          <a-menu :mode="isMobile ? 'horizontal' : 'inline'" v-model="currentKey" :style="{ border: '0' }" type="inner">
            <a-menu-item v-for="item in configItems" :key="item.key">
              {{ item.title }}
            </a-menu-item>
          </a-menu>
        </div>
        <div class="account-settings-info-right">
          <div class="account-settings-info-title">
            <span>{{ currentTitle }}</span>
          </div>
          <template v-for="item in configItems">
            <component :is="item.component" :key="item.key" v-if="currentKey.indexOf(item.key) > -1"></component>
          </template>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
import { baseMixin } from '@/store/app-mixin'
import Security from './Security'
import BaseSetting from './BaseSetting'
import Binding from './Binding'

export default {
  name: 'Settings',
  components: {
    Security,
    BaseSetting,
    Binding
  },
  mixins: [baseMixin],
  data() {
    return {
      currentKey: ['base']
    }
  },
  computed: {
    configItems() {
      return [
        { title: this.$t('account.center.user.info'), key: 'base', component: 'BaseSetting' },
        { title: this.$t('account.center.password.setting'), key: 'security', component: 'Security' },
        { title: this.$t('account.center.bind'), key: 'binding', component: 'Binding' }
      ]
    },
    currentTitle() {
      const currnetConfig = this.configItems.find(config => {
        return this.currentKey.indexOf(config.key) > -1
      })
      return currnetConfig.title
    }
  }
}
</script>

<style lang="less" scoped>
.account-settings-info-main {
  width: 100%;
  display: flex;
  height: 100%;
  overflow: auto;

  &.mobile {
    display: block;

    .account-settings-info-left {
      border-right: unset;
      border-bottom: 1px solid @border-color;
      width: 100%;
      height: 50px;
    }
    .account-settings-info-right {
      padding: 20px 40px;
    }
  }

  .account-settings-info-left {
    border-right: 1px solid @border-color;
    width: 224px;
  }

  .account-settings-info-right {
    flex: 1 1;
    padding: 8px 40px;

    .account-settings-info-title {
      color: rgba(0, 0, 0, 0.85);
      font-size: 20px;
      font-weight: 500;
      line-height: 28px;
      margin-bottom: 12px;
    }
    .account-settings-info-view {
      padding-top: 12px;
    }
  }
}
</style>
