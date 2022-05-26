<template>
  <div class="user-login-other">
    <span>其他登录方式</span>
    <a v-for="type in types" :key="type.source" @click="onThirdLogin(type.source)" :title="type.name">
      <a-icon :component="allIcon[type.icon + 'Icon']" class="item-icon"> </a-icon>
    </a>
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

  .item-icon {
    font-size: 24px;
    color: rgba(0, 0, 0, 0.2);
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
