import { mapState } from 'vuex'

const serverMixin = {
  computed: {
    ...mapState({
      baseConfig: state => state.server.baseConfig,
      systemConfig: state => state.server.systemConfig
    }),
    isWindowsOS() {
      return this.systemConfig && this.systemConfig.osName?.toLowerCase().startsWith('windows')
    },
    isJava() {
      return this.systemConfig && this.systemConfig.devPlatform === 'Java'
    },
    version() {
      return this.systemConfig && this.systemConfig.version
    },
    fullVersion() {
      return this.systemConfig && this.systemConfig.fullVersion
    },
    devPlatform() {
      return this.systemConfig && this.systemConfig.devPlatform
    },
    osArch() {
      return this.systemConfig && this.systemConfig.osArch
    },
    isX86Arch() {
      return this.osArch && this.osArch.indexOf('amd64') > -1
    },
    osName() {
      return this.systemConfig && this.systemConfig.osName
    },
    swaggerEnabled() {
      return !!this.systemConfig?.swaggerEnabled
    },
    logo() {
      return this.baseConfig && this.baseConfig.header.logo
    },
    title() {
      return this.baseConfig && this.baseConfig.header.title
    },
    copyright() {
      return this.baseConfig && this.baseConfig.footer.copyright
    },
    fullCopyright() {
      return `${this.copyright} ${this.fullVersion}`
    },
    defaultLogoAndTitle() {
      return this.baseConfig && this.baseConfig.header.defaultLogoAndTitle
    }
  }
}

export { serverMixin }
