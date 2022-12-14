#!/bin/bash
cd `dirname $0`

# 平台名称
PLATFORM_NAME=$1
CURRENT_DIR=$(cd `dirname $0`; pwd)
DOCKER_BUILD_PATH=${CURRENT_DIR}/../docker/${PLATFORM_NAME}
RELEASE_DIR=${CURRENT_DIR}/../docker-release/${PLATFORM_NAME}

echo "开始构建平台${PLATFORM_NAME} Docker镜像"

# 进入Docker构建目录
cd $DOCKER_BUILD_PATH

# 赋予执行权限
chmod +x ./copy.sh ./copy-server.sh ./deploy.sh

# 准备打包资源
./copy.sh

# 构建镜像
./deploy.sh build

echo "开始发布平台${PLATFORM_NAME} Docker镜像"

# 发布镜像
./deploy.sh publish

echo "开始打包平台${PLATFORM_NAME}"

# 如果存在目标目录先删除
if [ -d $RELEASE_DIR ]; then
    rm -rf $RELEASE_DIR
fi

# 创建目标目录
mkdir -p $RELEASE_DIR

# 拷贝运行脚本
cp -r $CURRENT_DIR/bin/* $RELEASE_DIR

# 拷贝Docker-Compose脚本
cp .env $RELEASE_DIR
cp docker-compose.yml $RELEASE_DIR

echo "完成打包平台${PLATFORM_NAME}"