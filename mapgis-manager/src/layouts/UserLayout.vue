<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <div class="container" :style="{ backgroundImage: backgourndImageUrl }">
      <div class="top">
        <div class="header">
          <a href="/">
            <img v-if="logo" :src="logo" class="logo" alt="logo" />
            <span class="title">{{ title }}</span>
          </a>
        </div>
        <div class="desc"></div>
      </div>
      <router-view />
      <div class="footer">
        <div class="copyright">{{ copyright }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'
import { getBaseConfig, getSystemConfig } from '@/api/system/config'

export default {
  name: 'UserLayout',
  mixins: [deviceMixin],
  data() {
    return {
      logo: '',
      title: '',
      copyright: ''
    }
  },
  computed: {
    backgourndImageUrl() {
      return `url('${process.env.BASE_URL}login-bg.png')`
    }
  },
  mounted() {
    document.body.classList.add('userLayout')

    getBaseConfig().then(response => {
      const configValue = response.data
      if (configValue) {
        const {
          header: { logo, title },
          footer: { copyright }
        } = JSON.parse(configValue)
        this.logo = logo
        this.title = title

        getSystemConfig().then(res => {
          this.copyright = `${copyright} ${res.data.fullVersion}`
        })
      }
    })
  },
  beforeDestroy() {
    document.body.classList.remove('userLayout')
  }
}
</script>

<style lang="less" scoped>
#userLayout.user-layout-wrapper {
  height: 100%;

  &.mobile {
    .container {
      .main {
        max-width: 368px;
        width: 98%;
      }
    }
  }

  .container {
    width: 100%;
    min-height: 100%;
    background-repeat: no-repeat;
    background-position-x: center;
    background-size: cover;
    padding: 110px 0 144px;
    position: relative;

    a {
      text-decoration: none;
    }

    .top {
      text-align: center;

      .header {
        height: 44px;
        line-height: 44px;

        .badge {
          position: absolute;
          display: inline-block;
          line-height: 1;
          vertical-align: middle;
          margin-left: -12px;
          margin-top: -10px;
          opacity: 0.8;
        }

        .logo {
          height: 44px;
          vertical-align: top;
          margin-right: 16px;
          border-style: none;
        }

        .title {
          font-size: 33px;
          color: rgba(255, 255, 255, 0.85);
          font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
          font-weight: 600;
          position: relative;
          top: 2px;
        }
      }
      .desc {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.45);
        margin-top: 12px;
        margin-bottom: 40px;
      }
    }

    .main {
      min-width: 260px;
      width: 368px;
      margin: 0 auto;
    }

    .footer {
      position: absolute;
      width: 100%;
      bottom: 0;
      padding: 0 16px;
      margin: 48px 0 24px;
      text-align: center;

      .links {
        margin-bottom: 8px;
        font-size: 14px;
        a {
          color: rgba(255, 255, 255, 0.45);
          transition: all 0.3s;
          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }
      .copyright {
        color: rgba(255, 255, 255, 0.45);
        font-size: 14px;
      }
    }
  }
}
</style>
