<template>
  <div class="setting-drawer-index-item">
    <h3 class="setting-drawer-index-title">{{ title }}</h3>
    <slot></slot>
    <a-divider v-if="divider" />
  </div>
</template>

<script>
export default {
  name: 'SettingItem',
  props: {
    title: {
      type: String,
      default: ''
    },
    divider: {
      type: Boolean,
      default: false
    }
  }
}
</script>

<style lang="less" scoped>
.setting-drawer-index-item {
  margin-bottom: 24px;

  .setting-drawer-index-title {
    font-size: 14px;
    color: @heading-color;
    line-height: 22px;
    margin-bottom: 12px;
  }
}
</style>
