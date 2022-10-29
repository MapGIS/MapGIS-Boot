<template>
  <div v-if="configLoaded && casInfo && casInfo.enabled && casInfo.isReserveDefaultLogin" class="user-login-other">
    <a :href="casInfo.casLoginUrl">CAS登录</a>
  </div>
</template>

<script>
import { getSystemConfig } from '@/api/system/config'

export default {
  name: 'CasLogin',
  data() {
    return {
      configLoaded: false,
      casInfo: null
    }
  },
  async mounted() {
    const systemInfoResult = await getSystemConfig()

    this.casInfo = systemInfoResult.data.casConfig
    this.configLoaded = true
  }
}
</script>

<style lang="less" scoped>
.user-login-other {
  a {
    color: rgba(255, 255, 255, 0.85);
  }
}
</style>
