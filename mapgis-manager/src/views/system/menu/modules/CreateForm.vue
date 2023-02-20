<template>
  <pop-dialog
    :mode="formMode"
    :title="formTitle"
    width="35%"
    :visible="open"
    :loading="submitLoading"
    @ok="submitForm"
    @cancel="onClose"
  >
    <a-form-model ref="form" :model="form" :rules="rules" v-bind="formLayout">
      <a-form-model-item :label="$t('security.menu.parent.menu')" prop="parentId">
        <a-tree-select
          v-model="form.parentId"
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="menuOptions"
          :placeholder="$t('please.select')"
          :replaceFields="{
            children: 'children',
            title: 'menuName',
            key: 'menuId',
            value: 'menuId'
          }"
          tree-default-expand-all
        >
        </a-tree-select>
      </a-form-model-item>
      <a-form-model-item :label="$t('security.menu.menu.type')" prop="menuType">
        <a-radio-group v-model="form.menuType" button-style="solid">
          <a-radio-button value="M">{{ $t('security.menu.menu.type.directory') }}</a-radio-button>
          <a-radio-button value="C">{{ $t('security.menu.menu.type.menu') }}</a-radio-button>
          <a-radio-button value="F">{{ $t('security.menu.menu.type.button') }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item :label="$t('icon')" prop="icon" v-if="form.menuType != 'F'">
        <a-space size="large">
          <a-icon :component="allIcon[form.icon + 'Icon']" v-if="form.icon && allIcon[form.icon + 'Icon']" />
          <a-icon :type="form.icon" v-if="form.icon && !allIcon[form.icon + 'Icon']" />
          <a-button type="dashed" @click="selectIcon"> {{ $t('security.menu.select.icon') }} </a-button>
          <a-button type="dashed" @click="removeIcon"> {{ $t('security.menu.remove.icon') }}</a-button>
          <a @click="cancelSelectIcon" v-if="iconVisible" style="margin-left: 8px">
            {{ $t('collapse') }}
            <a-icon type="up" />
          </a>
        </a-space>
      </a-form-model-item>
      <a-card :body-style="{ padding: '10px 20px' }" :bordered="false" v-if="iconVisible">
        <icon-selector v-model="form.icon" @change="handleIconChange" :svgIcons="iconList" :allIcon="allIcon" />
      </a-card>
      <a-form-model-item :label="$t('menu.name')" prop="menuName">
        <a-input v-model="form.menuName" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item :label="$t('order')" prop="orderNum">
        <a-input-number v-model="form.orderNum" :min="0" :max="9999" style="width: 100%" />
      </a-form-model-item>
      <a-form-model-item prop="isFrame" v-if="form.menuType != 'F'">
        <span slot="label">
          {{ $t('security.menu.whether.to.external.link') }}
          <a-tooltip>
            <template slot="title"> {{ $t('security.menu.external.link.description') }} </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.isFrame" button-style="solid">
          <a-radio-button value="0">{{ $t('yes') }}</a-radio-button>
          <a-radio-button value="1">{{ $t('no') }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="path" v-if="form.menuType != 'F'">
        <span slot="label">
          {{ $t('security.menu.route.path') }}
          <a-tooltip>
            <template slot="title"> {{ $t('security.menu.route.path.description') }} </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.path" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item prop="component" v-if="form.menuType == 'C'">
        <span slot="label">
          {{ $t('security.menu.component.path') }}
          <a-tooltip>
            <template slot="title"> {{ $t('security.menu.component.path.description') }}</template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
          <a-button type="dashed" size="small" style="margin: 0 8px" @click="useMicroPageCompoment">
            {{ $t('security.menu.use.micro.application.component') }}
          </a-button>
        </span>
        <a-input v-model="form.component" :placeholder="$t('please.input')" />
      </a-form-model-item>
      <a-form-model-item prop="perms" v-if="form.menuType != 'M'">
        <span slot="label">
          {{ $t('security.role.permission.identification') }}
          <a-tooltip>
            <template slot="title">
              {{
                $t('security.menu.permission.define.description', {
                  eg: '@PreAuthorize("@ss.hasPermi(\'system:user:list\')"'
                })
              }}
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-input v-model="form.perms" :placeholder="$t('please.input')" :maxLength="100" />
      </a-form-model-item>
      <a-form-model-item prop="visible" v-if="form.menuType != 'F'">
        <span slot="label">
          {{ $t('security.menu.whether.to.show') }}
          <a-tooltip>
            <template slot="title"> {{ $t('security.menu.whether.to.show.description') }} </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.visible" button-style="solid">
          <a-radio-button v-for="(d, index) in visibleOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="status" v-if="form.menuType != 'F'">
        <span slot="label">
          {{ $t('status') }}
          <a-tooltip>
            <template slot="title"> {{ $t('security.menu.status.description') }} </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item prop="isCache" v-if="form.menuType == 'C'">
        <span slot="label">
          {{ $t('security.menu.whether.to.cache') }}
          <a-tooltip>
            <template slot="title">
              {{ $t('security.menu.whether.to.cache.description') }}
            </template>
            <a-icon type="question-circle-o" />
          </a-tooltip>
        </span>
        <a-radio-group v-model="form.isCache" button-style="solid">
          <a-radio-button value="0">{{ $t('cache') }}</a-radio-button>
          <a-radio-button value="1">{{ $t('not.cache') }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm">{{ $t('ok') }}</a-button>
          <a-button type="dashed" @click="cancel">{{ $t('cancel') }}</a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { getMenu, addMenu, updateMenu } from '@/api/system/menu'
import allIcon from '@/core/icons'
import icons from '@/utils/requireIcons'
import IconSelector from '@/components/IconSelector'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    statusOptions: {
      type: Array,
      required: true
    },
    visibleOptions: {
      type: Array,
      required: true
    },
    menuOptions: {
      type: Array,
      required: true
    }
  },
  components: {
    IconSelector
  },
  data() {
    return {
      allIcon,
      iconVisible: false,
      iconList: icons,
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      },
      open: false,
      rules: {
        menuName: [
          { required: true, message: this.$t('not.empty.suffix', { content: this.$t('menu.name') }), trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: this.$t('not.empty.suffix', { content: this.$t('order') }), trigger: 'blur' }
        ],
        path: [
          {
            required: true,
            message: this.$t('not.empty.suffix', { content: this.$t('security.menu.route.path') }),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  methods: {
    filterIcons() {
      this.iconList = icons
      if (this.name) {
        this.iconList = this.iconList.filter(item => item.includes(this.name))
      }
    },
    onClose() {
      this.open = false
      this.iconVisible = false
    },
    hideIconSelect() {
      this.iconList = icons
      this.iconVisible = false
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.iconVisible = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: undefined,
        icon: undefined,
        menuType: 'M',
        orderNum: undefined,
        isFrame: '1',
        isCache: '0',
        visible: '0',
        status: '0'
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.$emit('select-tree')
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId
      } else {
        this.form.parentId = 0
      }
      this.open = true
      this.formTitle = this.$t('add.suffix', { content: this.$t('menu') })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.$emit('select-tree')
      getMenu(row.menuId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = this.$t('modify.suffix', { content: this.$t('menu') })
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.menuId !== undefined) {
            const modifyMessage = this.$t('modify.success')
            updateMenu(this.form)
              .then(response => {
                this.$message.success(modifyMessage, 3)
                this.open = false
                this.iconVisible = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            const addMessage = this.$t('add.success')
            addMenu(this.form)
              .then(response => {
                this.$message.success(addMessage, 3)
                this.open = false
                this.iconVisible = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          }
        } else {
          return false
        }
      })
    },
    handleIconChange(icon) {
      this.iconVisible = false
      this.form.icon = icon
    },
    changeIcon(type) {
      this.currentSelectedIcon = type
    },
    selectIcon() {
      this.iconVisible = !this.iconVisible
    },
    removeIcon() {
      this.form.icon = '#'
    },
    cancelSelectIcon() {
      this.iconVisible = false
    },
    useMicroPageCompoment() {
      this.$set(this.form, 'component', 'system/microPage/index')
    }
  }
}
</script>
