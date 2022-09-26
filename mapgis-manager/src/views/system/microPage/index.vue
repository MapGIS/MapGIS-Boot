<template>
  <div></div>
</template>

<script>
import microAppRegister from '@/qiankun'

export default {
  name: 'MicroPage',
  mounted() {
    microAppRegister(this)
  }
}
</script>

<style lang="less" scoped></style>
