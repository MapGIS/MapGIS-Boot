@echo off

cd /d %~dp0
cd ../

for /r %%i in (lib/mapgis-server-*.jar) do set MAPGIS_SERVER_JAR_NAME=%%~ni
set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8
start "mapgis-server" "support/jre/bin/java" %JAVA_OPTS% -jar lib/%MAPGIS_SERVER_JAR_NAME%.jar --spring.profiles.active=prod --spring.config.additional-location=file:config/