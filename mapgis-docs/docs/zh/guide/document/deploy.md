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
提供私有私有镜像仓库的脚本，见[docker-compose-server.yml](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-local-packer/docker/linux-x86_64/docker-compose-server.yml)，可基于脚本一键启动MapGIS Boot单体版应用，启动成功后访问：[http://docker-host-ip:8080/xxx/manager](http://docker-host-ip:8080/xxx/manager)

```shell
# 进入到存放docker-compose-server.yml目录并执行赋予执行权限
chmod +x docker-compose-server.yml
# 创建并启动
docker-compose -f docker-compose-server.yml up -d
# 查看运行中的容器
docker ps
# 停止并删除
docker-compose -f docker-compose-server.yml down
```

完整的脚本如下：
```yml
#### 镜像上传
# 仓库私服： 192.168.177.1:5000
# 第一步：上传镜像到docker仓库
#docker tag mapgis-server 192.168.177.1:5000/mapgis-server:1.0

#docker push 192.168.177.1:5000/mapgis-server:1.0

# 第二步：将此yml文件上传服务器，执行启动命令 docker-compose -f ./docker-compose-server.yml up
version: '3'
services:
  mapgis-server:
    image: 192.168.177.1:5000/mapgis-server:1.0
    restart: on-failure
    ports:
      - "8080:8080"
    volumes:
      - ./mapgis/server/logs:/mapgis/logs
      - ./mapgis/server/upload:/mapgis/upload
      - ./mapgis/server/data:/mapgis/data
```

### 微服务版
提供私有私有镜像仓库的脚本，见[docker-compose-server.yml](http://192.168.200.88/webgis/server/mapgis-boot/blob/master/mapgis-cloud-packer/docker/linux-x86_64/docker-compose-server.yml)，可基于脚本一键启动MapGIS Boot微服务版应用，启动成功（因微服务版运行容器较多，大概需等待3分钟，取决于机器性能）后访问：[http://docker-host-ip:8080/xxx/manager](http://docker-host-ip:8080/xxx/manager)

```shell
# 进入到存放docker-compose-server.yml目录并执行赋予执行权限
chmod +x docker-compose-server.yml
# 创建并启动
docker-compose -f docker-compose-server.yml up -d
# 查看运行中的容器
docker ps
# 停止并删除
docker-compose -f docker-compose-server.yml down
```

完整的脚本如下：
```yml
#### 镜像上传
# 仓库私服： 192.168.177.1:5000
# 第一步：上传镜像到docker仓库
#docker tag nacos/nacos-server 192.168.177.1:5000/nacos/nacos-server
#docker tag redis 192.168.177.1:5000/redis
#docker tag mapgis-mysql 192.168.177.1:5000/mapgis-mysql:1.0
#docker tag mapgis-gateway 192.168.177.1:5000/mapgis-gateway:1.0
#docker tag mapgis-monitor 192.168.177.1:5000/mapgis-monitor:1.0
#docker tag mapgis-auth 192.168.177.1:5000/mapgis-auth:1.0
#docker tag mapgis-system 192.168.177.1:5000/mapgis-system:1.0
#docker tag mapgis-file 192.168.177.1:5000/mapgis-file:1.0
#docker tag mapgis-job 192.168.177.1:5000/mapgis-job:1.0

#docker push 192.168.177.1:5000/nacos/nacos-server
#docker push 192.168.177.1:5000/redis
#docker push 192.168.177.1:5000/mapgis-mysql:1.0
#docker push 192.168.177.1:5000/mapgis-gateway:1.0
#docker push 192.168.177.1:5000/mapgis-monitor:1.0
#docker push 192.168.177.1:5000/mapgis-auth:1.0
#docker push 192.168.177.1:5000/mapgis-system:1.0
#docker push 192.168.177.1:5000/mapgis-file:1.0
#docker push 192.168.177.1:5000/mapgis-job:1.0

# 第二步：将此yml文件上传服务器，执行启动命令 docker-compose -f ./docker-compose-server.yml up
version : '3'
services:
  mapgis-nacos:
    container_name: mapgis-nacos
    image: 192.168.177.1:5000/nacos/nacos-server
    environment:
      MODE: standalone
      SPRING_DATASOURCE_PLATFORM: mysql
      MYSQL_SERVICE_HOST: mapgis-mysql
      MYSQL_SERVICE_PORT: 3306
      MYSQL_SERVICE_DB_NAME: mapgis-cloud-config
      MYSQL_SERVICE_USER: root
      MYSQL_SERVICE_PASSWORD: cloud123.mapgis
    volumes:
      - ./nacos/logs:/home/nacos/logs
    ports:
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    depends_on:
      - mapgis-mysql
  mapgis-mysql:
    container_name: mapgis-mysql
    image: 192.168.177.1:5000/mapgis-mysql:1.0
    ports:
      - "3306:3306"
    volumes:
      - ./mysql/conf:/etc/mysql/conf.d
      - ./mysql/logs:/logs
      - ./mysql/data:/var/lib/mysql
    command: [
          'mysqld',
          '--innodb-buffer-pool-size=80M',
          '--character-set-server=utf8mb4',
          '--collation-server=utf8mb4_unicode_ci',
          '--default-time-zone=+8:00',
          '--lower-case-table-names=1'
        ]
    environment:
      MYSQL_DATABASE: 'mapgis-cloud-xxx'
      MYSQL_ROOT_PASSWORD: cloud123.mapgis
  mapgis-redis:
    container_name: mapgis-redis
    image: 192.168.177.1:5000/redis
    ports:
      - "6379:6379"
    volumes:
      - ./redis/data:/data
    command: redis-server --requirepass cloud123.mapgis
  mapgis-gateway:
    container_name: mapgis-gateway
    image: 192.168.177.1:5000/mapgis-gateway:1.0
    restart: on-failure
    ports:
      - "8080:8080"
    volumes:
      - ./mapgis/gateway/logs:/mapgis/logs
    depends_on:
      - mapgis-redis
      - mapgis-nacos
  mapgis-monitor:
    image: 192.168.177.1:5000/mapgis-monitor:1.0
    restart: on-failure
    ports:
      - "9200:9200"
    volumes:
      - ./mapgis/monitor/logs:/mapgis/logs
    depends_on:
      - mapgis-nacos
  mapgis-auth:
    image: 192.168.177.1:5000/mapgis-auth:1.0
    restart: on-failure
    ports:
      - "10000:10000"
    volumes:
      - ./mapgis/auth/logs:/mapgis/logs
    depends_on:
      - mapgis-redis
      - mapgis-nacos
  mapgis-system:
    image: 192.168.177.1:5000/mapgis-system:1.0
    restart: on-failure
    ports:
      - "11000:11000"
    volumes:
      - ./mapgis/system/logs:/mapgis/logs
    depends_on:
      - mapgis-redis
      - mapgis-mysql
      - mapgis-nacos
  mapgis-file:
    image: 192.168.177.1:5000/mapgis-file:1.0
    restart: on-failure
    ports:
      - "12000:12000"
    volumes:
      - ./mapgis/file/logs:/mapgis/logs
      - ./mapgis/file/upload:/mapgis/upload
    depends_on:
      - mapgis-nacos
  mapgis-job:
    image: 192.168.177.1:5000/mapgis-job:1.0
    restart: on-failure
    ports:
      - "13000:13000"
    volumes:
      - ./mapgis/job/logs:/mapgis/logs
    depends_on:
      - mapgis-mysql
      - mapgis-nacos
```