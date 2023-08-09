#!/bin/bash -e

CURRENT_DIR=$(cd `dirname $0`; pwd)
# 是否需要推送的目标镜像仓库，如果需要，则填写仓库地址
DEST_REGISTRY=""

version="$(docker -v | cut -d ' ' -f3 | cut -d ',' -f1)"
MAJOR_W=1
MINOR_W=13
PATCH_W=1
major="${version%%.*}"
minor="${version#$major.}"
minor="${minor%%.*}"
patch="${version#$major.$minor.}"
patch="${patch%%[-.]*}"
shouldWarn=0
if [ "$major" -lt "$MAJOR_W" ]; then
	shouldWarn=1
fi
if [ "$major" -le "$MAJOR_W" ] && [ "$minor" -lt "$MINOR_W" ]; then
	shouldWarn=1
fi
if [ "$major" -le "$MAJOR_W" ] && [ "$minor" -le "$MINOR_W" ] && [ "$patch" -lt "$PATCH_W" ]; then
	shouldWarn=1
fi
if [ "$shouldWarn" -eq 1 ]; then
	cat >&2 <<-EOF
		docker version less than $MAJOR_W.$MINOR_W.$PATCH_W,please update Docker
	EOF
	exit 1
fi

# registry配置
REGISTRY_IMAGE=registry:latest
# 本地临时registry的端口
REGISTRY_PORT=55001

docker load -i $CURRENT_DIR/image_registry.tar
docker run -d -p $REGISTRY_PORT:5000 --name tmp-docker-registry -v $CURRENT_DIR/docker_registry:/var/lib/registry $REGISTRY_IMAGE

sleep 10

pull_from_tmp_registry() {
	imagename=$1
	docker pull 127.0.0.1:$REGISTRY_PORT/$imagename
	docker tag 127.0.0.1:$REGISTRY_PORT/$imagename $imagename
	docker rmi 127.0.0.1:$REGISTRY_PORT/$imagename
}
push_to_registry() {
	imagename=$1
	if [ ! x"$DEST_REGISTRY" = x ]; then
		docker tag $imagename $DEST_REGISTRY/$imagename
		docker push $DEST_REGISTRY/$imagename
	fi
}

load_image() {
	for p in $@; do
		pull_from_tmp_registry "$p"
		push_to_registry "$p"
	done
}

sleep 5
#换行符修正
sed -i 's/\r$//' $CURRENT_DIR/image-list.txt
load_image $(cat $CURRENT_DIR/image-list.txt)

docker stop tmp-docker-registry && docker rm -v tmp-docker-registry
