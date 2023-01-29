<template>
  <a-spin :spinning="loading" :tip="$t('loading...')">
    <div :style="'height:' + height">
      <iframe :src="src" frameborder="no" style="width: 100%; height: 100%" scrolling="auto" />
    </div>
  </a-spin>
</template>

<script>
export default {
  name: 'Swagger',
  components: {},
  data() {
    return {
      src: window._CONFIG['domainURL'] + '/webjars/swagger-ui/index.html',
      height: document.documentElement.clientHeight - 94.5 + 'px;',
      loading: true
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted: function () {
    setTimeout(() => {
      this.loading = false
    }, 230)
    const that = this
    window.onresize = function temp() {
      that.height = document.documentElement.clientHeight - 94.5 + 'px;'
    }
  },
  methods: {}
}
</script>
