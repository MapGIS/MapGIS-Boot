<template>
  <div class="header-title-wrapper">
    <div class="header-title-content">{{ title }}</div>
  </div>
</template>

<script>
export default {
  name: 'HeaderTitle',
  props: {
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style lang="less" scoped>
.header-title-wrapper {
  background-color: #f5f5f5;
  height: 40px;
  line-height: 40px;
  padding: 0 16px;
  margin-bottom: 16px;
  .header-title-content {
    font-size: 14px;
    font-weight: bold;
  }
}
</style>
