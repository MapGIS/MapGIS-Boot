import { FORM_MODE_TYPE } from '@/store/mutation-types'

const formMixin = {
  data() {
    return {
      formMode1: '',
      labelCol: { lg: { span: 7 }, sm: { span: 7 } },
      wrapperCol: { lg: { span: 13 }, sm: { span: 17 } }
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
            labelCol: this.labelCol,
            wrapperCol: this.wrapperCol
          }
        : {
            layout: 'vertical'
          }
    }
  }
}

export { formMixin }
