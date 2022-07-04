#!/bin/sh

# 私有镜像仓库地址，注意结尾需含/
DOCKER_REGISTRY_URL=192.168.177.1:5000/

# 镜像版本
IMAGE_VERSION=1.0

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
	docker-compose -f docker-compose-local.yml up -d mapgis-xxx-mysql mapgis-xxx-redis mapgis-xxx-nacos
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
	docker tag mapgis/xxx-mysql ${DOCKER_REGISTRY_URL}mapgis/xxx-mysql:${IMAGE_VERSION}
	docker tag mapgis/xxx-gateway ${DOCKER_REGISTRY_URL}mapgis/xxx-gateway:${IMAGE_VERSION}
	docker tag mapgis/xxx-monitor ${DOCKER_REGISTRY_URL}mapgis/xxx-monitor:${IMAGE_VERSION}
	docker tag mapgis/xxx-auth ${DOCKER_REGISTRY_URL}mapgis/xxx-auth:${IMAGE_VERSION}
	docker tag mapgis/xxx-system ${DOCKER_REGISTRY_URL}mapgis/xxx-system:${IMAGE_VERSION}
	docker tag mapgis/xxx-file ${DOCKER_REGISTRY_URL}mapgis/xxx-file:${IMAGE_VERSION}
	docker tag mapgis/xxx-job ${DOCKER_REGISTRY_URL}mapgis/xxx-job:${IMAGE_VERSION}

	docker push ${DOCKER_REGISTRY_URL}nacos/nacos-server
	docker push ${DOCKER_REGISTRY_URL}redis
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-mysql:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-gateway:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-monitor:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-auth:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-system:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-file:${IMAGE_VERSION}
	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx-job:${IMAGE_VERSION}
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
