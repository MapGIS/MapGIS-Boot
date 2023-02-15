# 基础镜像
FROM 192.168.177.1:5000/mysql:5.7

# author
LABEL author="mapgis"

# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 执行sql脚本
ADD ./db/*.sql /docker-entrypoint-initdb.d/
