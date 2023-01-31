---请运行docker version和docker-compose version，确保Docker和Docker Compose已成功安装---

1. 将Docker版的包放到linux服务器的某一目录，如：/opt

2. 安装
    进入应用根目录
    执行以下命令，进行安装
    sudo chmod +x install.sh && sudo ./install.sh

3. 调整参数【必须】
    编辑.env，根据需要修改相关参数，运行sudo vi .env

    # 私有镜像仓库地址，注意结尾需含/，默认为空
    DOCKER_REGISTRY_URL=

    # 镜像标签
    MAPGIS_IMAGE_TAG=10.6.2.10

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

4、启动/停止
    启动
    sudo ./startup.sh
    停止
    sudo ./shutdown.sh

5、访问应用
    http://{DOCKER_HOST_IP}:{GATEWAY_PUBLIC_PORT}
    其中{DOCKER_HOST_IP}为Docker宿主机的IP，{GATEWAY_PUBLIC_PORT}为应用端口