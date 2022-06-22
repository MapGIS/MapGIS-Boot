#!/bin/sh

cd `dirname $0`

# mapgis-server
IMAGE_NAME=mapgis-server:1.0

cd ./mapgis/server

docker build -t $IMAGE_NAME .
