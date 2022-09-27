const { getNavbarByCategory } = require('./navbar')
const { getSidebarByCategory } = require('./sidebar')

module.exports = {
  port: '60000',
  title: 'MapGIS Boot',
  base: '/mapgis-boot-docs/',
  head: [
    [
      'meta',
      { name: 'keywords', content: 'mapgis,开源,免费,mapgis boot,MapGIS-Boot' }
    ],
    ['link', { rel: 'icon', href: '/favicon.ico' }]
  ],
  themeConfig: {
    logo: '/logo.png',
    repo: 'mapgis/mapgis-boot',
    docsRepo: 'mapgis/mapgis-boot',
    docsDir: 'docs',
    editLinks: true,
    sidebarDepth: 3,
    lastUpdated: 'Last Updated',
    serviceWorker: {
      updatePopup: true
    },
    locales: {
      '/': {
        selectText: 'Languages',
        label: 'English',
        editLinkText: 'Edit this page on GitHub',
        serviceWorker: {
          updatePopup: {
            message: 'New content is available.',
            buttonText: 'Refresh'
          }
        },
        nav: [],
        sidebar: {}
      },
      '/zh/': {
        // 多语言下拉菜单的标题
        selectText: '选择语言',
        // 该语言在下拉菜单中的标签
        label: '简体中文',
        // 编辑链接文字
        editLinkText: '在 GitHub 上编辑此页',
        // Service Worker 的配置
        serviceWorker: {
          updatePopup: {
            message: '发现新内容可用.',
            buttonText: '刷新'
          }
        },
        nav: [
          {
            text: '指南',
            link: '/zh/guide/document/introduction'
          }
        ],
        sidebar: {
          '/zh/guide/': [
            {
              title: '文档',
              children: getSidebarByCategory('document', 'zh')
            },
            {
              title: '其它',
              children: getSidebarByCategory('other', 'zh')
            }
          ]
        }
      }
    }
  },
  locales: {
    '/': {
      lang: 'en-US',
      description:
        'Extreme Speed Background Development Framework for Separating Front-End and Back-End Microservices Based on Spring Boot, Spring Cloud & Alibaba'
    },
    '/zh/': {
      lang: 'zh-CN',
      description:
        '基于Spring Boot、Spring Cloud & Alibaba的前后端分离微服务极速后台开发框架'
    }
  },
  plugins: [
    '@vuepress/plugin-medium-zoom',
    '@vuepress/plugin-back-to-top',
    'vuepress-plugin-viewer',
    require('./plugins/alert')
  ]
}
