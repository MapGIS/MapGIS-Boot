<template>
  <a-list itemLayout="horizontal" :dataSource="data" :loading="loading">
    <a-list-item slot="renderItem" slot-scope="item, index" :key="index">
      <a-list-item-meta>
        <a slot="title">{{ item.title }}</a>
        <a-icon
          slot="avatar"
          :component="allIcon[item.type.icon + 'Icon']"
          :class="item.binded ? 'item-icon-active' : 'item-icon'"
        >
        </a-icon>
        <span slot="description">
          {{ item.description }}
        </span>
      </a-list-item-meta>
      <template v-if="item.actions">
        <a slot="actions" @click="item.binded ? unbind(item) : bind(item)">{{ item.actions.title }}</a>
      </template>
    </a-list-item>
    <!-- 第三方登录绑定账号密码输入弹框 -->
    <a-modal
      title="请输入登录账号密码进行绑定"
      :visible="thirdPasswordShow"
      @ok="thirdLoginCheckPassword"
      @cancel="thirdLoginNoPassword"
    >
      <a-input-password placeholder="请输入密码" v-model="thirdLoginPassword" />
    </a-modal>
  </a-list>
</template>

<script>
import { getAuthInfo, bindAuth, unbindAuth } from '@/api/system/authUser'
import { checkPassword } from '@/api/system/user'
import allIcon from '@/core/icons'

export default {
  data() {
    return {
      allIcon,
      loading: true,
      types: [
        {
          name: 'GitHub',
          source: 'github',
          icon: 'github'
        },
        {
          name: 'Gitee',
          source: 'gitee',
          icon: 'gitee'
        },
        {
          name: '自定义OAuth',
          source: 'custom',
          icon: 'oauth'
        }
      ],
      data: [],
      // 第三方登录相关信息
      thirdLoginInfo: '',
      thirdPasswordShow: false,
      thirdLoginPassword: '',
      authItem: undefined
    }
  },
  mounted() {
    this.getAuthInfo()
  },
  methods: {
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
            title: `绑定${type.name}`,
            description: binded ? `当前已经绑定${type.name}账号` : `当前未绑定${type.name}账号`,
            actions: {
              title: binded ? '解绑' : '绑定'
            }
          }
        })
        this.loading = false
      })
    },
    bind(item) {
      const loginUrl = window._CONFIG['domianURL'] + `/xxx/rest/services/auth/thirdLogin/render/${item.type.source}`
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
          if (token === '登录失败') {
            that.$message.warning(token)
          } else {
            that.$message.warning('第三方账号已绑定')
          }
        } else if (typeof token === 'object') {
          // 对象类型 说明需要提示是否绑定现有账号
          if (token['isObj'] === true) {
            that.thirdLoginInfo = { ...token }
            that.thirdLoginUserBind(item)
          }
        } else {
          that.$message.warning('不识别的信息传递')
        }

        window.removeEventListener('message', receiveMessage)
      }
      window.addEventListener('message', receiveMessage, false)
    },
    unbind(item) {
      this.$confirm({
        title: `您确定要解除${item.type.name}绑定吗?`,
        okText: '是',
        okType: 'danger',
        cancelText: '否',
        onOk() {
          unbindAuth({ authId: item.authId }).then(() => {
            item.binded = false
            item.authId = ''
            item.uuid = ''
            item.description = `当前未绑定${item.type.name}账号`
            item.actions.title = '绑定'
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
          this.$message.success(`当前已经绑定${this.authItem.type.name}账号`)
          this.authItem.binded = true
          this.authItem.authId = this.thirdLoginInfo.authId
          this.authItem.uuid = this.thirdLoginInfo.uuid
          this.authItem.description = `当前已经绑定${this.authItem.type.name}账号`
          this.authItem.actions.title = '解绑'
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
.item-icon {
  font-size: 32px;
}

.item-icon-active {
  font-size: 32px;
  color: @primary-color;
}
</style>
