#!/bin/bash
cd `dirname $0`

LIBS="mapgis-cloud-module-gateway mapgis-cloud-module-monitor mapgis-module-auth-server mapgis-module-file-server mapgis-module-job-server mapgis-module-system-server"  
for lib in LIBS
do
  sh shutdown-process.sh lib/${lib}
done