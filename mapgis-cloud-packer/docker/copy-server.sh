#!/bin/bash
cd `dirname $0`

# 源目录
SRC_SERVER_PATH=$1
# 目标目录
TARGET_SERVER_PATH=$2
# Jar前缀
LIB=$3
# 完整Jar名称
MAPGIS_SERVER_JAR_NAME=$(ls -r ${SRC_SERVER_PATH}/lib | grep '^'${LIB}'-[0-9\.]*\.jar$' | head -n 1)

# 拷贝对应平台下的文件
echo "begin copy $LIB"

# 如果存在目标目录先删除
if [ -d $TARGET_SERVER_PATH ]; then
    rm -rf $TARGET_SERVER_PATH
fi

# 创建目标目录
mkdir $TARGET_SERVER_PATH

# 拷贝bin目录
cp -dr $SRC_SERVER_PATH/bin $TARGET_SERVER_PATH

# 拷贝lib目录
mkdir $TARGET_SERVER_PATH/lib

cp -dr $SRC_SERVER_PATH/lib/$MAPGIS_SERVER_JAR_NAME $TARGET_SERVER_PATH/lib

chmod -R 777 $TARGET_SERVER_PATH
