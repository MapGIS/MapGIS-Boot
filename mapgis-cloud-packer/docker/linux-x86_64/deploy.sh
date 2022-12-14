#!/bin/sh

# 产品名称
MAPGIS_PRODUCT_NAME=xxx

# 产品完整名称
MAPGIS_PRODUCT_FULL_NAME=mapgis-xxx

# 私有镜像仓库地址，注意结尾需含/
DOCKER_REGISTRY_URL=192.168.177.1:5000/

# 镜像标签
MAPGIS_IMAGE_TAG=10.6.0.10

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [build|base|modules|stop|rm|down|publish]"
	exit 1
}

# 构建镜像
build() {
	docker-compose -f docker-compose-local.yml build
}

# 启动基础环境（必须）
base(){
	docker-compose -f docker-compose-local.yml up -d ${MAPGIS_PRODUCT_FULL_NAME}-mysql ${MAPGIS_PRODUCT_FULL_NAME}-redis ${MAPGIS_PRODUCT_FULL_NAME}-nacos
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

# 发布镜像到私有仓库
publish() {
	docker tag nacos/nacos-server ${DOCKER_REGISTRY_URL}nacos/nacos-server
	docker tag redis ${DOCKER_REGISTRY_URL}redis
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-mysql ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-mysql:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-gateway ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-gateway:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-monitor ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-monitor:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-auth ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-auth:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-system ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-system:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-file ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-file:${MAPGIS_IMAGE_TAG}
	docker tag mapgis/${MAPGIS_PRODUCT_NAME}-job ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-job:${MAPGIS_IMAGE_TAG}
  docker tag mapgis/${MAPGIS_PRODUCT_NAME}-gen ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-gen:${MAPGIS_IMAGE_TAG}

	docker push ${DOCKER_REGISTRY_URL}nacos/nacos-server
	docker push ${DOCKER_REGISTRY_URL}redis
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-mysql:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-gateway:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-monitor:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-auth:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-system:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-file:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-job:${MAPGIS_IMAGE_TAG}
	docker push ${DOCKER_REGISTRY_URL}mapgis/${MAPGIS_PRODUCT_NAME}-gen:${MAPGIS_IMAGE_TAG}
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
