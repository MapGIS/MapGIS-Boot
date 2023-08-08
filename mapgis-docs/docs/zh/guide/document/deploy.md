# 部署运行

## 传统部署

### 单体版

#### 安装

将与服务器架构一致的单体版包放到服务器的某一目录。

#### 调整参数

复制 config/application.example.properties 文件并命名为 application.properties，修改里面的服务端口和数据源等信息。

> 如果某项配置为中文，可额外通过application.yml文件进行补充配置

#### 启动运行

```shell
# win-x86_64启动
bin/startup.bat
# linx-x86_64启动
bin/startup.sh
# linx-x86_64停止
bin/shutdown.sh
```

#### 访问应用

[http://{HOST_IP}:{APP_PORT}](http://{HOST_IP}:{APP_PORT})

{HOST_IP}为应用所在主机的IP，{APP_PORT}为应用端口

### 微服务版

> 因微服务版涉及到服务较多，不推荐采用传统方式进行部署，建议采用容器部署方式（Docker Compose或Kubernetes）

#### 安装前提

- redis【必须】
- mysql【必须】
- nacos【必须】
- sentinel【可选】

#### 安装

将与服务器架构一致的微服务版包放到服务器的某一目录，并配置微服务版应用所在主机本地hosts：

```shell
127.0.0.1 mapgis-xxx-mysql
127.0.0.1 mapgis-xxx-redis
127.0.0.1 mapgis-xxx-nacos
127.0.0.1 mapgis-xxx-sentinel
127.0.0.1 mapgis-xxx-gateway
```

#### 调整参数

微服务版配置在配置中心nacos中，需要进入到nacos中进行调整，比如：redis密码、mysql数据源信息等

#### 启动运行

```shell
# win-x86_64启动
bin/startup.bat
# linx-x86_64启动
bin/startup.sh
# linx-x86_64停止
bin/shutdown.sh
```

#### 访问应用

[http://{HOST_IP}:{GATEWAY_PUBLIC_PORT}](http://{HOST_IP}:{GATEWAY_PUBLIC_PORT})

{HOST_IP}为应用所在主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口

## Docker Compose部署

### 安装Docker和Docker Compose

安装Docker

```shell
# 使用daocloud 的一键安装脚本
curl -sSL https://get.daocloud.io/docker | sh

# 安装 daocloud 加速器，避免dockerhub 网络问题
curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s http://f1361db2.m.daocloud.io

# 启动docker服务
systemctl start docker

# 测试运行 hello-world,由于本地没有hello-world这个镜像，所以会下载一个hello-world的镜像，并在容器内运行。
docker run hello-world

# 查看Docker版本
docker --version
```

安装Docker Compose
```shell
curl -L https://get.daocloud.io/docker/compose/releases/download/1.29.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```

### 配置私有镜像仓库

目前MapGIS Boot已上传到私有镜像仓库192.168.177.1:5000，需要配置Docker
```shell
# 编辑
vi /etc/docker/daemon.json

# 添加私有仓库地址
{
  "registry-mirrors": ["加速地址"],
  "insecure-registries": ["192.168.177.1:5000"]
}

# 生效
systemctl daemon-reload
systemctl restart docker
```
### 单体版
#### 安装

将Docker版的包放到linux服务器的某一目录，如：`/opt`，进入应用根目录，执行以下命令，进行安装：

```shell
sudo chmod +x install.sh && sudo ./install.sh
```

#### 调整参数

编辑`.env`，根据需要修改相关参数，运行`sudo vi .env`

```properties
# 私有镜像仓库地址，注意结尾需含/，默认为空
DOCKER_REGISTRY_URL=

# 镜像标签
MAPGIS_IMAGE_TAG=10.6.4.10

# 应用端口
MAPGIS_APP_PORT=8080
```

#### 启动运行
```shell
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

#### 访问应用
[http://{DOCKER_HOST_IP}:{MAPGIS_APP_PORT}](http://{DOCKER_HOST_IP}:{MAPGIS_APP_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{MAPGIS_APP_PORT}为应用端口


### 微服务版
#### 安装

将Docker版的包放到linux服务器的某一目录，如：`/opt`，进入应用根目录，执行以下命令，进行安装：

```shell
sudo chmod +x install.sh && sudo ./install.sh
```

#### 调整参数

编辑`.env`，根据需要修改相关参数，运行`sudo vi .env`

```properties
# 私有镜像仓库地址，注意结尾需含/，默认为空
DOCKER_REGISTRY_URL=

# 镜像标签
MAPGIS_IMAGE_TAG=10.6.4.10

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

#### 启动运行
```shell
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

#### 访问应用
[http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT}](http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口

## Kubernetes部署

### 安装前提

- Kubernetes 1.9及以上

- 所需硬件资源

  - CPU：4核

  - 内存：16GB+

  - 硬盘：200GB+
  
- NFS Server

- 私有镜像仓库（如果可以访问阿里云镜像仓库，则不需要搭建）

  > 搭建时可参考[配置私有镜像仓库](/zh/guide/document/deploy.html#配置私有镜像仓库)

### 单体版

#### 调整参数

编辑`values.yaml`，根据需要修改相关参数，运行`sudo vi values.yaml`

```shell
# 选填，应用端口，自定义范围30000-32767，默认为32000
appPort: 32000

# 必填，镜像仓库地址，默认为阿里云镜像仓库（外网推荐使用）：registry.cn-beijing.aliyuncs.com
# 内网私有镜像仓库：<ip>:5000，<ip>为镜像仓库所在的机器IP，例如：192.168.177.1:5000
imageRegistry: 

# 必填，NFS Server的地址，用于持久化存储应用数据，例如：192.168.130.10
nfsServer: 

# 必填，NFS Server提供的挂载路径，默认是根路径: /，请根据实际情况填写，例如：/nfs/data
nfsPath: /

# 选填，用于存储应用数据，它跟NFS Server都用于存储数据，如果和NFS Server同时存在，优先使用NFS Server存储应用数据
nfsStorageClassName: 
```

#### 启动运行

```shell
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

#### 访问应用

[http://{ip}:{appPort}}](http://{ip}:{appPort})

{ip}为外部访问k8s的IP，{appPort}为应用端口

### 微服务版

#### 调整参数

编辑`values.yaml`，根据需要修改相关参数，运行`sudo vi values.yaml`

```shell
# 必填，外部访问k8s的IP，可以是k8s集群内任意一台外部可访问的机器IP，如：192.168.130.10
k8sPublicIp: 

# 选填，应用端口，自定义范围30000-32767，默认为32000
appPort: 32000

# 必填，镜像仓库地址，默认为阿里云镜像仓库（外网推荐使用）：registry.cn-beijing.aliyuncs.com
# 内网私有镜像仓库：<ip>:5000，<ip>为镜像仓库所在的机器IP，例如：192.168.177.1:5000
imageRegistry: 

# 必填，NFS Server的地址，用于持久化存储应用数据，例如：192.168.130.10
nfsServer: 

# 必填，NFS Server提供的挂载路径，默认是根路径: /，请根据实际情况填写，例如：/nfs/data
nfsPath: /

# 选填，用于存储应用数据，它跟NFS Server都用于存储数据，如果和NFS Server同时存在，优先使用NFS Server存储应用数据
nfsStorageClassName: 
```

#### 启动运行

```shell
# 启动
sudo ./startup.sh
# 停止
sudo ./shutdown.sh
```

#### 访问应用

[http://{ip}:{appPort}}](http://{ip}:{appPort})

{ip}为外部访问k8s的IP，{appPort}为应用端口