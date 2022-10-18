<template>
  <div>
    <div class="user-login-other">
      <span>其他登录方式:</span>
      <a v-for="type in types" :key="type.source" @click="onThirdLogin(type.source)" :title="type.name">
        <a-icon :component="allIcon[type.icon + 'Icon']" class="item-icon"> </a-icon>
      </a>
    </div>
    <!-- 第三方登录绑定账号密码输入弹框 -->
    <a-modal
      title="请输入密码"
      :visible="thirdPasswordShow"
      @ok="thirdLoginCheckPassword"
      @cancel="thirdLoginNoPassword"
      :maskClosable="false"
    >
      <a-input-password placeholder="请输入密码" v-model="thirdLoginPassword" />
    </a-modal>

    <!-- 第三方登录提示是否绑定账号弹框 -->
    <a-modal
      :footer="null"
      :visible="thirdConfirmShow"
      :class="'ant-modal-confirm'"
      @cancel="thirdLoginNoConfirm"
      :maskClosable="false"
    >
      <div class="ant-modal-confirm-body-wrapper">
        <div class="ant-modal-confirm-body">
          <a-icon type="question-circle" style="color: #faad14" />
          <span class="ant-modal-confirm-title">提示</span>
          <div v-if="thirdLoginUserId" class="ant-modal-confirm-content">
            已有同名账号{{ this.thirdLoginUsername }}存在,请确认是否绑定该账号？
          </div>
          <div v-else class="ant-modal-confirm-content">请确认是否自动创建新账号？</div>
        </div>
        <div class="ant-modal-confirm-btns">
          <a-button
            @click="thirdLoginUserCreate"
            :loading="thirdCreateUserLoding"
            :type="thirdLoginUserId ? 'default' : 'primary'"
          >
            创建新账号
          </a-button>
          <a-button v-if="thirdLoginUserId" @click="thirdLoginUserBind" type="primary">确认绑定</a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { thirdLoginMixin } from '@/views/user/third/thirdLoginMixin'
import allIcon from '@/core/icons'

export default {
  name: 'ThirdLogin',
  mixins: [thirdLoginMixin],
  data() {
    return {
      allIcon,
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
      ]
    }
  }
}
</script>

<style lang="less" scoped>
.user-login-other {
  text-align: left;
  margin-top: 24px;
  line-height: 22px;
  color: rgba(255, 255, 255, 0.85);

  .item-icon {
    font-size: 24px;
    color: rgba(255, 255, 255, 0.85);
    margin-left: 16px;
    vertical-align: middle;
    cursor: pointer;
    transition: color 0.3s;

    & :hover {
      color: @primary-color;
    }
  }
}
</style>
