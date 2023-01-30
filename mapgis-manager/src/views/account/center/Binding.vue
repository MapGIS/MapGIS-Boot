<template>
  <a-list itemLayout="horizontal" :dataSource="data" :loading="loading">
    <a-list-item slot="renderItem" slot-scope="item, index" :key="index">
      <a-list-item-meta>
        <a slot="title" :class="item.binded ? 'item-title-active' : ''">{{ item.title }}</a>
        <img slot="avatar" :src="item.type.icon" style="width: 32px; height: 32px" />
        <span slot="description" :class="item.binded ? 'item-title-active' : ''">
          {{ item.description }}
        </span>
      </a-list-item-meta>
      <template v-if="item.actions">
        <a slot="actions" @click="item.binded ? unbind(item) : bind(item)">{{ item.actions.title }}</a>
      </template>
    </a-list-item>
    <!-- 第三方登录绑定账号密码输入弹框 -->
    <a-modal
      :title="$t('account.center.input.account.bind')"
      :visible="thirdPasswordShow"
      @ok="thirdLoginCheckPassword"
      @cancel="thirdLoginNoPassword"
    >
      <a-input-password :placeholder="$t('please.input.password')" v-model="thirdLoginPassword" />
    </a-modal>
  </a-list>
</template>

<script>
import { getAuthInfo, bindAuth, unbindAuth } from '@/api/system/authUser'
import { checkPassword } from '@/api/system/user'
import { getSystemConfig } from '@/api/system/config'

export default {
  data() {
    return {
      loading: true,
      types: [],
      data: [],
      // 第三方登录相关信息
      thirdLoginInfo: '',
      thirdPasswordShow: false,
      thirdLoginPassword: '',
      authItem: undefined
    }
  },
  async mounted() {
    await this.getAuthConfig()
    this.getAuthInfo()
  },
  methods: {
    async getAuthConfig() {
      await getSystemConfig().then(res => {
        this.types = res.data.oauthConfig
      })
    },
    getAuthInfo() {
      getAuthInfo().then(response => {
        this.data = this.types.map(type => {
          const authUser = response.auths.find(auth => auth.source === type.source)
          const binded = typeof authUser !== 'undefined'
          return {
            type,
            binded,
            authId: binded ? authUser.authId : '',
            uuid: binded ? authUser.uuid : '',
            title: this.$t('account.center.bind.type', { type: type.name }),
            description: binded
              ? this.$t('account.center.current.bound', { type: type.name })
              : this.$t('account.center.current.not.bound', { type: type.name }),
            actions: {
              title: binded ? this.$t('unbind') : this.$t('bind')
            }
          }
        })
        this.loading = false
      })
    },
    bind(item) {
      const loginUrl = `${window._CONFIG['domainURL']}${window._CONFIG['apiPathManagerPrefix']}/auth/thirdLogin/render/${item.type.source}`
      const that = this
      window.open(
        loginUrl,
        `login ${item.type.name}`,
        'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no'
      )
      this.thirdLoginInfo = ''
      const receiveMessage = function (event) {
        const token = event.data
        if (typeof token === 'string') {
          // 如果是字符串类型 说明是token信息
          if (token === 'Login Failed') {
            that.$message.warning(token)
          } else {
            that.$message.warning(that.$t('account.center.account.has.bound'))
          }
        } else if (typeof token === 'object') {
          // 对象类型 说明需要提示是否绑定现有账号
          if (token['isObj'] === true) {
            that.thirdLoginInfo = { ...token }
            that.thirdLoginUserBind(item)
          }
        } else {
          that.$message.warning(that.$t('user.login.unrecognized.information'))
        }

        window.removeEventListener('message', receiveMessage)
      }
      window.addEventListener('message', receiveMessage, false)
    },
    unbind(item) {
      const that = this
      this.$confirm({
        title: this.$t('account.center.confirm.unbind', { type: item.type.name }),
        okText: this.$t('yes'),
        okType: 'danger',
        cancelText: this.$t('no'),
        onOk() {
          unbindAuth({ authId: item.authId }).then(() => {
            item.binded = false
            item.authId = ''
            item.uuid = ''
            item.description = that.$t('account.center.current.not.bound', { type: item.type.name })
            item.actions.title = that.$t('bind')
          })
        },
        onCancel() {}
      })
    },
    // 绑定已有账号 需要输入密码
    thirdLoginUserBind(item) {
      this.thirdLoginPassword = ''
      this.thirdPasswordShow = true
      this.authItem = item
    },
    // 核实密码
    thirdLoginCheckPassword() {
      checkPassword({ password: this.thirdLoginPassword })
        .then(() => {
          return bindAuth({ uuid: this.thirdLoginInfo.uuid })
        })
        .then(() => {
          this.thirdLoginNoPassword()
          this.$message.success(this.$t('account.center.current.bound', { type: this.authItem.type.name }))
          this.authItem.binded = true
          this.authItem.authId = this.thirdLoginInfo.authId
          this.authItem.uuid = this.thirdLoginInfo.uuid
          this.authItem.description = this.$t('account.center.current.bound', { type: this.authItem.type.name })
          this.authItem.actions.title = this.$t('unbind')
        })
    },
    // 没有密码 取消操作
    thirdLoginNoPassword() {
      this.thirdPasswordShow = false
      this.thirdLoginPassword = ''
    }
  }
}
</script>

<style lang="less" scoped>
.item-title {
}

.item-title-active {
  color: @primary-color;
}
</style>
