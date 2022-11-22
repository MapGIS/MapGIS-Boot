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
    <a-form-model ref="form" :model="form" :rules="realRules" v-bind="formLayout">
      <a-form-model-item label="登录方式" v-if="form.configId == undefined">
        <a-select placeholder="登录方式" :value="authType" @change="handleAuthTypeChange">
          <a-select-option v-for="(d, index) in authTypeOptions" :key="index" :value="d.typeValue">
            {{ d.typeName }}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item label="登录平台" :prop="form.configId ? '' : 'type'">
        <a-input
          v-model="form.type"
          placeholder="请输入登录平台"
          :disabled="form.configId !== undefined || isDefaultAuthType(form.type)"
        />
      </a-form-model-item>
      <a-form-model-item label="登录名称" prop="name">
        <a-input v-model="form.name" placeholder="请输入登录名称" />
      </a-form-model-item>
      <div style="display: flex; flex-flow: row-reverse">
        <div v-if="isDefaultAuthType(form.type)">
          获取客户端ID、客户端秘钥请参考：<a :href="form.help" target="_blank">网站接入说明</a>
        </div>
      </div>
      <a-form-model-item label="客户端ID" prop="clientId">
        <a-input v-model="form.clientId" placeholder="请输入客户端ID" />
      </a-form-model-item>
      <a-form-model-item label="客户端秘钥" prop="clientSecret">
        <a-input v-model="form.clientSecret" placeholder="请输入客户端秘钥" />
      </a-form-model-item>
      <a-form-model-item label="回调地址" prop="redirectUri">
        <a-input v-model="form.redirectUri" placeholder="请输入回调地址" />
      </a-form-model-item>
      <a-form-model-item label="授权请求类" prop="authRequestClass" v-if="!isDefaultAuthType(form.type)">
        <a-input v-model="form.authRequestClass" placeholder="请输入内容" />
      </a-form-model-item>
      <a-form-model-item label="登录图标" prop="icon">
        <a-upload
          :file-list="authIcons"
          list-type="picture-card"
          class="avatar-uploader"
          :show-upload-list="false"
          :custom-request="customRequest"
          @change="handleFileChange"
          accept="image/png, image/jpeg, image/jpg"
        >
          <img v-if="form.icon" :src="form.icon" alt="avatar" style="max-height: 24px; max-width: 24px" />
          <div v-else>
            <a-icon :type="iconLoading ? 'loading' : 'plus'" />
          </div>
        </a-upload>
      </a-form-model-item>
      <a-form-model-item label="状态" prop="status">
        <a-radio-group v-model="form.status" button-style="solid">
          <a-radio-button v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{
            d.dictLabel
          }}</a-radio-button>
        </a-radio-group>
      </a-form-model-item>
      <a-form-model-item label="备注" prop="remark">
        <a-input v-model="form.remark" placeholder="请输入内容" type="textarea" allow-clear />
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
import { getAuthConfig, addAuthConfig, updateAuthConfig } from '@/api/system/authConfig'
import { formMixin } from '@/store/form-mixin'

const defaultAuthTypeOptions = [
  {
    typeName: 'QQ登录',
    typeValue: 'QQ',
    icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAFHGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDIwLTA0LTE4VDE4OjI3OjUyKzA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyMC0wNC0xOFQxODo1NDoxNCswODowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAyMC0wNC0xOFQxODo1NDoxNCswODowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpmYzE5MmI1OC0zYzQ2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',
    help: 'https://wiki.connect.qq.com/网站接入流程'
  },
  {
    typeName: '微博登录',
    typeValue: 'WEIBO',
    icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAFGUlEQVRYR+1WbYhUZRR+nnvve/3c8otSd+741e7eVYpKMMMQkpKEyizLSEyMKHA1KUpSBPuRghDGivZhKYZmmNKPyij6xsh0iVyIZma3NOfuruX60drKuveduSfu7Mwwszv7UQr98f117znve85zznPOeV/if178n/3jKoArnoF4xF4slF0kLYh0gDwJSB1F3qryUoe6U37lAThqG8jlpWpLRN5xPf0EgSCnv+IAYuUYTdrzBEFT6MQUGRaYxhqAszJORd53Pb3oPwFojNpT0xI8KDQsiiRdT+8caBclovZ6AV7qwiDLqj29K/weUAYSUbU0AJ4heGuRw0CedJv0ju4gYo7aTPJZETQalE+Ndr2u4hwuxBy1lWQNIG3ppL5uGuD3CeDECIy4dI39NoGHSkYq+Mj1/Pu76+JRtQfg4rxc5Kjp6dll18P6y1atIIdA0vNdL/1hrwAaHGt2msY+AmP7SPN+N+k/EuobgUHp8ZgolhoedOjEtFa0x8ZiImz7IIGpyGYr5tgfkFgAyBY3qVeVBBAvNxeKaewlqPrkWGQHiUMiXCSQuzOt18VxisTzblLXJhxVI+RWiLzuenp5PGpvArAaIvtcTz/aA0A8as0S8Ot+nQ+g+uxL/lh/sFoEsBbAJjfpvxh31BsgnwZwwE36DxcB+HU8HG2peoIjB2C/3y0B/fFD2tDRWWbvNQJZW9msj8UcdZjkTAg2uJ6/Lg9AACMeVUcJTu9hWSQQoM4AYgGZDLNcsGc6IHMJ2t3O/e4m/Uk9CtRRrwF4SkTuqm5KfZMHEItaawhjY/EBaRORl4dLaqfThHO9hRwfgzIZqh4DuJ7AuOzAOSPCVW6T/x6LAReZyQAIDWCoagU4KKcVyHarXb8Q9m9O9ttIXKuHqwrVrhunnEdbj+hCO0PUJyDvKGjBOiOll1aeQqxUAF0AItZqGEZYnV1LgnWul9qQ+820k1LbCcwBaWblB6xOv+aGP3G60HAI0i+z44XtKyIX7bSeOqUFIX0lMhBVXwKck03dUdfTt+V2xSP2TaD8kBkePYvjtAneWZH0fylUxSJqJQ1u6UbnHjepl/QCwD4OIFMwRjp9b2Vz+mAmEYCZiNo/A3B74x+Cz1zPv6dQn4ioGWLwSLczx92kP6U0ACczHseESvW3PyLHb6zcup2m8T0gFwC+KQHSNKQGYFmeLcj56qQeVWg4nKIBjW8LZSJypNrTM3uj4LvcdTmowx83qRV/dNWGvRgG9hDyeFVS787IHLUT5LK8IZE619MziijIXkaFMoo8V+XpV0sCyI/LbldlrByVNO2EIH1fdTL98YmJGNwZ2McAVOUMUWRFlae35f7DYZayVGNhR0HQNEz8SqcJHSUBhFzHHVVPchoELZbv35Kr7pijXgGwjMB+AeeRiGaLVQBuDKdZzujJKEZ2QH0F8OY8RSIpEDOrk/rHUnWUH0QNUUxOi6ojOQoi9XZKz598CidDL4movVBEVoCspIgpwBemoLaySecLLRExFwjNWhBOgfOzBmRBqbdgPoOFqI6Pw4ROy95OYm7mQQnskrTscVtSh0tNMy+CURepHgCxsjBqiAQg3jXTem1FMzJPs95Wyes4ETHnCY1NIG/MtuMpiDTkURMWhBNARIoMi3QIsZspvdltQaIvxyUzUNQ2ABui1qwAXEJghggckqOz/IcRnhHBaYLNIOop+CkI/M+rm3F2II77BfBvjFzO3gE9Si/HQX9nrwL4B973Lz8Er+OXAAAAAElFTkSuQmCC',
    help: 'https://open.weibo.com/wiki/网站接入介绍'
  },
  {
    typeName: 'GitHub登录',
    typeValue: 'GITHUB',
    icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADoUlEQVRYR+1XTYgcRRT+vp51d6IxqBEF/yJuKztdtQkBgyIaQRCNh+AhB9GD6EFBQfcqIlHBnJS4F0ERL4oKnkTEYNCDv7gJGjP1OjsyqwNmgwR/DjuR2djdT2rtDp1hmM02Q/biO3X1+/vqvVevXhHrTFxn//gfwJkIGGN2k/xQVXsk31XVZ0Xkt1GkqNFo7KzVajOquotkXVV3i8hH3nYZwOskHyscquppAK8BeE5EulWAhGG4aWJiYh+AJ0ie8aWqb4jI42cBsNYeAnDzAEcnAewFEAHYCuACAJMArsxl/wLwMwAP8h8AbQBHVTUF8BLJywfYPOyc29EfgUWSV1XZaQWdRefcNf0AOiS3VDBWReUX59wN/QAOkLynirW16qjqARHZ1V8Dz+e5Xqu9KvIvOOe8v/9OwdTU1OaxsbEfAVxdxVoFncUkSbbNz8//sQLAGPMByT0VDFVWUdX3RORBWmv90fNH8LySqmZJkoSMoujVIAiePq/ec2equtdHwOfeN5j1oG9ojPmd5OZ+76oqQRA82Ww2vwSQFXxjzEaSHvD4AMSnu93u951Op+d5YRhOjI+P30XSd8TtA+RPegApyaDM9AWyvLz8SLvdXh5FWCYnJ6+o1+uHSF7X5yfzKfA9/KKCoarficitpR3fDuCtfP2oiHw1DJQxZqC8tbahqkdIliN3ygP4FcBKX/aUZdl9cRx/UqyttV8DuC1ff+ucK74H4hgmb4zZT3KmpHjcA/gUwN3Fz16vVy+H3lp7CsCFnq+qXRG5eFgEhsn76JD0NVXQQQ/A39fP5A6WRGRT2YExZonkxnMFMEx+enp6SlWPlezv80Xoq/Sz4qdzbgyAv8tXaJQpiKLojiAIvihsp2l6p2/FgbW2A+DavAZuieN4btRFmG/mKQCzue3jzrktxV0wQ3J/DmA2juNyoQxL+Zp4fdGccc7NFnNazRjjz+l2PwuS3OGcO7om66sIR1H0UBAE7+S1NCci/jSl5aH0MgCfk9ymqif8gOqc+3gEIGrWWj/svgJgg6rOBUFwb7PZ9LPk2Q+TvM2+DeD+AimAgwCWABzp9Xo/LCws+CF1EAXGmK0kL8lTuYHkTgB7SIYA/lbVF0Xk5XKRD3wZRVEUknwYwAO5cuVA5Mfu/SRJ3my1Wif6Da36NGs0GrZWq/nauInkjfk47vUuVdXrSa70DVX9Mx/J2yR/yrKsnabp4Var1RqGflUAlbd+jorrDuBfcLOa2gefZKQAAAAASUVORK5CYII=',
    help: 'https://docs.github.com/cn/developers/apps/building-oauth-apps'
  },
  {
    typeName: 'Gitee登录',
    typeValue: 'GITEE',
    icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAA7EAAAOxAGVKw4bAAAF7GlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDIwLTA0LTE4VDE4OjUxOjUzKzA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAyMC0wNC0xOFQxOToxNDo0NiswODowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAyMC0wNC0xOFQxOToxNDo0NiswODowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1Zjg5MzYyZi02NDA0LTlhNGMtOGEzZS04ZmM4MmFlOWNhNzMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OGVmN2RiMmYtODlmOC0wNTQ4LTg1MWEtYzc1M2M0MzY2M2ZiIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6OGVmN2RiMmYtODlmOC0wNTQ4LTg1MWEtYzc1M2M0MzY2M2ZiIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo4ZWY3ZGIyZi04OWY4LTA1NDgtODUxYS1jNzUzYzQzNjYzZmIiIHN0RXZ0OndoZW49IjIwMjAtMDQtMThUMTg6NTE6NTMrMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6NWY4OTM2MmYtNjQwNC05YTRjLThhM2UtOGZjODJhZTljYTczIiBzdEV2dDp3aGVuPSIyMDIwLTA0LTE4VDE5OjE0OjQ2KzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+2teCiwAABfBJREFUWIWtV3tslFUW/9373fmGedfSQoMgtSswHQp2WymlWXkEKYuJgWhAjVY0PhJjNN2srI+4ZlcJxkTdbEzM7hK7aNENq/EVIwF8gPKyljJgraXWdqLY0lJomRk6M9/33Xv2jzI6M52h07rnv3vOuef3uyf3nHsuIyLkK+GjX5YMf/xZ//k9e2EODEKOxsDtOmzFRfDW1eKK+jXl039f35l3QABsIgKx73vEj8+/8KfBt97ZSEZC53Z7gE+bBnAOxhgIAKSEMgyoeLyDpOKF9Tfsnfv0k896qquGfhWBrgcfeXygeWcD1/UAdzoBxsAYy+lPRAARVCIBFb3YWbB61aeLPnz3oUkTiHV3i+DKtQdkJOzWPJ7FjPOJDpKVjBodbSMpRXnzjobpN914MpvfuMjn9+zzt8yv+I6kVSd8vimBAwBjDJrLVaU5nYu/2Xj7W/3bm+qz+qVm4MLBw7OPX7/qC/uVV5ZmA06meJJMACKYZ892LWjafu/MO247mJPA4VmlRxhQy4RIAyXThIxEO0hKoXncYRDllxbGFUhx7nBUgQjG4NnuygOfrPAtq+kbR+DE6nWvRoPBSs3lqkoFl+HwSce8ed1Xb3vmicL6G7omd3yg58mnH+j/x/Z/am4XlGFAc7k+Xfr9t6uTdg4A53bvCYSPHK3jTmcauDU83FF868Zd1a2Hb5kKOAA4SueGSFpjYLoOY2CwJPTstrvSCPQ+/tRzmtfjTy0xFYu1Fdav2bvgX69smwpwUpimqdS18HkDfS+/8vDP69h33XrsVJdfFBf97ERKgQxTX/jOrj9kBoye+LoAnCvGoDJtmeKqWBg999HuddxuTyUEa+SC88xrzStLNjfsF2de35ngTmdagyHLgvu317ZlBvxyfsW+eG+olNvENXkcvhNg0NyuKEshAACa2xXo3950f8nmhv1ioPlNcKcjzYEsiWllV4dSdb1P/eU+46e+WXrJzDHwiUvSD8aydk9msyHa3l4BAIIsC1lrXso0pRWOeLndHhgjaB0lyxJc143cJclAljlm0/WaNBKMAZYlAEDk2+mYxtUlcNjnzDld3XJwYz77uhu3NA4076zRXK4UfAYQMHLg89JJ91kyTbirKo/l6+9a6O8gyxpv4Dwgw9HeqTV6pfLeR6YlchoZkNuYa4/Nhsix49Xx3hAnKTml3gEpudO/IJ5XIKk6NZ93rSClsl7CXMJtNhh9fTe3BK6VmVWgEolQwcrl+ys/23fP5WIQEcCZKrj+dz8I7nBAxWITk1CKAwQiAhMCtqKicS4yEgnbiovTp6BsAwwRmKZZACBKNt/ZcPqlv/9Rc7srL4fPdN0whodCWiyWNcVkSaHPnBld+N83t6TqrZGRAmQcjgwDniXXtQKXXsPP7Z5Tthkz5idrVY7GUHjj2q3lrzf9+XKk8pG22uW74qHQJq7rY+BEsEZG2submxqKNqwPcgDwLF3aQonEr8UaJ8P7Ppl/sb29gtlsvyilguZ0jhZtWB8ELr2GZc9vfUxGIp2pw0nmKzZZiRwPFn57933/Fl5vIJlZIoIVCbdf9cSW55J+HAB8y2r7pm9Y/56MRE8CABMaEj+enj0V4IvfdLi7Gx9tPLFizQEoVZc2XRlGi6OsrGd24yPvJXVpI9mR2WWHyDQFt9trlGG0WOfOFxImMwMyMKFZmsPp505HWmWRUjD6+3tWyNhv0nZkjuWHimYdY0JUJS/N/0PGwM/0LNr9wbrMyWpc8Vd/dXgJKdUqY/HWyXzbcokyTZgDg90V77+9PttYl/Nn1OJftCfxU/8s4fVUME2bNHByoBWFhefL39hxh69uWV82v5ztr6bz67VzHm18UY7GgtaFCyfJsjBRRogIJCVkJBo0zw51zbht067a3lOrcoEDeXxOASD01613n3l1x73m0FAREzY/03UwmwBjHACBLAsqYYBMo5M7XaMzbt/0n3kv/+2FCQPnSyAp8Z4ePnLoiIy2tiEaPAEVT4DpNkybexV8K5bDu3SJz714UTjvgAD+B9Let6xSxlISAAAAAElFTkSuQmCC',
    help: 'https://gitee.com/api/v5/oauth_doc'
  }
]

const customAuthTypeOption = {
  typeName: '自定义OAuth2登录',
  typeValue: 'CUSTOM',
  icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IArs4c6QAAArVJREFUWEfFl01oE0EUx/+z1SRU/LY9KMYPPDQqgprWeskWQb0IFSKo9JDkpkX0UKEHCyLUQ8EeKlK9JTkUFQxY8KKCdHOxNlFB1HgQP1LsofVbLEm0OzJrNmY3m8wkLuwcd9/7v9/Mm/dmhsDhQRyOj7oAXMlIN1HVg4QQvwpsJsBqNgEKfJKAN5TSNJWku4VAdFx0YkIAbiXUB5AzANYLCk8DdCQvx4d59jUBFj+I7GmS1CuUED9PyOo/oTS9oEqnfu2LPqrmXxXApUSOEtAbjQQ2+1CQYwU5etMS0uqjncF1/WoQFSvAll1qopN2zNysoS6QTnM6KgA8E6GUSM5vbTuN3Us3aTEe/3iLIy8uc5nZnsh1xdvLDQ0Axd1+iaeUC0QtTTzJCM+VFe3Z8uowAYSzvFIb8/Ui2NKOwffjSMxNaQGDLR0Y2NCNxFwKPZlRHsR0Xo55daMSgNZkKL3N8852jqDVtQwHng0h+fWVZh5Y0YZ7O/oxW/gO7yRrF7UHJeSw3qxKAO6J0CgIOclzfuIfxNbmddiZPofM/Ixm7mtei6f+i3g5/wG70gM8CYDSq/mueC8zLAF4lPAUBQwbxErpwsYg+r2HtBQkvxVXYHmbloKh7B2cf5fgAhAglZNjHQYAlxL+qPf2Wgr7V25H3HcCqxYtMZh9/v0Tocw13P/ynAvAzo6CHFtjAHArYcr1LBqw4MNbenC8da/25frsQ/S9HgODEB15Oaat/r89UAeAHkQvR7HyM6JVAIimoFymUQDLFIhuQjsALDehaBnaAWBZhqKNyAqAfWNNiTUnkWHZiJijW+G3YhsArFvxXwDt6sU9jP6vCmocRkxY9DhmtvVWAfc4ZqKOX0gYhKNXMj2/dkLUfSnVIRy9lpeXm2MPE3NTcexpJtLdGrURehs2Ki7i9wdHwT8wAKY+CAAAAABJRU5ErkJggg=='
}

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
        configId: undefined,

        type: null,

        name: null,

        icon: null,

        help: null,

        clientId: null,

        clientSecret: null,

        redirectUri: null,

        authRequestClass: null,

        status: '0',

        remark: null
      },
      // 1增加,2修改
      formType: 1,
      open: false,
      rules: {
        type: [{ required: true, message: '登录平台不能为空', trigger: 'blur' }],

        name: [{ required: true, message: '登录名称不能为空', trigger: 'blur' }],

        clientId: [{ required: true, message: '客户端Id不能为空', trigger: 'blur' }],

        clientSecret: [{ required: true, message: '客户端秘钥不能为空', trigger: 'blur' }],

        redirectUri: [{ required: true, message: '回调地址不能为空', trigger: 'blur' }]
      },
      defaultAuthTypeOptions: defaultAuthTypeOptions,
      iconLoading: false
    }
  },
  filters: {},
  created() {},
  computed: {
    realRules() {
      if (this.isDefaultAuthType(this.form.type)) {
        return this.rules
      } else {
        return {
          ...this.rules,
          authRequestClass: [{ required: true, message: '授权请求类不能为空', trigger: 'blur' }]
        }
      }
    },
    authTypeOptions() {
      return [...defaultAuthTypeOptions, customAuthTypeOption]
    },
    authType() {
      if (this.isDefaultAuthType(this.form.type)) {
        return this.form.type
      }

      return 'CUSTOM'
    },
    authIcons() {
      const icons = []
      icons.push({ url: this.form.icon, name: 'unique_name', uid: 'unique_uid' })
      return icons
    }
  },
  watch: {},
  mounted() {},
  methods: {
    isDefaultAuthType(type) {
      return defaultAuthTypeOptions.some(value => {
        return value.typeValue === type
      })
    },
    handleAuthTypeChange(value) {
      const authTypeItem = defaultAuthTypeOptions.find(item => item.typeValue === value)
      if (authTypeItem) {
        this.form.type = authTypeItem.typeValue
        this.form.name = authTypeItem.typeName
        this.form.icon = authTypeItem.icon
        this.form.help = authTypeItem.help
      } else {
        this.form.type = ''
        this.form.name = ''
        this.form.icon = customAuthTypeOption.icon
        this.form.help = ''
      }
    },
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
        configId: undefined,

        type: null,

        name: null,

        icon: null,

        help: null,

        clientId: null,

        clientSecret: null,

        redirectUri: null,

        authRequestClass: null,

        status: '0',

        remark: null
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      this.formType = 1
      this.open = true
      this.formTitle = '添加'
      this.form.type = defaultAuthTypeOptions[0].typeValue
      this.form.name = defaultAuthTypeOptions[0].typeName
      this.form.icon = defaultAuthTypeOptions[0].icon
      this.form.help = defaultAuthTypeOptions[0].help
    },
    /** 修改按钮操作 */
    handleUpdate(row, ids) {
      this.reset()
      this.formType = 2
      const configId = row ? row.configId : ids
      getAuthConfig(configId).then(response => {
        this.form = response.data
        this.open = true
        this.formTitle = '修改'
      })
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.configId !== undefined && this.form.configId !== null) {
            updateAuthConfig(this.form)
              .then(response => {
                this.$message.success('修改成功', 3)
                this.open = false
                this.$emit('ok')
              })
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            addAuthConfig(this.form)
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
    handleFileChange(info) {
      if (info.file.status === 'uploading') {
        this.iconLoading = true
      }
    },
    customRequest(data) {
      this.form.icon = []
      this.getBase64(data.file)
        .then(res => {
          this.form.icon = res
          this.iconLoading = false
        })
        .catch(() => {
          this.iconLoading = false
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
    }
  }
}
</script>

<style lang="less" scoped>
/deep/ .ant-upload-select-picture-card {
  width: 32px;
  height: 32px;
  i {
    font-size: 10px;
  }
  .ant-upload {
    padding: 2px;
  }
}
</style>
