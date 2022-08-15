# 基础镜像
FROM 192.168.177.1:5000/mapgis/openjdk:8u342-jre

# author
LABEL author="mapgis"

# 挂载目录
VOLUME /mapgis

# 创建目录
RUN mkdir -p /mapgis

# 指定路径
WORKDIR /mapgis

# 复制文件到路径
COPY ./app /mapgis

# 暴露端口
EXPOSE 8080

# 启动服务
ENTRYPOINT ["/mapgis/bin/docker-startup.sh"]