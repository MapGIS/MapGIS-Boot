<template>
  <global-footer class="footer custom-render">
    <template v-slot:links> </template>
    <template v-slot:copyright> Copyright &copy; 2022 武汉中地数码科技有限公司 Version 10.5.8.10 </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@/components/ProLayout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
