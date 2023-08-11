#!/bin/bash
cd `dirname $0`
set -e

# 镜像标签（用于覆盖默认的镜像标签）
IMAGE_TAG=$1

chmod +x ./build.sh

./build.sh linux arm64 $IMAGE_TAG