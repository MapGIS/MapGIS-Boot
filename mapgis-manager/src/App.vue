<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view />
    </div>
  </a-config-provider>
</template>

<script>
import { setDocumentTitle, setFavicon } from '@/utils/domUtil'
import Watermark from '@/utils/wartermark'
import { i18nRender } from '@/locales'
import { getBaseConfig } from '@/api/system/config'

export default {
  data() {
    return {}
  },
  mounted() {
    Watermark.set('MapGIS Boot')
    getBaseConfig().then(response => {
      const configValue = response.data
      if (configValue) {
        const {
          header: { logo }
        } = JSON.parse(configValue)
        setFavicon(logo)
      }
    })
  },
  destroyed() {
    Watermark.remove()
  },
  computed: {
    locale() {
      // 只是为了切换语言时，更新标题
      const { title } = this.$route.meta
      title && setDocumentTitle(`${i18nRender(title)}`)

      return this.$i18n.getLocaleMessage(this.$store.getters.lang).antLocale
    }
  }
}
</script>
