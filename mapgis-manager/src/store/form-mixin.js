import { FORM_MODE_TYPE } from '@/store/mutation-types'

const formMixin = {
  data() {
    return {
      formMode1: '',
      labelCol: 8,
      wrapperCol: 16
    }
  },
  computed: {
    formMode: {
      get() {
        return this.formMode1 || this.$store.state.app.formMode
      },
      set(value) {
        this.formMode1 = value
      }
    },
    formLayout() {
      return this.formMode === FORM_MODE_TYPE.MODAL
        ? {
            layout: 'horizontal',
            labelCol: { span: this.labelCol },
            wrapperCol: { span: this.wrapperCol }
          }
        : {
            layout: 'vertical'
          }
    }
  }
}

export { formMixin }
