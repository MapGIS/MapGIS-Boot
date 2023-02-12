#!/bin/bash
cd `dirname $0`

LIBS=(
  "mapgis-server"
  )

for lib in ${LIBS[@]}; do
  sh shutdown-process.sh lib/${lib}
done