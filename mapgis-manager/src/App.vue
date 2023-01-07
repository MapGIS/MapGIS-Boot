<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view />
    </div>
  </a-config-provider>
</template>

<script>
import { setDocumentTitle, setFavicon } from '@/utils/domUtil'
import watermark from 'watermark-dom'
import { i18nRender } from '@/locales'
import { getBaseConfig } from '@/api/system/config'

export default {
  data() {
    return {}
  },
  mounted() {
    watermark.load({
      watermark_txt: 'MapGIS Boot',
      watermark_width: 300,
      watermark_height: 240,
      watermark_angle: 30,
      watermark_color: 'black',
      watermark_alpha: 0.15,
      watermark_fontsize: '15px',
      watermark_x: 0,
      watermark_y: 0,
      monitor: false
    })
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
    watermark.remove()
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
