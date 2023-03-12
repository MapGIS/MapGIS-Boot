<template>
  <div class="setting-drawer">
    <a-drawer width="300" placement="right" @close="onClose" :closable="false" :visible="visible" style="z-index: 999">
      <div class="setting-drawer-index-content">
        <setting-item :title="$t('app.setting.pagestyle')">
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
        </setting-item>

        <setting-item :title="$t('app.setting.themecolor')" divider>
          <div style="height: 20px">
            <a-tooltip class="setting-drawer-theme-color-colorBlock" v-for="(item, index) in colorList" :key="index">
              <template slot="title">
                {{ item.key }}
              </template>
              <a-tag :color="item.color" @click="handleChange('primaryColor', item.color)">
                <a-icon type="check" v-if="item.color === primaryColor"></a-icon>
              </a-tag>
            </a-tooltip>
          </div>
        </setting-item>

        <setting-item :title="$t('app.setting.navigationmode')" divider>
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
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>{{ $t('app.setting.content-width') }}</span>
                <a-select
                  size="small"
                  style="width: 90px"
                  :value="contentWidth"
                  @change="value => handleChange('contentWidth', value)"
                >
                  <a-select-option value="Fixed" v-if="layout === 'topmenu' || layout === 'mixmenu-center'">
                    {{ $t('app.setting.content-width.fixed') }}
                  </a-select-option>
                  <a-select-option value="Fluid">{{ $t('app.setting.content-width.fluid') }}</a-select-option>
                </a-select>
              </a-list-item>
              <a-list-item>
                <span>{{ $t('app.setting.fixedheader') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :disabled="layout === 'mixmenu'"
                  :checked="fixedHeader"
                  @change="checked => handleChange('fixedHeader', checked)"
                />
              </a-list-item>
              <a-list-item>
                <span :style="{ opacity: layout === 'topmenu' ? 0.5 : 1 }">{{ $t('app.setting.fixedsidebar') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :disabled="layout === 'topmenu'"
                  :checked="fixedSidebar"
                  @change="checked => handleChange('fixSiderbar', checked)"
                />
              </a-list-item>
              <a-list-item>
                <span>{{ $t('app.setting.hideFotter') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :checked="hideFooter"
                  @change="checked => handleChange('hideFooter', checked)"
                />
              </a-list-item>
              <a-list-item>
                <span>{{ $t('app.setting.hideBreadcrumb') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :checked="hideBreadcrumb"
                  @change="checked => handleChange('hideBreadcrumb', checked)"
                />
              </a-list-item>
              <a-list-item>
                <span>{{ $t('app.setting.multitab') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :checked="multiTab"
                  @change="checked => handleChange('multiTab', checked)"
                />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <setting-item :title="$t('app.setting.table.style')" divider>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>{{ $t('app.setting.table.size') }}</span>
                <a-radio-group
                  slot="actions"
                  :value="tableSize"
                  size="small"
                  button-style="solid"
                  @change="e => handleChange('tableSize', e.target.value)"
                >
                  <a-radio-button value="default">{{ $t('app.setting.table.size.default') }}</a-radio-button>
                  <a-radio-button value="middle">{{ $t('app.setting.table.size.middle') }}</a-radio-button>
                  <a-radio-button value="small">{{ $t('app.setting.table.size.small') }}</a-radio-button>
                </a-radio-group>
              </a-list-item>
              <a-list-item>
                <span>{{ $t('app.setting.table.bordered') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :checked="tableBordered"
                  @change="checked => handleChange('tableBordered', checked)"
                />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <setting-item :title="$t('app.setting.form.style')" divider>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>{{ $t('app.setting.form.popup') }}</span>
                <a-select
                  size="small"
                  style="width: 90px"
                  :value="formMode"
                  @change="value => handleChange('formMode', value)"
                >
                  <a-select-option value="Drawer">{{ $t('app.setting.form.popup.drawer') }}</a-select-option>
                  <a-select-option value="Modal">{{ $t('app.setting.form.popup.modal') }}</a-select-option>
                </a-select>
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <setting-item :title="$t('app.setting.othersettings')" divider>
          <div>
            <a-list :split="false">
              <a-list-item>
                <span>{{ $t('app.setting.weakmode') }}</span>
                <a-switch
                  slot="actions"
                  size="small"
                  :checked="colorWeak"
                  @change="checked => handleChange('colorWeak', checked)"
                />
              </a-list-item>
            </a-list>
          </div>
        </setting-item>

        <a-button @click="doCopy" icon="copy" block>{{ $t('app.setting.copy') }}</a-button>
      </div>
      <div class="setting-drawer-index-handle" @click="toggle" slot="handle">
        <a-icon type="setting" v-if="!visible" />
        <a-icon type="close" v-else />
      </div>
    </a-drawer>
  </div>
</template>

<script>
import SettingItem from './SettingItem'
import { updateTheme, updateColorWeak, getColorList } from './settingConfig'
import { baseMixin } from '@/store/app-mixin'
import { tableMixin } from '@/store/table-mixin'
import { formMixin } from '@/store/form-mixin'

export default {
  components: {
    SettingItem
  },
  mixins: [baseMixin, tableMixin, formMixin],
  data() {
    return {
      visible: false
    }
  },
  computed: {
    colorList() {
      return getColorList()
    }
  },
  watch: {},
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
      this.$emit('change', { type, value })
    },
    showDrawer() {
      this.visible = true
    },
    onClose() {
      this.visible = false
    },
    toggle() {
      this.visible = !this.visible
    },
    doCopy() {
      // get current settings from mixin or this.$store.state.app, pay attention to the property name
      const text = `module.exports = {
  navTheme: '${this.navTheme}', // theme for nav menu
  primaryColor: '${this.primaryColor}', // primary color of ant design
  layout: '${this.layout}', // nav menu position: sidemenu or topmenu
  contentWidth: '${this.contentWidth}', // layout of content: Fluid or Fixed, only works when layout is topmenu
  fixedHeader: ${this.fixedHeader}, // sticky header
  fixSiderbar: ${this.fixedSidebar}, // sticky siderbar
  colorWeak: ${this.colorWeak},
  multiTab: ${this.multiTab},
  tableSize: '${this.tableSize}',
  tableBordered: ${this.tableBordered},
  hideFooter: ${this.hideFooter},
  hideBreadcrumb: ${this.hideBreadcrumb},
  formMode: ${this.formMode},
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
}`
      this.$copyText(text)
        .then(message => {
          console.log('copy', message)
          this.$message.success(this.$t('app.setting.copyinfo'))
        })
        .catch(err => {
          console.log('copy.err', err)
          this.$message.error(this.$t('app.setting.copy.failed'))
        })
    }
  }
}
</script>

<style lang="less" scoped>
.setting-drawer-index-content {
  position: relative;
  min-height: 100%;

  .setting-drawer-index-blockChecbox {
    display: flex;

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
}

.setting-drawer-index-handle {
  position: absolute;
  top: 240px;
  background: #1890ff;
  width: 48px;
  height: 48px;
  right: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  pointer-events: auto;
  z-index: 1001;
  text-align: center;
  font-size: 16px;
  border-radius: 4px 0 0 4px;

  i {
    color: @btn-primary-color;
    font-size: 20px;
  }
}
</style>
