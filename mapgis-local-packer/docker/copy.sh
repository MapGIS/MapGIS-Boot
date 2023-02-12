#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}

# 拷贝对应平台下的文件
# 平台名称
PLATFORM_NAME=$1
# 源目录
SRC_SERVER_PATH=../release/$PLATFORM_NAME

# 调用copy-server.sh脚本进行拷贝mapgis-server
bash ./copy-server.sh $SRC_SERVER_PATH "./${PLATFORM_NAME}/mapgis/server/app" "mapgis-server"