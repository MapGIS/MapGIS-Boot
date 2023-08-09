#!/bin/sh

cd `dirname $0`

LIB="mapgis-module-gen-server"
NACOS_SERVER_PORT=8848

# set nacos server port
if [ ! -z "${NACOS_PORT}" ]; then
  NACOS_SERVER_PORT=${NACOS_PORT}
fi

# set nacos server host
if [ ! -z "${NACOS_HOST}" ]; then
  echo ${NACOS_HOST}:${NACOS_SERVER_PORT}
  sh wait-for.sh ${NACOS_HOST}:${NACOS_SERVER_PORT} -t 0 -- sh docker-startup.sh ${LIB}
else
  sh docker-startup.sh ${LIB}
fi