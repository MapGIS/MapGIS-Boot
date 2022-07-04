# 部署运行

## Docker Compose

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
提供部署脚本[docker-compose.yml](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-local-packer/docker/linux-x86_64/docker-compose.yml)和环境变量设置脚本[.env](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-local-packer/docker/linux-x86_64/.env)，将他们上传到服务器同一目录下

#### 调整`.env`环境变量
- 如果需要从指定私有仓库或者公网仓库获取镜像，则必须指定DOCKER_REGISTRY_URL，比如`192.168.177.1:5000/`
```properties
# 私有镜像仓库地址，注意结尾需含/，默认为空
DOCKER_REGISTRY_URL=

# 应用端口
MAPGIS_PORT=8080
```

#### 启动运行
```shell
# 进入到存放docker-compose.yml目录并执行赋予执行权限
chmod +x docker-compose.yml
# 创建并启动
docker-compose up -d
# 查看运行中的容器
docker ps
# 停止并删除
docker-compose down
```

#### 应用访问
[http://{DOCKER_HOST_IP}:{MAPGIS_PORT}](http://{DOCKER_HOST_IP}:{MAPGIS_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{MAPGIS_PORT}为应用端口


### 微服务版
提供部署脚本[docker-compose.yml](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-cloud-packer/docker/linux-x86_64/docker-compose.yml)和环境变量设置脚本[.env](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-cloud-packer/docker/linux-x86_64/.env)，将他们上传到服务器同一目录下

#### 调整`.env`环境变量
- 如果需要从指定私有仓库或者公网仓库获取镜像，则必须指定DOCKER_REGISTRY_URL，比如`192.168.177.1:5000/`
- GATEWAY_PUBLIC_HOST建议填写为Docker宿主机IP

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

#### 启动运行
```shell
# 进入到存放docker-compose.yml目录并执行赋予执行权限
chmod +x docker-compose.yml
# 创建并启动
docker-compose up -d
# 查看运行中的容器
docker ps
# 停止并删除
docker-compose down
```

#### 应用访问
[http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT}](http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT})

{DOCKER_HOST_IP}为Docker宿主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口