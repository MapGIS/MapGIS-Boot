# 基础镜像
FROM mysql:8

# author
LABEL author="mapgis"

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 执行sql脚本
ADD ./db/*.sql /docker-entrypoint-initdb.d/
