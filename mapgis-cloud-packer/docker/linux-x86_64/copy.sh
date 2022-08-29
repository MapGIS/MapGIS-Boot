#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}

# 拷贝对应平台下的文件
# 平台名称
PLATFORM_NAME=`basename $(pwd)`
# 源目录
SRC_SERVER_PATH=../../release/$PLATFORM_NAME

# 拷贝SQL
echo "begin copy sql"
if ls ./mysql/db/*.sql >/dev/null 2>&1; then
	rm ./mysql/db/*.sql
fi
cp $SRC_SERVER_PATH/sql/*.sql ./mysql/db

# 调用copy-server.sh脚本进行拷贝mapgis-module-auth-server
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/auth/app" "mapgis-module-auth-server"

# 调用copy-server.sh脚本进行拷贝mapgis-module-system-server
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/system/app" "mapgis-module-system-server"

# 调用copy-server.sh脚本进行拷贝mapgis-module-file-server
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/file/app" "mapgis-module-file-server"

# 调用copy-server.sh脚本进行拷贝mapgis-module-job-server
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/job/app" "mapgis-module-job-server"

# 调用copy-server.sh脚本进行拷贝mapgis-module-gen-server
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/gen/app" "mapgis-module-gen-server"

# 调用copy-server.sh脚本进行拷贝mapgis-cloud-module-gateway
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/gateway/app" "mapgis-cloud-module-gateway"

# 调用copy-server.sh脚本进行拷贝mapgis-cloud-module-monitor
bash ./copy-server.sh $SRC_SERVER_PATH "./mapgis/monitor/app" "mapgis-cloud-module-monitor"