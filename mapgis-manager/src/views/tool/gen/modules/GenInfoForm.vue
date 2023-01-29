<template>
  <div>
    <a-form-model ref="genInfoForm" :model="info" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-row>
        <a-col :span="12">
          <a-form-model-item prop="tplCategory">
            <span slot="label"> {{ $t('dev.gen.template') }} </span>
            <a-select v-model="info.tplCategory" :placeholder="$t('please.select')" @change="tplSelectChange">
              <a-select-option value="crud">{{ $t('dev.gen.single.table') }}</a-select-option>
              <a-select-option value="tree">{{ $t('dev.gen.tree.table') }}</a-select-option>
              <a-select-option value="sub">{{ $t('dev.gen.primary.sub.table') }}</a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="packageName">
            <span slot="label">
              {{ $t('dev.gen.package.path') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.package.path.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.package.path') })"
              v-model="info.packageName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="moduleName">
            <span slot="label">
              {{ $t('dev.gen.module.name') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.module.name.desc') }}</template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.module.name') })"
              v-model="info.moduleName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="businessName">
            <span slot="label">
              {{ $t('dev.gen.business.name') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.business.name.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.business.name') })"
              v-model="info.businessName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item prop="functionName">
            <span slot="label">
              {{ $t('dev.gen.function.name') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.function.name.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input
              :placeholder="$t('please.prefix.input', { content: $t('dev.gen.function.name') })"
              v-model="info.functionName"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('security.menu.parent.menu') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.assign.to.menu') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-tree-select
              v-model="info.parentMenuId"
              :tree-data="menus"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :placeholder="$t('please.prefix.select', { content: $t('dev.gen.system.menu') })"
              :replaceFields="treeReplaceFields"
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.code.gen.mode') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.code.gen.mode.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-radio-group v-model="info.genType">
              <a-radio :value="'0'">{{ $t('dev.gen.zip.package') }}</a-radio>
              <a-radio :value="'1'">{{ $t('dev.gen.custom.path') }}</a-radio>
            </a-radio-group>
          </a-form-model-item>
        </a-col>
        <a-col :span="12"> </a-col>
        <a-col :span="24" v-if="info.genType == '1'">
          <a-form-model-item
            :labelCol="{ xs: { span: 6 }, sm: { span: 4 } }"
            :wrapperCol="{ xs: { span: 18 }, sm: { span: 20 } }"
          >
            <span slot="label">
              {{ $t('dev.gen.custom.path') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.custom.path.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-input v-model="info.genPath">
              <a-dropdown slot="suffix">
                <a-button style="width: 100%">
                  {{ $t('dev.gen.select.nearest.path') }} <a-icon type="down" />
                </a-button>
                <a-menu slot="overlay">
                  <a-menu-item key="1" @click.native="info.genPath = '/'">
                    <a-icon type="user" />1st menu item
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </a-input>
          </a-form-model-item>
        </a-col>
      </a-row>
      <a-row v-show="info.tplCategory == 'tree'">
        <a-divider orientation="left"> {{ $t('dev.gen.other.information') }} </a-divider>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.tree.code.field') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.tree.code.field.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeCode" :placeholder="$t('please.select')">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName">
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.tree.parent.code.field') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.tree.parent.code.field.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeParentCode" :placeholder="$t('please.select')">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName">
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.tree.name.field') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.tree.name.field.desc') }} </template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.treeName" :placeholder="$t('please.select')">
              <a-select-option v-for="(item, index) in info.columns" :key="index" :value="item.columnName">
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
      </a-row>
      <!-- 主子表 -->
      <a-row v-show="info.tplCategory == 'sub'">
        <a-divider orientation="left"> {{ $t('dev.gen.associated.information') }} </a-divider>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.subtable.name') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.subtable.name.desc') }}</template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.subTableName" :placeholder="$t('please.select')" @change="subSelectChange">
              <a-select-option v-for="(item, index) in tables" :key="index" :value="item.tableName">
                {{ item.tableName + '：' + item.tableComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="12">
          <a-form-model-item>
            <span slot="label">
              {{ $t('dev.gen.subtable.foreign.key') }}
              <a-tooltip>
                <template slot="title"> {{ $t('dev.gen.subtable.foreign.key.desc') }}</template>
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <a-select v-model="info.subTableFkName" :placeholder="$t('please.select')">
              <a-select-option v-for="(item, index) in subColumns" :key="index" :value="item.columnName">
                {{ item.columnName + '：' + item.columnComment }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
export default {
  name: 'GenInfoForm',
  props: {
    info: {
      type: Object,
      default: null
    },
    tables: {
      type: Array,
      default: null
    },
    menus: {
      type: Array,
      default: null
    }
  },
  data() {
    return {
      visible: false,
      loading: false,
      // 模态框数据
      data: {},
      subColumns: [],
      rules: {
        tplCategory: [
          {
            required: true,
            message: this.$t('please.prefix.select', { content: this.$t('dev.gen.template') }),
            trigger: 'blur'
          }
        ],
        packageName: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.package.path') }),
            trigger: 'blur'
          }
        ],
        moduleName: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.module.name') }),
            trigger: 'blur'
          }
        ],
        businessName: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.business.name') }),
            trigger: 'blur'
          }
        ],
        functionName: [
          {
            required: true,
            message: this.$t('please.prefix.input', { content: this.$t('dev.gen.function.name') }),
            trigger: 'blur'
          }
        ]
      },
      labelCol: {
        xs: { span: 12 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 12 },
        sm: { span: 16 }
      },
      // 类型数据字典
      typeOptions: [],
      // 类型数据字典
      statusOptions: [],
      treeReplaceFields: {
        children: 'children',
        title: 'menuName',
        key: 'menuId',
        value: 'menuId'
      }
    }
  },
  watch: {
    'info.subTableName': function (val) {
      this.setSubTableColumns(val)
    }
  },
  methods: {
    // 关闭模态框
    close() {
      this.visible = false
    },
    // 打开抽屉(由外面的组件调用)
    show(data) {
      if (data) {
        this.data = data
      }
      this.visible = true
    },
    /** 选择子表名触发 */
    subSelectChange() {
      this.info.subTableFkName = ''
    },
    /** 选择生成模板触发 */
    tplSelectChange(value) {
      if (value !== 'sub') {
        this.info.subTableName = ''
        this.info.subTableFkName = ''
      }
    },
    /** 设置关联外键 */
    setSubTableColumns(value) {
      for (const item in this.tables) {
        const name = this.tables[item].tableName
        if (value === name) {
          this.subColumns = this.tables[item].columns
          break
        }
      }
    }
  }
}
</script>
