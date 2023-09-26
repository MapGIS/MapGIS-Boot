<template>
  <a-modal footer :title="$t('preview')" :destroyOnClose="true" :visible="visible" @cancel="onClose" :width="740">
    <xgplayer v-if="visible" :config="config" @player="onPlayer" />
  </a-modal>
</template>

<script>
import Xgplayer from 'xgplayer-vue'

export default {
  components: { Xgplayer },
  data() {
    return {
      visible: false,
      // 视频播放器配置
      config: {
        id: 'previewVideoPlayer',
        lang: 'zh-cn',
        // 宽度 100%
        fluid: true,
        // 开启倍速播放
        playbackRate: [0.5, 1, 1.5, 2],
        // 开启画中画
        pip: true,
        // 播放地址
        url: '',
        // 自动播放
        autoplay: true,
        // 跨域支持
        cors: true
      },
      // 视频播放器实例
      player: null
    }
  },
  methods: {
    preview(url) {
      this.config.url = url
      this.visible = true
    },
    /* 播放器渲染完成 */
    onPlayer(e) {
      this.player = e
      this.player.on('play', () => {})
    },
    // 关闭模态框
    onClose() {
      this.visible = false
    }
  }
}
</script>

<style></style>
