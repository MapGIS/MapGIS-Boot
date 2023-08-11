#!/bin/bash 
set -e

# 待打包镜像来源的仓库,结尾不带'/'，没有为本地
SOURCE_REGISTRY=$1

# 打包需要的registry image配置
REGISTRY_IMAGE=registry:latest
# 本地临时registry的端口
REGISTRY_PORT=55001

CURRENT_DIR=$(cd `dirname $0`; pwd)

OUT_DIR=$CURRENT_DIR/docker-images

if [[ -n "$SOURCE_REGISTRY" ]] ; then
  docker pull $SOURCE_REGISTRY/$REGISTRY_IMAGE
  docker tag $SOURCE_REGISTRY/$REGISTRY_IMAGE $REGISTRY_IMAGE
fi

rm -rf $OUT_DIR
mkdir -p $OUT_DIR/docker_registry
docker run -d -p $REGISTRY_PORT:5000  --name tmp-docker-registry -v $OUT_DIR/docker_registry:/var/lib/registry $REGISTRY_IMAGE

sleep 10

get_from_registry(){
    local imagename=$1;
    if [[ ! x"$SOURCE_REGISTRY" = x ]]; then
        docker pull $SOURCE_REGISTRY/$imagename;
        docker tag $SOURCE_REGISTRY/$imagename $imagename;
		docker rmi $SOURCE_REGISTRY/$imagename;
    fi
}
push_to_tmp(){
   local imagename=$1;
    docker tag $imagename 127.0.0.1:$REGISTRY_PORT/$imagename;
    docker push 127.0.0.1:$REGISTRY_PORT/$imagename;
    docker rmi 127.0.0.1:$REGISTRY_PORT/$imagename;
}

pkg_images(){
	for p in $@;
	do
		get_from_registry "$p";
		push_to_tmp "$p";
	done;
}

sleep 5
#换行符修正
sed -i 's/\r$//' $CURRENT_DIR/image-list.txt
pkg_images $(cat $CURRENT_DIR/image-list.txt)

docker stop tmp-docker-registry && docker rm -v tmp-docker-registry

# registry镜像打包为gz
docker save $REGISTRY_IMAGE > $OUT_DIR/image_registry.tar

cp image-list.txt $OUT_DIR
cp image-load.sh $OUT_DIR
cp readme.txt $OUT_DIR