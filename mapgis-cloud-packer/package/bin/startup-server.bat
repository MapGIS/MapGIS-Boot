@echo off

cd /d %~dp0
cd ../

if "%~1" neq "" set "LIB_NAME=%~1"

for /r %%i in (lib/%LIB_NAME%-*.jar) do set MAPGIS_SERVER_JAR_NAME=%%~ni
set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8
start "%LIB_NAME%" "support/jre/bin/java" %JAVA_OPTS% -jar lib/%MAPGIS_SERVER_JAR_NAME%.jar --spring.profiles.active=prod
exit