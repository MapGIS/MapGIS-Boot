<h1 align="center">服务器产品打包器</h1>

## 开始

打包之前需先添加支持环境，请执行`add-support.bat`会自动添加`\\192.168.17.59\03-Support`环境到 release 目录内。

> 执行前，请先确保该目录能访问，账号为 support/support

```bash
add-support.bat
```

## 打包

```bash
package/package.bat
```

## 运行（临时）

```bash
# win-x86_64，其他类似，需要先启动Nacos、MySQL、Redis等基础服务
release/win-x86_64/bin/startup.bat
```

## 运行（正式）

> 将与服务器架构一致的 release/xxx 下的目录发布到该服务器下，需要先启动Nacos、MySQL、Redis等基础服务，然后运行启动脚本即可。

## Docker打包

将与Linux服务器架构一致的产品包和Docker脚本拷贝到具备联网环境的该服务器下
> 比如linux-x86_64的产品包需要到x86_64架构的Linux环境下进行镜像制作（可通过uname -m查询架构）

形成如下的目录结构，确保docker、docker-package和release在同一目录下：

```text
├─docker
│  ├─linux-x86_64
├─docker-package
├─release
│  ├─linux-x86_64
```

```bash
# 进入到docker-package目录
cd docker-package

# 执行打包(根据服务器架构选择相应的打包脚本，比如当前是x86_64就选择package-linux-x86_64.sh脚本)
sh package-linux-x86_64.sh
```

## Docker Compose 运行（正式）
> 将与服务器架构一致的 docker-release/xxx 下的目录发布到该服务器下

### 安装
进入应用根目录
执行以下命令，进行安装
```bash
sudo chmod +x install.sh && sudo ./install.sh
```

### 调整参数【必须】
编辑.env，根据需要修改相关参数，运行`sudo vi .env`
```properties
# 私有镜像仓库地址，注意结尾需含/，默认为空
DOCKER_REGISTRY_URL=

# NACOS主机(用于微服务之间通信，一般不需要修改)
NACOS_HOST=mapgis-xxx-nacos

# NACOS端口(内部端口，用于微服务之间通信，一般不需要修改)
NACOS_PORT=8848

# 网关主机(用于微服务之间通信，一般不需要修改)
GATEWAY_HOST=mapgis-xxx-gateway

# 网关主机(外部主机，用于Swagger在线文档，需要外部能访问，如不能访问，请设置成Docker宿主机IP)
GATEWAY_PUBLIC_HOST=

# 网关端口(外部端口，用于应用入口和Swagger在线文档)
GATEWAY_PUBLIC_PORT=8080

# Redis主机(用于微服务之间通信，一般不需要修改)
REDIS_HOST=mapgis-xxx-redis

# Redis端口(内部端口，用于微服务之间通信，一般不需要修改)
REDIS_PORT=6379

# Redis密码(可修改)
REDIS_PWD=cloud123.mapgis

# MySQL主机(用于微服务之间通信，一般不需要修改)
MYSQL_HOST=mapgis-xxx-mysql

# MySQL端口(内部端口，用于微服务之间通信，一般不需要修改)
MYSQL_PORT=3306

# MySQL数据库名(默认由容器创建，一般不需要修改)
MYSQL_DB=mapgis-cloud-xxx

# MySQL配置中心数据库名(由配置中心sql脚本自动创建，一般不需要修改)
MYSQL_CONFIG_DB=mapgis-cloud-config

# MySQL用户名
MYSQL_USER=root

# MySQL密码(可修改)
MYSQL_PWD=cloud123.mapgis

# MONITOR端口(外部端口，用于Spring Boot应用监控，需要外部能访问)
MONITOR_PUBLIC_PORT=9200
```

### 启动/停止
```bash
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

### 访问应用
[http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT}](http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口