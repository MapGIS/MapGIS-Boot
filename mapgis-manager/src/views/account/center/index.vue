<template>
  <div class="page-header-index-wide">
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="account-settings-info-main" :class="{ mobile: isMobile }">
        <div class="account-settings-info-left">
          <a-menu
            :mode="isMobile ? 'horizontal' : 'inline'"
            :default-selected-keys="['base']"
            :style="{ border: '0', width: isMobile ? '560px' : 'auto' }"
            type="inner"
            @openChange="onOpenChange"
          >
            <a-menu-item key="base">
              <a @click="baseClick">个人信息</a>
            </a-menu-item>
            <a-menu-item key="security">
              <a @click="securityClick">密码设置</a>
            </a-menu-item>
            <a-menu-item key="binding">
              <a @click="bindingClick">账号绑定</a>
            </a-menu-item>
          </a-menu>
        </div>
        <div class="account-settings-info-right">
          <div class="account-settings-info-title">
            <span>{{ title }}</span>
          </div>
          <base-setting ref="baseSetting" v-if="base"></base-setting>
          <security ref="security" v-if="security"></security>
          <binding ref="binding" v-if="binding"></binding>
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
      // horizontal  inline
      mode: 'inline',
      openKeys: [],
      title: '个人信息',
      base: true,
      security: false,
      binding: false
    }
  },
  mounted() {},
  methods: {
    onOpenChange(openKeys) {
      this.openKeys = openKeys
    },
    baseClick() {
      this.base = true
      this.security = false
      this.binding = false
      this.title = '个人信息'
    },
    securityClick() {
      this.base = false
      this.security = true
      this.binding = false
      this.title = '密码设置'
    },
    bindingClick() {
      this.base = false
      this.security = false
      this.binding = true
      this.title = '账号绑定'
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
      border-bottom: 1px solid #e8e8e8;
      width: 100%;
      height: 50px;
      overflow-x: auto;
      overflow-y: scroll;
    }
    .account-settings-info-right {
      padding: 20px 40px;
    }
  }

  .account-settings-info-left {
    border-right: 1px solid #e8e8e8;
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
