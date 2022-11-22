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
      <a-form-model-item label="路由ID" prop="routeId">
        <a-input v-model="form.routeId" placeholder="请输入路由ID" />
      </a-form-model-item>
      <a-form-model-item label="服务地址" prop="uri">
        <a-input v-model="form.uri" placeholder="请输入服务地址" />
      </a-form-model-item>
      <a-form-model-item label="断言" prop="predicates">
        <div v-for="(item, index) in form.predicates" :key="index">
          <a-divider>
            {{ item.name }}
            <a-icon type="delete" size="22" @click="removePredicate(form.predicates, index)" />
          </a-divider>
          <div v-if="keyRoutes.includes(item.name)">
            <template v-for="(tag, tagIndex) in item.args">
              <a-input
                v-if="tagIndex == currentTagIndex && index == currentNameIndex"
                ref="input"
                type="text"
                size="small"
                :style="{ width: '190px' }"
                :value="tag"
                @change="handleInputChange"
                @blur="handleInputEditConfirm(item, tag, tagIndex)"
                @keyup.enter="handleInputEditConfirm(item, tag, tagIndex)"
                :key="tagIndex"
              />
              <a-tag
                v-else
                :key="tag"
                :closable="true"
                @close="() => removeTag(item, tag)"
                @click="editTag(tag, tagIndex, index)"
              >
                {{ tag }}
              </a-tag>
            </template>
            <a-input
              v-if="inputVisible && index == currentNameIndex"
              ref="input"
              type="text"
              size="small"
              :style="{ width: '100px' }"
              :value="inputValue"
              @change="handleInputChange"
              @blur="handleInputConfirm(item)"
              @keyup.enter="handleInputConfirm(item)"
            />
            <a-tag v-else style="borderstyle: dashed; cursor: pointer" @click="showInput(item, index)">
              <a-icon type="plus" />
              新建{{ item.name }}
            </a-tag>
          </div>
          <div v-if="!keyRoutes.includes(item.name)">
            <a-row v-for="(value, key) in item.args" :key="key">
              <a-col :span="5" style="margin-top: 2px">
                <span v-if="key == 'header'">Header名称</span>
                <span v-if="key == 'regexp'">参数值</span>
                <span v-if="key == 'param'">参数名</span>
                <span v-if="key == 'name'">Cookie名称</span>
              </a-col>
              <a-col :span="18">
                <a-input
                  :defaultValue="value"
                  placeholder="参数值"
                  style="width: 70%; margin-right: 8px; margin-top: 3px"
                  @change="e => valueChange(e, item.args, key)"
                />
              </a-col>
            </a-row>
          </div>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item :key="item.name" v-for="item in tagArray" @click="predicatesHandleMenuClick(item)">{{
                item.name
              }}</a-menu-item>
            </a-menu>
            <a-button type="dashed" style="width: 100%">
              添加断言
              <a-icon type="down" />
            </a-button>
          </a-dropdown>
        </p>
      </a-form-model-item>
      <a-form-model-item label="过滤器" prop="filters">
        <div v-for="(item, index) in form.filters" :key="index">
          <a-divider>
            {{ item.name }}
            <a-icon type="delete" size="22" @click="removeFilter(form.filters, index)" />
          </a-divider>
          <div v-for="tag in item.args" :key="tag.key">
            <a-input v-model="tag.key" placeholder="参数键" style="width: 45%; margin-right: 8px" />
            <a-input v-model="tag.value" placeholder="参数值" style="width: 40%; margin-right: 8px" />
            <a-icon class="dynamic-delete-button" type="minus-circle-o" @click="removeFilterParams(item, index)" />
          </div>
          <a-button type="dashed" style="margin-left: 28%; width: 30%" size="small" @click="addFilterParams(item)">
            <a-icon type="plus" />
            添加参数
          </a-button>
        </div>
        <p class="btn" style="padding-top: 10px">
          <a-dropdown>
            <a-menu slot="overlay" @click="filterHandleMenuClick">
              <a-menu-item :key="item.key" :name="item.name" v-for="item in filterArray">
                {{ item.title || item.name }}
              </a-menu-item>
            </a-menu>
            <a-button type="dashed" style="width: 100%">
              添加过滤器
              <a-icon type="down" />
            </a-button>
          </a-dropdown>
        </p>
      </a-form-model-item>
      <a-form-model-item label="顺序" prop="orderNum">
        <a-input v-model="form.orderNum" placeholder="请输入顺序" />
      </a-form-model-item>
      <a-form-model-item label="路由状态" prop="status">
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <div class="bottom-control">
        <a-space>
          <a-button type="primary" :loading="submitLoading" @click="submitForm"> 保存 </a-button>
          <a-button type="dashed" @click="cancel"> 取消 </a-button>
        </a-space>
      </div>
    </a-form-model>
  </pop-dialog>
</template>

<script>
import { getRoute, addRoute, updateRoute } from '@/api/system/route'
import { formMixin } from '@/store/form-mixin'

export default {
  name: 'CreateForm',
  mixins: [formMixin],
  props: {
    statusOptions: {
      type: Array,
      required: true
    }
  },
  components: {},
  data() {
    return {
      submitLoading: false,
      formTitle: '',
      // 表单参数
      form: {
        routeId: null,

        uri: null,

        predicates: null,

        filters: null,

        orderNum: null,

        status: '0'
      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        routeId: [{ required: true, message: '路由ID不能为空', trigger: 'blur' }],

        uri: [{ required: true, message: '服务地址不能为空', trigger: 'blur' }]
      },
      keyRoutes: ['Path', 'Host', 'Method', 'After', 'Before', 'Between', 'RemoteAddr'],
      // gateway对应的规则key
      tagArray: [
        {
          name: 'Path',
          args: []
        },
        {
          name: 'Header',
          args: {
            header: '',
            regexp: ''
          }
        },
        {
          name: 'Query',
          args: {
            param: '',
            regexp: ''
          }
        },
        {
          name: 'Method',
          args: []
        },
        {
          name: 'Host',
          args: []
        },
        {
          name: 'Cookie',
          args: {
            name: '',
            regexp: ''
          }
        },
        {
          name: 'After',
          args: []
        },
        {
          name: 'Before',
          args: []
        },
        {
          name: 'Between',
          args: []
        },
        {
          name: 'RemoteAddr',
          args: []
        }
      ],
      inputVisible: false,
      inputValue: '',
      currentNameIndex: 0,
      currentTagIndex: -1,
      filterArray: [
        {
          key: 1,
          name: 'StripPrefix',
          args: [
            {
              key: 'parts',
              value: '1'
            }
          ]
        },
        {
          key: 2,
          name: 'RewritePath',
          args: [
            {
              key: 'regexp',
              value: ''
            },
            {
              key: 'replacement',
              value: ''
            }
          ]
        },
        {
          key: 3,
          name: 'AddRequestHeader',
          args: [
            {
              key: 'name',
              value: ''
            },
            {
              key: 'value',
              value: ''
            }
          ]
        },
        {
          key: 4,
          name: 'AddRequestParameter',
          args: [
            {
              key: 'name',
              value: ''
            },
            {
              key: 'value',
              value: ''
            }
          ]
        },
        {
          key: 5,
          name: 'AddResponseHeader',
          args: [
            {
              key: 'name',
              value: ''
            },
            {
              key: 'value',
              value: ''
            }
          ]
        },
        {
          key: 6,
          name: 'PrefixPath',
          args: [
            {
              key: 'prefix',
              value: ''
            }
          ]
        },
        {
          key: 7,
          name: 'RedirectTo',
          args: [
            {
              key: 'status',
              value: ''
            },
            {
              key: 'url',
              value: ''
            }
          ]
        },
        {
          key: 8,
          name: 'RemoveRequestHeader',
          args: [
            {
              key: 'name',
              value: ''
            }
          ]
        },
        {
          key: 9,
          name: 'RemoveResponseHeader',
          args: [
            {
              key: 'name',
              value: ''
            }
          ]
        },
        {
          key: 10,
          name: 'RewriteResponseHeader',
          args: [
            {
              key: 'name',
              value: ''
            },
            {
              key: 'regexp',
              value: ''
            },
            {
              key: 'replacement',
              value: ''
            }
          ]
        },
        { key: 11, title: '缓存请求过滤器', name: 'CacheRequestFilter', args: [] },
        { key: 12, title: '验证码过滤器', name: 'ValidateCodeFilter', args: [] }
      ]
    }
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    onClose() {
      this.open = false
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.formType = 1
      this.form = {
        routeId: null,

        uri: null,

        predicates: null,

        filters: null,

        orderNum: null,

        status: '0'
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.formType = 1
      this.open = true
      this.formTitle = '添加'
      this.form.predicates = []
      this.form.filters = []
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      this.formType = 2
      const gatewayRouteId = row ? row.gatewayRouteId : ids
      getRoute(gatewayRouteId).then(response => {
        this.form = response.data
        this.form.predicates = JSON.parse(this.form.predicates)
        this.form.filters = JSON.parse(this.form.filters)
        this.open = true
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.form.predicates = JSON.stringify(this.form.predicates)
          this.form.filters = JSON.stringify(this.form.filters)
          this.submitLoading = true
          if (this.form.gatewayRouteId !== undefined && this.form.gatewayRouteId !== null) {
            updateRoute(this.form)
              .then(response => {
                this.$message.success('修改成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            addRoute(this.form)
              .then(response => {
                this.$message.success('新增成功', 3)
                this.open = false
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
    // 删除断言配置项
    removeTag(item, removedTag) {
      const tags = item.args.filter(tag => tag !== removedTag)
      item.args = tags
    },
    // 添加断言
    predicatesHandleMenuClick(e) {
      this.form.predicates.push({
        name: e.name,
        args: e.args
      })
    },
    editTag(tag, tagIndex, index) {
      this.currentNameIndex = index
      this.currentTagIndex = tagIndex
    },
    /**
     * 值修改事件
     * @param e
     * @param item
     * @param key
     */
    valueChange(e, item, key) {
      item[key] = e.target.value
    },
    // 显示输入框
    showInput(item, index) {
      this.inputVisible = true
      this.currentNameIndex = index
    },
    // 断言参数输入框失去焦点事件
    handleInputChange(e) {
      this.inputValue = e.target.value
    },
    // 删除断言
    removePredicate(predicates, index) {
      predicates.splice(index, 1)
    },
    // 输入框确认
    handleInputConfirm(item) {
      const inputValue = this.inputValue
      const tags = item.args
      if (inputValue && tags.indexOf(inputValue) === -1) {
        item.args = [...tags, inputValue]
      }
      Object.assign(this, {
        tags,
        inputVisible: false,
        inputValue: ''
      })
      this.currentTagIndex = -1
    },
    // 输入框确认
    handleInputEditConfirm(item, tag, index) {
      if (this.inputValue) {
        const inputValue = this.inputValue
        item.args[index] = inputValue
      }
      this.currentTagIndex = -1
    },
    // 过滤器添加事件
    filterHandleMenuClick(e) {
      const filter = this.filterArray.find(value => {
        return value.key === e.key
      })
      if (filter) {
        this.form.filters.push({ name: filter.name, args: filter.args })
      }
    },
    // 删除过滤器
    removeFilter(filters, index) {
      filters.splice(index, 1)
    },
    // 添加过滤器参数
    addFilterParams(item) {
      item.args.push({
        key: 'key' + item.args.length + 1,
        value: ''
      })
    },
    // 删除过滤器参数
    removeFilterParams(item, index) {
      item.args.splice(index, 1)
    }
  }
}
</script>
