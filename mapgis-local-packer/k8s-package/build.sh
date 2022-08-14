#!/bin/bash
cd `dirname $0`

# 平台名称
PLATFORM_NAME=$1
SUPPORT_DIR=$2
CURRENT_DIR=$(cd `dirname $0`; pwd)
RELEASE_DIR=${CURRENT_DIR}/../k8s-release/${PLATFORM_NAME}

echo "开始打包平台${PLATFORM_NAME}"

# 如果存在目标目录先删除
if [ -d $RELEASE_DIR ]; then
    rm -rf $RELEASE_DIR
fi

# 创建目标目录
mkdir -p $RELEASE_DIR

# 拷贝运行脚本
cp -r $CURRENT_DIR/bin/* $RELEASE_DIR

cp -r $CURRENT_DIR/bin/.helmignore $RELEASE_DIR

# 拷贝helm
cp $SUPPORT_DIR/${PLATFORM_NAME}/helm $RELEASE_DIR

echo "完成打包平台${PLATFORM_NAME}"