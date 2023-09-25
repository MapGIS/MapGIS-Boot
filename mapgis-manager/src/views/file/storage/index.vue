<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('file.storage.file.name')" prop="name">
                <a-input
                  v-model="queryParam.name"
                  :placeholder="$t('please.prefix.input', { content: $t('file.storage.file.name') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item :label="$t('file.storage.file.url')" prop="url">
                <a-input
                  v-model="queryParam.url"
                  :placeholder="$t('please.prefix.input', { content: $t('file.storage.file.url') })"
                  allow-clear
                />
              </a-form-item>
            </a-col>
            <a-col :md="(!advanced && 8) || 24" :sm="24">
              <span
                class="table-page-search-submitButtons"
                :style="(advanced && { float: 'right', overflow: 'hidden' }) || {}"
              >
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />{{ $t('query') }}</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery">
                  <a-icon type="redo" />{{ $t('reset') }}
                </a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
      <!-- 操作 -->
      <div class="table-operations">
        <a-button type="primary" @click="$refs.uploadFile.uploadFileHandleOpen()">
          <a-icon type="upload" />{{ $t('file.storage.file.upload') }}
        </a-button>
        <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['file:storage:remove']">
          <a-icon type="delete" />{{ $t('delete') }}
        </a-button>
        <a-button type="primary" @click="handleExport" v-hasPermi="['file:storage:export']">
          <a-icon type="download" />{{ $t('export') }}
        </a-button>
        <table-setting
          :style="{ float: 'right' }"
          :table-size.sync="tableSize"
          v-model="columns"
          :refresh-loading="loading"
          @refresh="getList"
        />
      </div>
      <!-- 查看详情 -->
      <detail-form ref="detailForm" />
      <!-- 上传文件 -->
      <upload-file ref="uploadFile" @ok="getList" />
      <!-- 数据展示 -->
      <a-table
        :loading="loading"
        :size="tableSize"
        rowKey="fileId"
        :columns="columns"
        :data-source="list"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="false"
        :bordered="tableBordered"
      >
        <span slot="thumbnail" slot-scope="text, record">
          <img :src="realThumbnail(record)" class="record-img" />
        </span>
        <span slot="createTime" slot-scope="text, record">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="text, record">
          <a @click="$refs.detailForm.handleLook(record)"><a-icon type="eye" />{{ $t('details') }}</a>
          <a-divider type="vertical" />
          <a :href="downloadUrl(record)" target="_blank"><a-icon type="download" />{{ $t('download') }}</a>
          <a-divider type="vertical" v-hasPermi="['file:storage:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['file:storage:remove']">
            <a-icon type="delete" />{{ $t('delete') }}
          </a>
        </span>
      </a-table>
      <!-- 分页 -->
      <a-pagination
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="totalItems"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { listFile, delFile } from '@/api/file/storage'
import DetailForm from './modules/DetailForm'
import UploadFile from './modules/UploadFile'
import { tableMixin } from '@/store/table-mixin'

export default {
  name: 'Storage',
  components: {
    DetailForm,
    UploadFile
  },
  mixins: [tableMixin],
  data() {
    return {
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {
        name: null,
        url: null,
        pageNum: 1,
        pageSize: 10
      },
      columns: [
        {
          title: this.$t('id'),
          dataIndex: 'fileId',
          width: '8%',
          ellipsis: true,
          align: 'center',
          visible: false
        },
        {
          title: this.$t('file.storage.file.engine'),
          dataIndex: 'engine',
          ellipsis: true,
          align: 'center',
          visible: false,
          width: 120
        },
        {
          title: this.$t('file.storage.file.bucket'),
          dataIndex: 'bucket',
          ellipsis: true,
          align: 'center',
          visible: false,
          width: 120
        },
        {
          title: this.$t('file.storage.file.name'),
          dataIndex: 'name',
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('file.storage.file.thumbnail'),
          dataIndex: 'thumbnail',
          scopedSlots: { customRender: 'thumbnail' },
          ellipsis: true,
          align: 'center',
          width: 80
        },
        {
          title: this.$t('file.storage.file.size.kb'),
          dataIndex: 'sizeKb',
          ellipsis: true,
          align: 'center',
          visible: false,
          width: 120
        },
        {
          title: this.$t('file.storage.file.size'),
          dataIndex: 'size',
          ellipsis: true,
          align: 'center',
          width: 120
        },
        {
          title: this.$t('file.storage.file.suffix'),
          dataIndex: 'suffix',
          ellipsis: true,
          align: 'center',
          width: 120
        },
        {
          title: this.$t('file.storage.file.url'),
          dataIndex: 'url',
          ellipsis: true,
          align: 'center',
          visible: false
        },
        {
          title: this.$t('file.storage.file.upload.time'),
          dataIndex: 'createTime',
          scopedSlots: { customRender: 'createTime' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: this.$t('operation'),
          dataIndex: 'operation',
          width: '25%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {},
  created() {
    this.getList()
  },
  computed: {
    downloadUrl() {
      return function (record) {
        if (record.engine === 'LOCAL') {
          return `${window.location.protocol}//${window.location.host}${window._CONFIG['domainURL']}${record.url}`
        }

        return record.url
      }
    },
    realThumbnail() {
      return function (record) {
        const imgSuffixs = ['png', 'jpg', 'jpeg', 'bmp', 'gif', 'ico']
        const fileSuffixs = [
          {
            suffixs: ['doc', 'docx'],
            img: require('@/assets/images/fileImg/docx.png')
          },
          {
            suffixs: ['xls', 'xlsx'],
            img: require('@/assets/images/fileImg/xlsx.png')
          },
          {
            suffixs: ['zip'],
            img: require('@/assets/images/fileImg/zip.png')
          },
          {
            suffixs: ['rar'],
            img: require('@/assets/images/fileImg/rar.png')
          },
          {
            suffixs: ['ppt', 'pptx'],
            img: require('@/assets/images/fileImg/ppt.png')
          },
          {
            suffixs: ['pdf'],
            img: require('@/assets/images/fileImg/pdf.png')
          },
          {
            suffixs: ['txt'],
            img: require('@/assets/images/fileImg/txt.png')
          },
          {
            suffixs: ['html'],
            img: require('@/assets/images/fileImg/html.png')
          }
        ]
        if (imgSuffixs.includes(record.suffix)) {
          return record.thumbnail
        } else {
          const result = fileSuffixs.find(item => {
            return item.suffixs.includes(record.suffix)
          })
          if (result) {
            return result.img
          } else {
            return require('@/assets/images/fileImg/file.png')
          }
        }
      }
    }
  },
  watch: {},
  methods: {
    totalItems(total) {
      const totalText = this.$t('result.total')
      const itemsText = this.$t('result.items')
      return `${totalText} ${total} ${itemsText}`
    },
    /** 查询文件列表 */
    getList() {
      this.loading = true
      listFile(this.queryParam).then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        name: undefined,
        url: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.fileId)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced() {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const that = this
      const fileIds = row.fileId || this.ids
      const messge = this.$t('delete.success')
      this.$confirm({
        title: this.$t('confirm.selected.data.delete'),
        content: this.$t('currently.selected.number') + fileIds + this.$t('is.data'),
        onOk() {
          return delFile(fileIds).then(() => {
            that.onSelectChange([], [])
            that.getList()
            that.$message.success(messge, 3)
          })
        },
        onCancel() {}
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const that = this
      this.$confirm({
        title: this.$t('confirm.export'),
        content: this.$t('export.condition.data.description'),
        onOk() {
          that.download(
            `${window._CONFIG['apiPathManagerPrefix']}/file/export`,
            {
              ...that.queryParam
            },
            `file_${new Date().getTime()}.xlsx`
          )
        },
        onCancel() {}
      })
    }
  }
}
</script>

<style lang="less" scoped>
.record-img {
  width: 24px;
  height: 24px;
}
</style>
