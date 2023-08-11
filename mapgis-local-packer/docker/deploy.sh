#!/bin/bash
cd `dirname $0`

# 产品名称
MAPGIS_PRODUCT_NAME=xxx

# 产品完整名称
MAPGIS_PRODUCT_FULL_NAME=mapgis-xxx

# 镜像标签
MAPGIS_IMAGE_TAG=10.6.4.10

# 镜像操作系统
IMAGE_OS_NAME=$2

# 传入的镜像标签
IMAGE_TAG=$3

# 如果传入的镜像标签参数有值，则用传入的镜像标签
if [ "$IMAGE_TAG" ];then
  MAPGIS_IMAGE_TAG=$IMAGE_TAG
fi

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [build|base|modules|stop|rm|down|publish]"
	exit 1
}

# 构建镜像
build() {
	docker-compose -f docker-compose-local.yml build --no-cache
}

# 启动基础环境（必须）
base(){
	echo "unimplement"
}

# 启动程序模块（必须）
modules(){
	docker-compose -f docker-compose-local.yml up -d
}

# 关闭所有环境/模块
stop(){
	docker-compose -f docker-compose-local.yml stop
}

# 删除所有环境/模块
rm(){
	docker-compose -f docker-compose-local.yml rm
}

# 停用移除所有容器以及网络
down() {
	docker-compose -f docker-compose-local.yml down
}

# 发布镜像到本地
publish() {
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-${IMAGE_OS_NAME} mapgis/${MAPGIS_PRODUCT_NAME}-${IMAGE_OS_NAME}:${MAPGIS_IMAGE_TAG}
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"build")
	build
;;
"base")
	base
;;
"modules")
	modules
;;
"stop")
	stop
;;
"rm")
	rm
;;
"down")
	down
;;
"publish")
	publish
;;
*)
	usage
;;
esac
