#!/bin/bash
cd `dirname $0`
cd ../

CURRENT_DIR=$(cd `dirname $0`; pwd)

LIBS=(
  "mapgis-cloud-module-gateway"
  "mapgis-cloud-module-monitor"
  "mapgis-module-auth-server"
  "mapgis-module-file-server"
  "mapgis-module-job-server"
  "mapgis-module-gen-server"
  "mapgis-module-system-server"
)

for lib in ${LIBS[@]}; do
  sh $CURRENT_DIR/bin/shutdown-process.sh $CURRENT_DIR/lib/${lib}
done