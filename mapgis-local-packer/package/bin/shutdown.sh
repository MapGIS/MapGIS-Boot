#!/bin/bash
cd `dirname $0`
cd ../

CURRENT_DIR=$(cd `dirname $0`; pwd)

LIBS=(
  "mapgis-server"
  )

for lib in ${LIBS[@]}; do
  sh $CURRENT_DIR/bin/shutdown-process.sh $CURRENT_DIR/lib/${lib}
done