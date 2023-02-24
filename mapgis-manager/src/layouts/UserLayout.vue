<template>
  <div id="userLayout" :class="['user-layout-wrapper', isMobile && 'mobile']">
    <div class="container" :style="{ backgroundImage: backgourndImageUrl }">
      <div class="user-layout-lang">
        <select-lang class="select-lang-trigger" />
      </div>
      <div class="user-layout-content">
        <div class="top">
          <div class="header">
            <a href="/">
              <template v-if="defaultLogoAndTitle">
                <img src="@/assets/images/logo.svg" class="default-logo" alt="logo" />
                <img src="@/assets/images/title.svg" class="default-title" alt="title" />
              </template>
              <template v-else>
                <img v-if="logo" :src="logo" class="logo" alt="logo" />
                <span class="title">{{ title }}</span>
              </template>
            </a>
          </div>
          <div class="desc"></div>
        </div>
        <router-view />
      </div>
      <div class="footer">
        <div class="copyright">{{ fullCopyright }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { deviceMixin } from '@/store/device-mixin'
import { serverMixin } from '@/store/server-mixin'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'UserLayout',
  mixins: [deviceMixin, serverMixin],
  components: {
    SelectLang
  },
  data() {
    return {}
  },
  computed: {
    backgourndImageUrl() {
      return `url('${process.env.BASE_URL}login-bg.png')`
    }
  },
  mounted() {
    document.body.classList.add('userLayout')
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
    position: relative;

    .user-layout-lang {
      width: 100%;
      height: 40px;
      line-height: 44px;
      text-align: right;

      .select-lang-trigger {
        cursor: pointer;
        padding: 12px;
        margin-right: 24px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        vertical-align: middle;
      }
    }

    a {
      text-decoration: none;
    }

    .user-layout-content {
      padding: 110px 0 144px;

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
            color: rgba(0, 0, 0, 0.85);
            font-family: Avenir, 'Helvetica Neue', Arial, Helvetica, sans-serif;
            font-weight: 600;
            position: relative;
            top: 2px;
          }

          .default-logo {
            height: 50px;
            margin-right: 16px;
          }

          .default-title {
            height: 50px;
            position: relative;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.65);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 260px;
        width: 368px;
        margin: 0 auto;
      }
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
          color: rgba(255, 255, 255, 0.65);
          transition: all 0.3s;
          &:not(:last-child) {
            margin-right: 40px;
          }
        }
      }
      .copyright {
        color: rgba(255, 255, 255, 0.65);
        font-size: 14px;
      }
    }
  }
}
</style>
