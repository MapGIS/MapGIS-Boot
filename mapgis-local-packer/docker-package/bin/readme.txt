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

    # 应用端口
    MAPGIS_APP_PORT=8080

4、启动/停止
    启动
    sudo ./startup.sh
    停止
    sudo ./shutdown.sh

5、访问应用
    http://{DOCKER_HOST_IP}:{MAPGIS_APP_PORT}
    其中{DOCKER_HOST_IP}为Docker宿主机的IP，{MAPGIS_APP_PORT}为应用端口