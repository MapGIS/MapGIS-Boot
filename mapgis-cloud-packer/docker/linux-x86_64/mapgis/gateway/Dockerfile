# 基础镜像
FROM java:8

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

# 支持中文
ENV LANG=zh_CN.UTF-8

# 设定时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 8080

# 启动服务
ENTRYPOINT ["/mapgis/bin/docker-startup.sh", "mapgis-cloud-module-gateway"]