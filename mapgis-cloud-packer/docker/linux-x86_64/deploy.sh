#!/bin/sh

# 使用说明，用来提示输入参数
usage() {
	echo "Usage: sh 执行脚本.sh [build|base|modules|stop|rm]"
	exit 1
}

# 构建镜像
build() {
	docker-compose build
}

# 启动基础环境（必须）
base(){
	docker-compose up -d mapgis-xxx-mysql mapgis-xxx-redis mapgis-xxx-nacos
}

# 启动程序模块（必须）
modules(){
	docker-compose up -d
}

# 关闭所有环境/模块
stop(){
	docker-compose stop
}

# 删除所有环境/模块
rm(){
	docker-compose rm
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
*)
	usage
;;
esac
