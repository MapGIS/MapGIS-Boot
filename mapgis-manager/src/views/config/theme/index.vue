<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <a-form-model
        :labelCol="{ lg: { span: 7 }, sm: { span: 7 } }"
        :wrapperCol="{ lg: { span: 10 }, sm: { span: 17 } }"
      >
        <header-title :title="$t('app.setting.theme.style')"></header-title>
        <a-form-model-item :label="$t('app.setting.pagestyle')">
          <div class="setting-drawer-index-blockChecbox">
            <a-tooltip>
              <template slot="title">{{ $t('app.setting.pagestyle.dark') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('theme', 'dark')">
                <div class="setting-drawer-index-item-side setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="navTheme === 'dark'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>
            <a-tooltip>
              <template slot="title">{{ $t('app.setting.pagestyle.light') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('theme', 'light')">
                <div class="setting-drawer-index-item-light setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="navTheme === 'light'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>
            <a-tooltip>
              <template slot="title">{{ $t('app.setting.pagestyle.realdark') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('theme', 'night')">
                <div class="setting-drawer-index-item-night setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="navTheme === 'night'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>
          </div>
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.themecolor')">
          <div style="margin-top: 10px">
            <a-tooltip class="setting-drawer-theme-color-colorBlock" v-for="(item, index) in colorList" :key="index">
              <template slot="title">
                {{ item.key }}
              </template>
              <a-tag :color="item.color" @click="handleChange('primaryColor', item.color)">
                <a-icon type="check" v-if="item.color === primaryColor"></a-icon>
              </a-tag>
            </a-tooltip>
          </div>
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.navigationmode')">
          <div class="setting-drawer-index-blockChecbox">
            <a-tooltip>
              <template slot="title">{{ $t('app.setting.sidemenu') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('layout', 'sidemenu')">
                <div class="setting-drawer-index-item-side setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'sidemenu'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>

            <a-tooltip>
              <template slot="title">{{ $t('app.setting.topmenu') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('layout', 'topmenu')">
                <div class="setting-drawer-index-item-top setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'topmenu'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>

            <a-tooltip>
              <template slot="title">{{ $t('app.setting.mixmenu') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('layout', 'mixmenu')">
                <div class="setting-drawer-index-item-mix setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'mixmenu'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>

            <a-tooltip>
              <template slot="title">{{ $t('app.setting.mixmenu-center') }}</template>
              <div class="setting-drawer-index-item" @click="handleChange('layout', 'mixmenu-center')">
                <div class="setting-drawer-index-item-mix-center setting-drawer-index-item-com-style" />
                <div class="setting-drawer-index-selectIcon" v-if="layout === 'mixmenu-center'">
                  <a-icon type="check" />
                </div>
              </div>
            </a-tooltip>
          </div>
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.content-width')">
          <a-select style="width: 90px" :value="contentWidth" @change="value => handleChange('contentWidth', value)">
            <a-select-option value="Fixed" v-if="layout === 'topmenu' || layout === 'mixmenu-center'">
              {{ $t('app.setting.content-width.fixed') }}
            </a-select-option>
            <a-select-option value="Fluid">{{ $t('app.setting.content-width.fluid') }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.fixedheader')">
          <a-switch
            :disabled="layout === 'mixmenu'"
            :checked="fixedHeader"
            @change="checked => handleChange('fixedHeader', checked)"
          />
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.fixedsidebar')" :style="{ opacity: layout === 'topmenu' ? 0.5 : 1 }">
          <a-switch
            :disabled="layout === 'topmenu'"
            :checked="fixedSidebar"
            @change="checked => handleChange('fixSiderbar', checked)"
          />
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.hideFotter')">
          <a-switch :checked="hideFooter" @change="checked => handleChange('hideFooter', checked)" />
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.hideBreadcrumb')">
          <a-switch :checked="hideBreadcrumb" @change="checked => handleChange('hideBreadcrumb', checked)" />
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.multitab')">
          <a-switch :checked="multiTab" @change="checked => handleChange('multiTab', checked)" />
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.weakmode')">
          <a-switch :checked="colorWeak" @change="checked => handleChange('colorWeak', checked)" />
        </a-form-model-item>
        <header-title :title="$t('app.setting.table.style')"></header-title>
        <a-form-model-item :label="$t('app.setting.table.size')">
          <a-radio-group
            :value="tableSize"
            button-style="solid"
            @change="e => handleChange('tableSize', e.target.value)"
          >
            <a-radio-button value="default">{{ $t('app.setting.table.size.default') }}</a-radio-button>
            <a-radio-button value="middle">{{ $t('app.setting.table.size.middle') }}</a-radio-button>
            <a-radio-button value="small">{{ $t('app.setting.table.size.small') }}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
        <a-form-model-item :label="$t('app.setting.table.bordered')">
          <a-switch :checked="tableBordered" @change="checked => handleChange('tableBordered', checked)" />
        </a-form-model-item>
        <header-title :title="$t('app.setting.form.style')"></header-title>
        <a-form-model-item :label="$t('app.setting.form.popup')">
          <a-select style="width: 90px" :value="formMode" @change="value => handleChange('formMode', value)">
            <a-select-option value="Drawer">{{ $t('app.setting.form.popup.drawer') }}</a-select-option>
            <a-select-option value="Modal">{{ $t('app.setting.form.popup.modal') }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ lg: { span: 10, offset: 7 }, sm: { span: 17, offset: 7 } }">
          <a-button type="primary" @click="reset" icon="redo">{{ $t('reset') }}</a-button>
        </a-form-model-item>
      </a-form-model>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import {
  CONTENT_WIDTH_TYPE,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_FIXED_HEADER,
  TOGGLE_FIXED_SIDEBAR,
  TOGGLE_LAYOUT,
  TOGGLE_NAV_THEME,
  TOGGLE_WEAK,
  TOGGLE_COLOR,
  TOGGLE_MULTI_TAB,
  TABLE_SIZE,
  TABLE_BORDERED,
  HIDE_FOOTER,
  HIDE_BREADCRUMB,
  FORM_MODE,
  ENABLE_DEPARTMENT,
  ENABLE_POST
} from '@/store/mutation-types'
import { baseMixin } from '@/store/app-mixin'
import { tableMixin } from '@/store/table-mixin'
import { formMixin } from '@/store/form-mixin'
import { updateTheme, updateColorWeak, getColorList } from '@/components/SettingDrawer/settingConfig'
import defaultSettings from '@/config/defaultSettings'
import HeaderTitle from '@/components/HeaderTitle'

export default {
  mixins: [baseMixin, tableMixin, formMixin],
  components: { HeaderTitle },
  data() {
    return {
      settings: {
        // 布局类型
        layout: defaultSettings.layout, // 'sidemenu', 'topmenu', 'mixmenu'
        // CONTENT_WIDTH_TYPE
        contentWidth: defaultSettings.layout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : defaultSettings.contentWidth,
        // 主题 'dark' | 'light' | 'night'
        theme: defaultSettings.navTheme,
        // 主色调
        primaryColor: defaultSettings.primaryColor,
        fixedHeader: defaultSettings.fixedHeader,
        fixSiderbar: defaultSettings.fixSiderbar,
        multiTab: defaultSettings.multiTab,
        colorWeak: defaultSettings.colorWeak,

        tableSize: defaultSettings.tableSize,
        tableBordered: defaultSettings.tableBordered,
        hideFooter: defaultSettings.hideFooter,
        hideBreadcrumb: defaultSettings.hideBreadcrumb,
        formMode: defaultSettings.formMode,

        enableDepartment: defaultSettings.enableDepartment,
        enablePost: defaultSettings.enablePost
      }
    }
  },
  computed: {
    colorList() {
      return getColorList()
    }
  },
  mounted() {},
  methods: {
    handleChange(type, value) {
      if (type === 'primaryColor') {
        // 更新主色调
        updateTheme(this.navTheme, value)
      }
      if (type === 'theme') {
        // 更新主题模式
        updateTheme(value, this.primaryColor)
      }
      if (type === 'colorWeak') {
        updateColorWeak(value)
      }
      this.handleSettingChange({ type, value })
    },
    handleSettingChange({ type, value }) {
      switch (type) {
        case 'theme':
          this.$store.commit(TOGGLE_NAV_THEME, value)
          break
        case 'primaryColor':
          this.$store.commit(TOGGLE_COLOR, value)
          break
        case 'contentWidth':
          this.$store.commit(TOGGLE_CONTENT_WIDTH, value)
          break
        case 'layout':
          this.$store.commit(TOGGLE_LAYOUT, value)
          if (value !== 'topmenu' && value !== 'mixmenu-center') {
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fluid)
            if (value === 'mixmenu') {
              this.$store.commit(TOGGLE_FIXED_HEADER, true)
            }
          } else {
            this.$store.commit(TOGGLE_FIXED_SIDEBAR, false)
            this.$store.commit(TOGGLE_CONTENT_WIDTH, CONTENT_WIDTH_TYPE.Fixed)
          }
          break
        case 'fixSiderbar':
          this.$store.commit(TOGGLE_FIXED_SIDEBAR, value)
          break
        case 'fixedHeader':
          this.$store.commit(TOGGLE_FIXED_HEADER, value)
          break
        case 'multiTab':
          this.$store.commit(TOGGLE_MULTI_TAB, value)
          break
        case 'colorWeak':
          this.$store.commit(TOGGLE_WEAK, value)
          break
        case 'tableSize':
          this.$store.commit(TABLE_SIZE, value)
          break
        case 'tableBordered':
          this.$store.commit(TABLE_BORDERED, value)
          break
        case 'hideFooter':
          this.$store.commit(HIDE_FOOTER, value)
          break
        case 'hideBreadcrumb':
          this.$store.commit(HIDE_BREADCRUMB, value)
          break
        case 'formMode':
          this.$store.commit(FORM_MODE, value)
          break
        case 'enableDepartment':
          this.$store.commit(ENABLE_DEPARTMENT, value)
          break
        case 'enablePost':
          this.$store.commit(ENABLE_POST, value)
          break
      }
    },
    reset() {
      this.handleChange('layout', this.settings.layout)
      this.handleChange('contentWidth', this.settings.contentWidth)
      this.handleChange('theme', this.settings.theme)
      this.handleChange('primaryColor', this.settings.primaryColor)
      this.handleChange('fixedHeader', this.settings.fixedHeader)
      this.handleChange('fixSiderbar', this.settings.fixSiderbar)
      this.handleChange('multiTab', this.settings.multiTab)
      this.handleChange('colorWeak', this.settings.colorWeak)
      this.handleChange('tableSize', this.settings.tableSize)
      this.handleChange('tableBordered', this.settings.tableBordered)
      this.handleChange('hideFooter', this.settings.hideFooter)
      this.handleChange('hideBreadcrumb', this.settings.hideBreadcrumb)
      this.handleChange('formMode', this.settings.formMode)
      this.handleChange('enableDepartment', this.settings.enableDepartment)
      this.handleChange('enablePost', this.settings.enablePost)
    }
  }
}
</script>

<style lang="less" scoped>
.setting-drawer-index-blockChecbox {
  display: flex;
  line-height: 1.5;

  .setting-drawer-index-item {
    margin-right: 16px;
    position: relative;
    border-radius: 4px;
    cursor: pointer;

    img {
      width: 48px;
    }

    .setting-drawer-index-selectIcon {
      position: absolute;
      top: 0;
      right: 0;
      width: 100%;
      padding-top: 15px;
      padding-left: 24px;
      height: 100%;
      color: #1890ff;
      font-size: 14px;
      font-weight: 700;
    }
  }
}
.setting-drawer-theme-color-colorBlock {
  width: 20px;
  height: 20px;
  border-radius: 2px;
  float: left;
  cursor: pointer;
  margin-right: 8px;
  padding-left: 0px;
  padding-right: 0px;
  text-align: center;
  color: @checkbox-check-color;
  font-weight: 700;

  i {
    font-size: 14px;
  }
}
.setting-drawer-index-item-com-style {
  position: relative;
  width: 44px;
  height: 36px;
  margin-right: 8px;
  overflow: hidden;
  background-color: #f0f2f5;
  border-radius: 4px;
  box-shadow: 1px 2px 3px #d2d2d2;
  cursor: pointer;
}
.setting-drawer-index-item-com-style:before {
  position: absolute;
  top: 0;
  left: 0;
  width: 33%;
  height: 100%;
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-com-style:after {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 25%;
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-top:before {
  background-color: #f0f2f5;
  content: '';
}
.setting-drawer-index-item-top:after {
  background-color: #001529;
  content: '';
}
.setting-drawer-index-item-side:before {
  background-color: #001529;
  content: '';
  z-index: 1;
}
.setting-drawer-index-item-side:after {
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-mix:before {
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-mix:after {
  background-color: #001529;
  content: '';
}
.setting-drawer-index-item-light:after {
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-light:before {
  background-color: #fff;
  content: '';
}
.setting-drawer-index-item-night {
  background-color: #001529;
}
.setting-drawer-index-item-night:after {
  background-color: #001529;
  content: '';
}
.setting-drawer-index-item-night:before {
  background-color: #001529;
  content: '';
}
.setting-drawer-index-item-mix-center:before {
  background-color: #fff;
  content: '';
  width: 80%;
  height: 20%;
  top: 26%;
  left: 10%;
}
.setting-drawer-index-item-mix-center:after {
  background-color: #001529;
  content: '';
}
</style>
