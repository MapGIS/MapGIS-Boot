<template>
  <div>
    <a-card :bordered="false" :bodyStyle="{ padding: '16px 0', height: '100%' }" :style="{ height: '100%' }">
      <div class="security-settings-info-main" :class="{ mobile: isMobile }">
        <div class="security-settings-info-left">
          <a-menu :mode="isMobile ? 'horizontal' : 'inline'" v-model="currentKey" :style="{ border: '0' }" type="inner">
            <a-menu-item v-for="item in configItems" :key="item.key">
              {{ item.title }}
            </a-menu-item>
          </a-menu>
        </div>
        <div class="security-settings-info-right">
          <div class="security-settings-info-title">
            <span>{{ currentTitle }}</span>
          </div>
          <keep-alive>
            <template v-for="item in configItems">
              <component :is="item.component" :key="item.key" v-if="currentKey.indexOf(item.key) > -1"></component>
            </template>
          </keep-alive>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
import { baseMixin } from '@/store/app-mixin'
import InitPasswordConfig from './InitPasswordConfig'
import RegisterUserConfig from './RegisterUserConfig'
import PasswordProtectedConfig from './PasswordProtectedConfig'
import UserLoginConfig from './UserLoginConfig.vue'

export default {
  name: 'SecurityConfig',
  components: {
    InitPasswordConfig,
    RegisterUserConfig,
    PasswordProtectedConfig,
    UserLoginConfig
  },
  mixins: [baseMixin],
  data() {
    return {
      configItems: [
        { title: '密码安全配置', key: 'passwordProtected', component: 'PasswordProtectedConfig' },
        { title: '初始密码配置', key: 'initPassword', component: 'InitPasswordConfig' },
        { title: '用户注册配置', key: 'registerUser', component: 'RegisterUserConfig' },
        { title: '用户登录配置', key: 'userLogin', component: 'UserLoginConfig' }
      ],
      currentKey: ['passwordProtected']
    }
  },
  computed: {
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
.security-settings-info-main {
  width: 100%;
  display: flex;
  height: 100%;
  overflow: auto;

  &.mobile {
    display: block;

    .security-settings-info-left {
      border-right: unset;
      border-bottom: 1px solid @border-color;
      width: 100%;
      height: 50px;
      overflow-x: auto;
      overflow-y: scroll;
    }
    .security-settings-info-right {
      padding: 20px 40px;
    }
  }

  .security-settings-info-left {
    border-right: 1px solid @border-color;
    width: 224px;
  }

  .security-settings-info-right {
    flex: 1 1;
    padding: 8px 40px;

    .security-settings-info-title {
      color: rgba(0, 0, 0, 0.85);
      font-size: 20px;
      font-weight: 500;
      line-height: 28px;
      margin-bottom: 12px;
    }
    .security-settings-info-view {
      padding-top: 12px;
    }
  }
}
</style>
