#!/bin/sh

# 私有镜像仓库地址，注意结尾需含/
DOCKER_REGISTRY_URL=192.168.177.1:5000/

# 镜像版本
IMAGE_VERSION=1.0

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [build|modules|stop|rm|down|publish]"
	exit 1
}

# 构建镜像
build() {
	docker-compose -f docker-compose-local.yml build
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
	docker tag mapgis/xxx ${DOCKER_REGISTRY_URL}mapgis/xxx:${IMAGE_VERSION}

	docker push ${DOCKER_REGISTRY_URL}mapgis/xxx:${IMAGE_VERSION}
}

# 根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
"build")
	build
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
