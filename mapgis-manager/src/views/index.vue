<template>
  <page-header-wrapper>
    <div>
      <a-row :gutter="[24, 24]">
        <a-col :xl="16" :lg="24" :md="24" :sm="24" :xs="24">
          <!-- 项目简介 -->
          <a-card title="MapGIS Boot 简介">
            <p>
              <b>
                <a href="https://github.com/MapGIS/MapGIS-Boot" target="_blank">
                  基于Spring Boot、Spring Cloud & Alibaba的前后端分离微服务极速后台开发框架
                </a>
              </b>
            </p>
            <p>
              MapGIS Boot 是一个 Java EE 企业级快速开发平台，基于经典技术组合（Spring Boot、Spring Cloud & Alibaba、Ant
              Design
              Vue），内置模块如：部门管理、角色用户、菜单及按钮授权、数据权限、系统参数、日志管理、代码生成、在线定时任务、路由监控等。
            </p>
          </a-card>
        </a-col>
        <a-col :xl="8" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card title="更新日志">
            <a-timeline>
              <a-timeline-item v-for="(value, index) in updateLogs" :key="index">
                {{ value.time }} {{ value.description }}
              </a-timeline-item>
            </a-timeline>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </page-header-wrapper>
</template>

<script>
import { PageHeaderWrapper } from '@/components/ProLayout'

export default {
  name: 'Index',
  components: {
    PageHeaderWrapper
  },
  data() {
    return {
      updateLogs: [
        {
          time: '2023年10月',
          description: 'MapGIS Boot v10.6.6.10 发布'
        },
        {
          time: '2023年7月',
          description: 'MapGIS Boot v10.6.4.10 发布'
        },
        {
          time: '2023年1月',
          description: 'MapGIS Boot v10.6.2.10 发布'
        },
        {
          time: '2022年9月',
          description: 'MapGIS Boot v10.6.0.10 开源'
        },
        {
          time: '2022年7月',
          description: 'MapGIS Boot v10.6.0.10 发布'
        },
        {
          time: '2022年5月',
          description: 'MapGIS Boot 正式发布'
        }
      ]
    }
  }
}
</script>

<style lang="less" scoped></style>
