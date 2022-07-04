#!/bin/bash
cd `dirname $0`

if [ $UID -ne 0 ]; then
    echo "需要 root 权限执行此脚本"
    exit 1
fi

docker-compose up -d