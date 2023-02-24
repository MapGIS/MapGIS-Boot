<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view v-if="initialized" />
    </div>
  </a-config-provider>
</template>

<script>
import { setDocumentTitle, setFavicon } from '@/utils/domUtil'
import watermark from 'watermark-dom'
import { i18nRender } from '@/locales'
import { serverMixin } from '@/store/server-mixin'
import { mapGetters } from 'vuex'

export default {
  mixins: [serverMixin],
  data() {
    return {
      initialized: false
    }
  },
  computed: {
    ...mapGetters(['domTitle']),
    locale() {
      // 只是为了切换语言时，更新标题
      const { title } = this.$route.meta
      title && setDocumentTitle(`${i18nRender(title)} - ${this.domTitle}`)

      return this.$i18n.getLocaleMessage(this.$store.getters.lang).antLocale
    }
  },
  async mounted() {
    await this.$store.dispatch('getSystemConfig')
    await this.$store.dispatch('getBaseConfig')
    this.initialized = true
    if (this.baseConfig.header.defaultLogoAndTitle) {
      setFavicon(require('@/assets/images/logo.svg'))
    } else {
      setFavicon(this.baseConfig.header.logo)
    }
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
  },
  destroyed() {
    watermark.remove()
  }
}
</script>
