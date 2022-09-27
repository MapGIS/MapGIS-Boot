(window.webpackJsonp=window.webpackJsonp||[]).push([[14],{372:function(a,t,s){"use strict";s.r(t);var e=s(11),n=Object(e.a)({},(function(){var a=this,t=a.$createElement,s=a._self._c||t;return s("ContentSlotsDistributor",{attrs:{"slot-key":a.$parent.slotKey}},[s("h1",{attrs:{id:"实战技巧"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#实战技巧"}},[a._v("#")]),a._v(" 实战技巧")]),a._v(" "),s("h2",{attrs:{id:"项目扩展"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#项目扩展"}},[a._v("#")]),a._v(" 项目扩展")]),a._v(" "),s("h3",{attrs:{id:"如何扩展"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#如何扩展"}},[a._v("#")]),a._v(" 如何扩展")]),a._v(" "),s("h4",{attrs:{id:"克隆本项目-并移除mapgis-docs目录"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#克隆本项目-并移除mapgis-docs目录"}},[a._v("#")]),a._v(" 克隆本项目，并移除"),s("code",[a._v("mapgis-docs")]),a._v("目录")]),a._v(" "),s("blockquote",[s("p",[s("code",[a._v("mapgis-docs")]),a._v("为"),s("code",[a._v("mapgis-boot")]),a._v("的开发手册，用于扩展项目参考，建议在扩展项目中移除")])]),a._v(" "),s("h4",{attrs:{id:"修改产品标识"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#修改产品标识"}},[a._v("#")]),a._v(" 修改产品标识")]),a._v(" "),s("p",[a._v("全项目搜索 "),s("code",[a._v("xxx")]),a._v("，替换成产品小写标识，如："),s("code",[a._v("igs、datastore、igs-x、igs-s、workspace、psmap、portal、manager")])]),a._v(" "),s("h4",{attrs:{id:"推送到新的项目"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#推送到新的项目"}},[a._v("#")]),a._v(" 推送到新的项目")]),a._v(" "),s("p",[a._v("推送现有的Git仓库到一个新的项目仓库")]),a._v(" "),s("div",{staticClass:"language- extra-class"},[s("pre",{pre:!0,attrs:{class:"language-text"}},[s("code",[a._v("cd existing_repo\ngit remote rename origin old-origin\ngit remote add origin git@github.com:xyz/xyz.git\ngit push -u origin --all\ngit push -u origin --tags\n")])])]),s("h3",{attrs:{id:"如何升级"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#如何升级"}},[a._v("#")]),a._v(" 如何升级")]),a._v(" "),s("p",[s("code",[a._v("MapGIS Boot")]),a._v("属于平台级产品，每次升级改动内容较多，目前做不到平滑升级，这里给用户的升级建议是这样的：")]),a._v(" "),s("ul",[s("li",[a._v("通过cherry-pick进行仓库之间的同步，参考："),s("a",{attrs:{href:"https://www.ruanyifeng.com/blog/2020/04/git-cherry-pick.html",target:"_blank",rel:"noopener noreferrer"}},[a._v("五、转移到另一个代码库"),s("OutboundLink")],1),a._v(" "),s("blockquote",[s("p",[a._v("对于sql脚本、docker脚本的同步需要仔细对比代码")])])])]),a._v(" "),s("div",{staticClass:"language-shell extra-class"},[s("pre",{pre:!0,attrs:{class:"language-shell"}},[s("code",[s("span",{pre:!0,attrs:{class:"token function"}},[a._v("git")]),a._v(" remote "),s("span",{pre:!0,attrs:{class:"token function"}},[a._v("add")]),a._v(" boot git@github.com:MapGIS/MapGIS-Boot.git\n"),s("span",{pre:!0,attrs:{class:"token function"}},[a._v("git")]),a._v(" fetch boot\n"),s("span",{pre:!0,attrs:{class:"token function"}},[a._v("git")]),a._v(" log boot/master\n"),s("span",{pre:!0,attrs:{class:"token function"}},[a._v("git")]),a._v(" cherry-pick "),s("span",{pre:!0,attrs:{class:"token operator"}},[a._v("<")]),a._v("commitHash"),s("span",{pre:!0,attrs:{class:"token operator"}},[a._v(">")]),a._v("\n")])])]),s("h2",{attrs:{id:"新建子模块"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#新建子模块"}},[a._v("#")]),a._v(" 新建子模块")]),a._v(" "),s("h3",{attrs:{id:"新建单体和微服务共用模块"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#新建单体和微服务共用模块"}},[a._v("#")]),a._v(" 新建单体和微服务共用模块")]),a._v(" "),s("p",[a._v("建议项目结构如下：")]),a._v(" "),s("div",{staticClass:"language-text extra-class"},[s("pre",{pre:!0,attrs:{class:"language-text"}},[s("code",[a._v("├── mapgis-module-xxx                                // 模块xxx\n│   ├── mapgis-module-xxx-api                        // 模块API\n│       ├── mapgis-module-xxx-base-api               // 基础API\n│       ├── mapgis-module-xxx-cloud-api              // 微服务API\n│       ├── mapgis-module-xxx-local-api              // 单体API\n│   ├── mapgis-module-xxx-biz                        // 模块业务\n│   ├── mapgis-module-xxx-server                     // 模块服务器\n")])])]),s("p",[a._v("在 mapgis-boot 的根 "),s("code",[a._v("pom.xml")]),a._v(" 中针对 local 和 cloud 两个 profile 编写\n"),s("code",[a._v("mapgis.module.xxx.api.artifact")])]),a._v(" "),s("div",{staticClass:"language-xml extra-class"},[s("pre",{pre:!0,attrs:{class:"language-xml"}},[s("code",[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("profiles")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token comment"}},[a._v("\x3c!-- 单体模式 --\x3e")]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("profile")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("id")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("local"),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("id")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("properties")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n      "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("mapgis.module.xxx.api.artifact")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("mapgis-module-xxx-local-api"),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("mapgis.module.xxx.api.artifact")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("properties")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("profile")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token comment"}},[a._v("\x3c!-- 微服务模式 --\x3e")]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("profile")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("id")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("local"),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("id")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("properties")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n      "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("<")]),a._v("mapgis.module.xxx.api.artifact")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("mapgis-module-xxx-cloud-api"),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("mapgis.module.xxx.api.artifact")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n    "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("properties")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n  "),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("profile")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n"),s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token tag"}},[s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v("</")]),a._v("profiles")]),s("span",{pre:!0,attrs:{class:"token punctuation"}},[a._v(">")])]),a._v("\n")])])]),s("p",[a._v("模块之间的依赖关系如下：")]),a._v(" "),s("ul",[s("li",[s("p",[s("code",[a._v("mapgis-module-xxx-local-api")]),a._v(" 和 "),s("code",[a._v("mapgis-module-xxx-cloud-api")]),a._v(" 依赖 "),s("code",[a._v("mapgis-module-xxx-base-api")])])]),a._v(" "),s("li",[s("p",[s("code",[a._v("mapgis-module-xxx-biz")]),a._v("依赖"),s("code",[a._v("${mapgis.module.xxx.api.artifact}")])])])]),a._v(" "),s("div",{staticClass:"language- extra-class"},[s("pre",{pre:!0,attrs:{class:"language-text"}},[s("code",[a._v("<dependency>\n    <groupId>com.zondy.mapgis</groupId>\n    <artifactId>${mapgis.module.xxx.api.artifact}</artifactId>\n</dependency>\n")])])]),s("ul",[s("li",[s("p",[s("code",[a._v("mapgis-module-xxx-server")]),a._v("依赖"),s("code",[a._v("mapgis-module-xxx-biz")])])]),a._v(" "),s("li",[s("p",[s("code",[a._v("mapgis-server")]),a._v("依赖"),s("code",[a._v("mapgis-module-xxx-biz")])])])]),a._v(" "),s("blockquote",[s("p",[s("code",[a._v("mapgis-module-xxx-server")]),a._v("为微服务版时需要独立运行的服务，在单体版下运行的是"),s("code",[a._v("mapgis-server")])])]),a._v(" "),s("h3",{attrs:{id:"新建微服务特有模块"}},[s("a",{staticClass:"header-anchor",attrs:{href:"#新建微服务特有模块"}},[a._v("#")]),a._v(" 新建微服务特有模块")]),a._v(" "),s("p",[a._v("可直接建立在 mapgis-cloud-module 模块下，建议模块名如下：")]),a._v(" "),s("div",{staticClass:"language- extra-class"},[s("pre",{pre:!0,attrs:{class:"language-text"}},[s("code",[a._v("mapgis-cloud-module-xxx\n")])])])])}),[],!1,null,null,null);t.default=n.exports}}]);