<template>
  <page-header-wrapper>
    <a-card>
      <a-form-model
        v-if="configLoaded"
        :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
        :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
        ref="form"
        :model="form"
        :rules="rules"
      >
        <a-form-model-item :label="$t('config.base.system.logo')" prop="logo">
          <a-upload
            :file-list="form.logo"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            :custom-request="customRequest"
            @change="handleFileChange"
            accept="image/png, image/jpeg, image/jpg"
          >
            <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="max-height: 100px; max-width: 100px" />
            <div v-else>
              <a-icon :type="logoLoading ? 'loading' : 'plus'" />
              <div class="ant-upload-text">{{ $t('upload') }}</div>
            </div>
          </a-upload>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 8 }" :label="$t('config.base.system.name')" prop="title">
          <a-input v-model="form.title" :placeholder="$t('config.base.system.name.placeholder')" />
        </a-form-model-item>
        <a-form-model-item :label="$t('config.base.system.copyright')" prop="copyright">
          <a-input v-model="form.copyright" :placeholder="$t('config.base.system.copyright.placeholder')" />
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
          <a-button type="primary" :loading="submitLoading" @click="submit" v-hasPermi="['system:config:edit']">
            {{ $t('save') }}
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import merge from 'lodash/merge'
import { getConfigByKey, updateConfig } from '@/api/system/config'

const defaultConfigValue = {
  header: {
    logo: null,
    title: ''
  },
  footer: {
    copyright: ''
  }
}

export default {
  data() {
    return {
      configLoaded: false,
      submitLoading: false,
      configInfo: {},
      form: {},
      rules: {
        logo: [{ required: true, message: this.$t('config.base.system.logo.placeholder'), trigger: 'blur' }],
        title: [{ required: true, message: this.$t('config.base.system.name.placeholder'), trigger: 'blur' }],
        copyright: [{ required: true, message: this.$t('config.base.system.copyright.placeholder'), trigger: 'blur' }]
      },
      logoLoading: false,
      imageUrl: ''
    }
  },
  mounted() {
    getConfigByKey('system.base').then(response => {
      this.configInfo = response.data
      const configValue = merge(defaultConfigValue, this.configInfo && JSON.parse(this.configInfo.configValue || '{}'))
      const {
        header: { logo }
      } = configValue
      if (logo) {
        this.imageUrl = logo
        configValue.header.logo = [{ url: logo, name: 'unique_name', uid: 'unique_uid' }]
      }

      this.form = Object.assign({}, this.form, {
        logo: configValue.header.logo,
        title: configValue.header.title,
        copyright: configValue.footer.copyright
      })

      this.configLoaded = true
    })
  },
  methods: {
    handleFileChange(info) {
      if (info.file.status === 'uploading') {
        this.logoLoading = true
      }
    },
    customRequest(data) {
      this.form.logo = []
      this.getBase64(data.file)
        .then(res => {
          this.imageUrl = res
          this.form.logo.push({ url: res, name: data.file.name, uid: data.file.uid })
          this.logoLoading = false
        })
        .catch(() => {
          this.logoLoading = false
        })
    },
    // 文件转base64，用于显示图片
    getBase64(file) {
      return new Promise((resolve, reject) => {
        // FileReader类就是专门用来读文件的
        const reader = new FileReader()
        reader.readAsDataURL(file)
        // 成功和失败返回对应的信息，reader.result一个base64，可以直接使用
        reader.onload = () => resolve(reader.result)
        // 失败返回失败的信息
        reader.onerror = error => reject(error)
      })
    },
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          this.configInfo.configValue = JSON.stringify({
            header: { logo: this.form.logo[0].url, title: this.form.title },
            footer: { copyright: this.form.copyright }
          })
          updateConfig(this.configInfo)
            .then(response => {
              this.$message.success(this.$t('setting.success'), 3)
            })
            .finally(() => {
              this.submitLoading = false
            })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.ant-upload-select-picture-card i {
  font-size: 24px;
}
.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
}
</style>
