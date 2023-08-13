#!/bin/bash
cd `dirname $0`
set -e

# 本脚本的作用是
# 1. 上传本地包到远程主机
# 2. 远程执行Docker镜像构建、仓库打包、Docker Compose打包、K8s打包脚本
# 3. 下载远程包到本地主机

# 远程主机
REMOTE_HOST=$1
# 远程用户名
REMOTE_USERNAME=$2
# 镜像操作系统
IMAGE_OS_NAME=$3
# 镜像架构
IMAGE_ARCH=$4
# 镜像标签
IMAGE_TAG=$5
# 时间
ADD_DATE=$6

CURRENT_DIR=$(cd `dirname $0`; pwd)
PLATFORM_NAME=${IMAGE_OS_NAME}-${IMAGE_ARCH}
MAPGIS_PRODUCT_NAME=mapgis-xxx
VERSION=10.6.5.10

# 本地包安装后的位置
INSTALL_DIR=${CURRENT_DIR}/../install
REMOTE_DIR=/home/mapgis/xxx
INSTALL_RELEASE_DIR=${CURRENT_DIR}/../install-local-release

# Docker打包脚本
DOCKER_PACKAGE_SCRIPT=package-$PLATFORM_NAME.sh

# K8s打包脚本
KUBERNETES_PACKAGE_SCRIPT=package-$PLATFORM_NAME.sh

# 是否给包上添加时间
NEED_ADD_DATE=false
DATE_PART=
# 如果传入的镜像标签参数有值，则需要替换image-list.txt里面的标签
if [ "$ADD_DATE" = "ADD_DATE" ];then
  DATE_PART=-$(date +%Y%m%d)
fi
# Docker镜像仓库名称
DOCKER_IMAGES_PACKAGE_NAME=$MAPGIS_PRODUCT_NAME-images-$VERSION$DATE_PART-$PLATFORM_NAME
# Docker包名称
DOCKER_PACKAGE_NAME=$MAPGIS_PRODUCT_NAME-for-docker-$VERSION$DATE_PART-$PLATFORM_NAME
# K8s包名称
KUBERNETES_PACKAGE_NAME=$MAPGIS_PRODUCT_NAME-for-kubernetes-$VERSION$DATE_PART-$PLATFORM_NAME
# K8s-app包名称
KUBERNETES_APP_PACKAGE_NAME=$MAPGIS_PRODUCT_NAME-for-kubernetes-app-$VERSION$DATE_PART-$PLATFORM_NAME

# 根据平台找对应的包
INSTALL_TAR_GZ=$(ls -r ${INSTALL_DIR} | grep '^.*-'${PLATFORM_NAME}'.tar.gz$' | head -n 1)
INSTALL_TAR_GZ_ROOT=${INSTALL_TAR_GZ%.tar.gz}

if [ -z "$INSTALL_TAR_GZ" ]; then
    echo "tar.gz压缩包不存在"
    exit 1
else
    echo "tar.gz压缩包: $INSTALL_TAR_GZ"
fi

# 远程目录清理
ssh -tt $REMOTE_USERNAME@$REMOTE_HOST << eeooff
# 如果存在目标目录先删除
if [ -d $REMOTE_DIR ]; then
    rm -rf $REMOTE_DIR
fi

# 创建目标目录
mkdir -p $REMOTE_DIR/release
exit
eeooff

# scp上传本地包
scp -r ${INSTALL_DIR}/$INSTALL_TAR_GZ ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}/release

# 上传打包脚本docker、docker-package、k8s-app、k8s-package
scp -r ./docker ./docker-package ./k8s-package ./k8s-app ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}

# 远程解压和执行Docker打包命令
ssh -tt $REMOTE_USERNAME@$REMOTE_HOST << eeooff
# 解压
cd ${REMOTE_DIR}/release
tar -xzvf $INSTALL_TAR_GZ
mv $INSTALL_TAR_GZ_ROOT $PLATFORM_NAME

# Docker镜像制作和打包
cd ${REMOTE_DIR}/docker-package
chmod +x $DOCKER_PACKAGE_SCRIPT
sh $DOCKER_PACKAGE_SCRIPT $IMAGE_TAG

# K8s打包
cd ${REMOTE_DIR}/k8s-package
chmod +x $KUBERNETES_PACKAGE_SCRIPT
sh $KUBERNETES_PACKAGE_SCRIPT

# 打包docker-images-release
cd ${REMOTE_DIR}/docker-images-release
mv $PLATFORM_NAME $DOCKER_IMAGES_PACKAGE_NAME
tar -zcvf $DOCKER_IMAGES_PACKAGE_NAME.tar.gz $DOCKER_IMAGES_PACKAGE_NAME

# 打包docker-release
cd ${REMOTE_DIR}/docker-release
mv $PLATFORM_NAME $DOCKER_PACKAGE_NAME
tar -zcvf $DOCKER_PACKAGE_NAME.tar.gz $DOCKER_PACKAGE_NAME

# 打包k8s-release
cd ${REMOTE_DIR}/k8s-release
mv $PLATFORM_NAME $KUBERNETES_PACKAGE_NAME
tar -zcvf $KUBERNETES_PACKAGE_NAME.tar.gz $KUBERNETES_PACKAGE_NAME

# 打包k8s-app-release
mkdir -p ${REMOTE_DIR}/k8s-app-release/$PLATFORM_NAME
cp -rf ${REMOTE_DIR}/k8s-app/* ${REMOTE_DIR}/k8s-app-release/$PLATFORM_NAME
cd ${REMOTE_DIR}/k8s-app-release
mv $PLATFORM_NAME $KUBERNETES_APP_PACKAGE_NAME
tar -zcvf $KUBERNETES_APP_PACKAGE_NAME.tar.gz $KUBERNETES_APP_PACKAGE_NAME

exit
eeooff

# 下载远程包到本地主机
# 如果安装目录是否存在，不存在就创建
if [ ! -d $INSTALL_RELEASE_DIR ]; then
  mkdir $INSTALL_RELEASE_DIR
fi

# 删除同平台的文件
rm -f *$PLATFORM_NAME.tar.gz

# 下载
scp ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}/docker-images-release/$DOCKER_IMAGES_PACKAGE_NAME.tar.gz ${INSTALL_RELEASE_DIR}
scp ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}/docker-release/$DOCKER_PACKAGE_NAME.tar.gz ${INSTALL_RELEASE_DIR}
scp ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}/k8s-release/$KUBERNETES_PACKAGE_NAME.tar.gz ${INSTALL_RELEASE_DIR}
scp ${REMOTE_USERNAME}@${REMOTE_HOST}:${REMOTE_DIR}/k8s-app-release/$KUBERNETES_APP_PACKAGE_NAME.tar.gz ${INSTALL_RELEASE_DIR}

echo "完成打包平台${PLATFORM_NAME}"