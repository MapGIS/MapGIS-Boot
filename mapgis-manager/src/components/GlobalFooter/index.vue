<template>
  <global-footer class="footer custom-render">
    <template v-slot:links> </template>
    <template v-slot:copyright> {{ copyright }} </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@/components/ProLayout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  },
  props: {
    copyright: {
      type: String,
      default: ''
    }
  }
}
</script>
