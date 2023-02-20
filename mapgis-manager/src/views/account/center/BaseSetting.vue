<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form-model
          :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
          :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
          :rules="rules"
          :model="user"
          ref="form"
        >
          <a-form-model-item :label="$t('username')" ref="userName" prop="userName">
            <span>{{ user.userName }}</span>
          </a-form-model-item>
          <a-form-model-item :label="$t('role')" ref="roleGroup">
            <span>{{ roleGroup }}</span>
          </a-form-model-item>
          <a-form-model-item :label="$t('usergroup')" ref="userGroup">
            <span>{{ userGroup }}</span>
          </a-form-model-item>
          <a-form-model-item :label="$t('department')" ref="deptName">
            <span>{{ user.dept && user.dept.deptName }}</span>
          </a-form-model-item>
          <a-form-model-item :label="$t('post')" ref="postGroup">
            <span>{{ postGroup }}</span>
          </a-form-model-item>
          <a-form-model-item :label="$t('user.nickname')" ref="nickName" prop="nickName">
            <a-input v-model="user.nickName" :placeholder="$t('please.input')" :maxLength="30" />
          </a-form-model-item>
          <a-form-model-item :label="$t('user.info')" prop="remark">
            <a-input v-model="user.remark" :placeholder="$t('please.input')" type="textarea" />
          </a-form-model-item>
          <a-form-model-item :label="$t('mobile.phone.number')" prop="phonenumber">
            <a-input v-model="user.phonenumber" :placeholder="$t('please.input')" />
          </a-form-model-item>
          <a-form-model-item :label="$t('email')" prop="email">
            <a-input v-model="user.email" :placeholder="$t('please.input')" />
          </a-form-model-item>
          <a-form-model-item :label="$t('sex')" prop="sex">
            <a-radio-group v-model="user.sex">
              <a-radio :value="sexValue.men">{{ $t('male') }}</a-radio>
              <a-radio :value="sexValue.women">{{ $t('female') }}</a-radio>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
            <a-button type="primary" :loading="submitLoading" @click="submit" icon="save">{{ $t('save') }}</a-button>
          </a-form-model-item>
        </a-form-model>
      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(1)">
          <a-icon type="cloud-upload-o" class="upload-icon" />
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="avatar" />
        </div>
      </a-col>
    </a-row>
    <avatar-modal ref="modal" @ok="setavatar" />
  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import { getUserProfile, updateUserProfile } from '@/api/system/user'
import { mapGetters } from 'vuex'

export default {
  name: 'BaseSettings',
  components: {
    AvatarModal
  },
  data() {
    return {
      submitLoading: false,
      // cropper
      sexValue: {
        men: '0',
        women: '1'
      },
      user: {},
      roleGroup: '',
      userGroup: '',
      postGroup: '',
      preview: {},
      option: {
        img: this.avatar,
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },
      rules: {
        nickName: [{ required: true, message: this.$t('please.input.nickname'), trigger: 'blur' }],
        email: [
          {
            type: 'email',
            message: this.$t('please.input.correct.email'),
            trigger: ['blur', 'change']
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: this.$t('please.input.correct.mobile.phone.number'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['nickname', 'avatar'])
  },
  mounted() {
    this.getUser()
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data
        this.roleGroup = response.roleGroup
        this.userGroup = response.userGroup
        this.postGroup = response.postGroup
      })
    },
    setavatar(url) {
      this.option.img = url
    },
    submit() {
      this.submitLoading = true
      updateUserProfile(this.user)
        .then(response => {
          this.$notification.open({
            message: this.$t('tip'),
            description: this.$t('modify.success'),
            icon: <a-icon type="check" style="color: #1890FF" />,
            duration: 3
          })
        })
        .finally(() => {
          this.submitLoading = false
        })
    }
  }
}
</script>

<style lang="less" scoped>
.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img,
  .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>
