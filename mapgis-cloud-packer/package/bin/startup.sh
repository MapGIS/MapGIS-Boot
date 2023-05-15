#!/bin/bash
cd `dirname $0`
cd ../

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
  MAPGIS_SERVER_JAR_NAME=$(ls -r lib | grep '^'${lib}'-[0-9\.]*\.jar$' | head -n 1)
  JAVA_OPTS="-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8 -Dxxx.home=."
  nohup support/jre/bin/java $JAVA_OPTS -jar lib/${MAPGIS_SERVER_JAR_NAME} --spring.profiles.active=prod --spring.config.additional-location=file:config/ >/dev/null 2>&1 &
done