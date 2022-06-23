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

# 复制入口脚本到路径
COPY docker-entrypoint.sh /mapgis/bin

# 安装netcat
RUN echo "deb [check-valid-until=no] http://cdn-fastly.deb.debian.org/debian jessie main" > /etc/apt/sources.list.d/jessie.list
RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
RUN apt-get -o Acquire::Check-Valid-Until=false update
RUN apt-get install -y --no-install-recommends netcat

# 支持中文
ENV LANG=zh_CN.UTF-8

# 设定时区
ENV TZ=Asia/Shanghai

# 暴露端口
EXPOSE 13000

# 启动服务
ENTRYPOINT ["sh", "/mapgis/bin/docker-entrypoint.sh"]