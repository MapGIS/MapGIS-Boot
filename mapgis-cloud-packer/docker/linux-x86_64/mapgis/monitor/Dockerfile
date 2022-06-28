# 基础镜像
FROM 192.168.177.1:5000/mapgis/java:8

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

# 复制入口脚本到路径
COPY docker-entrypoint.sh /mapgis/bin

# 暴露端口
EXPOSE 9200

# 启动服务
ENTRYPOINT ["sh", "/mapgis/bin/docker-entrypoint.sh"]