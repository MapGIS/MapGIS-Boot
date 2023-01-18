<template>
  <page-header-wrapper>
    <a-card :bordered="false" :bodyStyle="{ height: '100%' }" :style="{ height: '100%' }">
      <div class="ldap-settings-info-main" :class="{ mobile: isMobile }">
        <div class="ldap-settings-info-left">
          <a-menu :mode="isMobile ? 'horizontal' : 'inline'" v-model="currentKey" :style="{ border: '0' }" type="inner">
            <a-menu-item v-for="item in configItems" :key="item.key">
              {{ item.title }}
            </a-menu-item>
          </a-menu>
        </div>
        <div class="ldap-settings-info-right">
          <template v-for="item in configItems">
            <component :is="item.component" :key="item.key" v-if="currentKey.indexOf(item.key) > -1"></component>
          </template>
        </div>
      </div>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { baseMixin } from '@/store/app-mixin'
import LdapBaseConfig from './LdapBaseConfig'
import LdapRoleMapping from './LdapRoleMapping'

export default {
  name: 'LdapConfig',
  components: {
    LdapBaseConfig,
    LdapRoleMapping
  },
  mixins: [baseMixin],
  data() {
    return {
      currentKey: ['baseConfig']
    }
  },
  computed: {
    configItems() {
      return [
        { title: 'LDAP登录配置', key: 'baseConfig', component: 'LdapBaseConfig' },
        { title: '角色映射配置', key: 'roleMapping', component: 'LdapRoleMapping' }
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
.ldap-settings-info-main {
  width: 100%;
  display: flex;
  height: 100%;
  overflow: auto;

  &.mobile {
    display: block;

    .ldap-settings-info-left {
      border-right: unset;
      border-bottom: 1px solid @border-color;
      width: 100%;
      height: 50px;
    }
    .security-settings-info-right {
      padding: 20px 40px;
    }
  }

  .ldap-settings-info-left {
    border-right: 1px solid @border-color;
    width: 224px;
  }

  .ldap-settings-info-right {
    flex: 1 1;
    padding: 8px 40px;

    .ldap-settings-info-view {
      padding-top: 12px;
    }
  }
}
</style>
