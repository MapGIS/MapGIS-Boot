#!/bin/bash
cd `dirname $0`
set -e

# 平台名称
IMAGE_OS_NAME=$1
IMAGE_ARCH=$2
CURRENT_DIR=$(cd `dirname $0`; pwd)
PLATFORM_NAME=${IMAGE_OS_NAME}-${IMAGE_ARCH}
DOCKER_BUILD_PATH=${CURRENT_DIR}/../docker
IMAGES_BUILD_PATH=${CURRENT_DIR}/../docker/images
IMAGES_RELEASE_DIR=${CURRENT_DIR}/../docker-images-release/${PLATFORM_NAME}

# 进入Docker镜像构建目录
cd $IMAGES_BUILD_PATH

# 准备image-list.txt文件
# 判断是否存在，存在即删除
if [ -f ".image-list.txt" ];then
  rm -f .image-list.txt
fi
# 拷贝image-list.txt到images目录
cp $DOCKER_BUILD_PATH/$PLATFORM_NAME/image-list.txt image-list.txt

# 赋予执行权限
chmod +x ./image-pkg.sh

# 打包镜像
./image-pkg.sh ${IMAGE_OS_NAME}

# 如果存在目标目录先删除
if [ -d $IMAGES_RELEASE_DIR ]; then
    rm -rf $IMAGES_RELEASE_DIR
fi

# 创建目标目录
mkdir -p $IMAGES_RELEASE_DIR

# 拷贝镜像文件
cp -r $DOCKER_BUILD_PATH/images/docker-images/* $IMAGES_RELEASE_DIR

echo "完成打包平台${PLATFORM_NAME} Docker镜像"