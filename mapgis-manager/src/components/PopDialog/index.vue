<template>
  <a-drawer v-if="isDrawerMode" :width="realWidth" :visible="visible" @close="$emit('cancel')">
    <a-divider orientation="left">
      <b>{{ title }}</b>
    </a-divider>
    <slot></slot>
  </a-drawer>
  <a-modal
    v-else
    :title="title"
    :width="realWidth"
    :visible="visible"
    :confirm-loading="loading"
    @ok="$emit('ok')"
    @cancel="$emit('cancel')"
  >
    <slot></slot>
  </a-modal>
</template>

<script>
import { FORM_MODE_TYPE } from '@/store/mutation-types'
import { baseMixin } from '@/store/app-mixin'

export default {
  name: 'PopDialog',
  mixins: [baseMixin],
  props: {
    mode: {
      type: String,
      default: FORM_MODE_TYPE.MODAL,
      validator: function (value) {
        return [FORM_MODE_TYPE.MODAL, FORM_MODE_TYPE.DRAWER].includes(value)
      }
    },
    title: {
      type: String,
      required: true
    },
    width: {
      type: [Number, String],
      default: 520
    },
    visible: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    isDrawerMode() {
      return this.mode === FORM_MODE_TYPE.DRAWER
    },
    realWidth() {
      return this.isMobile ? '98%' : this.width
    }
  }
}
</script>

<style></style>
